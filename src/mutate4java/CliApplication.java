package mutate4java;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class CliApplication {

    private final Path workspaceRoot;
    private final PrintStream out;
    private final PrintStream err;
    private final TestCommandExecutor executor;
    private final CoverageRunner coverageRunner;
    private final WorkspaceManager workspaceManager;
    private final ProgressReporter verboseProgressReporter;
    private final MutationCatalog catalog = new MutationCatalog();
    private final ReportFormatter formatter = new ReportFormatter();
    private final ManifestSupport manifestSupport = new ManifestSupport();

    CliApplication(Path workspaceRoot, PrintStream out, PrintStream err, TestCommandExecutor executor) {
        this(workspaceRoot,
                out,
                err,
                executor,
                new CoverageRunner(new ProcessCommandExecutor()),
                new CopiedWorkspaceManager(),
                new PrintStreamProgressReporter(out));
    }

    CliApplication(Path workspaceRoot,
                   PrintStream out,
                   PrintStream err,
                   TestCommandExecutor executor,
                   CoverageRunner coverageRunner) {
        this(workspaceRoot,
                out,
                err,
                executor,
                coverageRunner,
                new CopiedWorkspaceManager(),
                new PrintStreamProgressReporter(out));
    }

    CliApplication(Path workspaceRoot,
                   PrintStream out,
                   PrintStream err,
                   TestCommandExecutor executor,
                   CoverageRunner coverageRunner,
                   WorkspaceManager workspaceManager) {
        this(workspaceRoot, out, err, executor, coverageRunner, workspaceManager, new PrintStreamProgressReporter(out));
    }

    CliApplication(Path workspaceRoot,
                   PrintStream out,
                   PrintStream err,
                   TestCommandExecutor executor,
                   CoverageRunner coverageRunner,
                   WorkspaceManager workspaceManager,
                   ProgressReporter verboseProgressReporter) {
        this.workspaceRoot = workspaceRoot;
        this.out = out;
        this.err = err;
        this.executor = executor;
        this.coverageRunner = coverageRunner;
        this.workspaceManager = workspaceManager;
        this.verboseProgressReporter = verboseProgressReporter;
    }

    int execute(String[] args) throws Exception {
        ParseOutcome parse = parseArguments(args);
        if (parse.exitCode >= 0) {
            return parse.exitCode;
        }
        CliArguments parsed = parse.arguments;
        TestCommandExecutor selectedExecutor = parsed.testCommand() == null ? executor : executor.withCommand(parsed.testCommand());

        List<Path> files = filesForMode(parsed);
        Path sourceFile = files.get(0);
        Path moduleRoot = moduleRootFor(files);
        ProgressReporter progressReporter = parsed.verbose() ? verboseProgressReporter : new NoOpProgressReporter();

        progressReporter.baselineStarting(moduleRoot);
        CoverageRun coverageRun = parsed.testCommand() == null
                ? coverageRunner.generateCoverage(moduleRoot)
                : new CoverageRun(selectedExecutor.runTests(moduleRoot, 0L), CoverageReport.allCovered());
        TestRun baseline = coverageRun.baseline();
        progressReporter.baselineFinished(baseline);
        if (!baseline.passed()) {
            if (baseline.timedOut()) {
                err.println("Baseline tests timed out.");
            }
            err.println("Baseline tests failed.");
            err.print(baseline.output());
            return 2;
        }

        CoverageReport coverage = coverageRun.report();
        SourceAnalysis analysis = analyze(sourceFile);
        DifferentialSelection differentialSelection = selectSites(sourceFile, parsed, analysis);
        List<MutationSite> discovered = filterByLines(differentialSelection.selected(), parsed.lines());
        CoverageSelection coverageSelection = filterCoveredSites(moduleRoot, discovered, coverage);
        StringBuilder extra = new StringBuilder();
        if (differentialSelection.unchangedModule()) {
            extra.append("No mutations need testing.\n");
        }
        if (coverageSelection.covered().size() > parsed.mutationWarning()) {
            extra.append("WARNING: Found ").append(coverageSelection.covered().size())
                    .append(" mutations. Consider splitting this module.\n");
        }
        if (coverageSelection.covered().isEmpty()) {
            if (baseline.passed()) {
                writeManifest(sourceFile, analysis);
            }
            out.print(formatter.format(workspaceRoot, baseline, extra.toString(), coverageSelection.uncovered(), List.of()));
            return 0;
        }

        long timeoutMillis = mutantTimeoutMillis(baseline.durationMillis(), parsed.timeoutFactor());
        List<MutationResult> results = runMutations(
                moduleRoot,
                coverageSelection.covered(),
                timeoutMillis,
                parsed.maxWorkers(),
                progressReporter,
                selectedExecutor
        );
        int exit = results.stream().anyMatch(result -> !result.killed()) ? 3 : 0;
        if (exit == 0) {
            writeManifest(sourceFile, analysis);
        }
        out.print(formatter.format(workspaceRoot, baseline, extra.toString(), coverageSelection.uncovered(), results));
        return exit;
    }

    private ParseOutcome parseArguments(String[] args) {
        try {
            CliArguments parsed = CliArgumentsParser.parse(args);
            if (parsed.mode() == CliMode.HELP) {
                out.println(Main.usage());
                return ParseOutcome.exit(0);
            }
            return ParseOutcome.ok(parsed);
        } catch (IllegalArgumentException ex) {
            err.println(ex.getMessage());
            out.println(Main.usage());
            return ParseOutcome.exit(1);
        }
    }

    private List<Path> filesForMode(CliArguments parsed) throws Exception {
        return List.of(explicitFile(parsed.fileArgs().get(0)));
    }

    private List<MutationResult> runMutations(Path moduleRoot,
                                              List<MutationSite> sites,
                                              long timeoutMillis,
                                              int maxWorkers,
                                              ProgressReporter progressReporter,
                                              TestCommandExecutor testExecutor) throws Exception {
        List<MutationJob> jobs = new ArrayList<>();
        for (int i = 0; i < sites.size(); i++) {
            MutationSite site = sites.get(i);
            jobs.add(new MutationJob(site, moduleRoot.relativize(site.file()), timeoutMillis, i, sites.size()));
        }
        int workerCount = Math.max(1, Math.min(jobs.size(), maxWorkers));
        try (WorkerWorkspaces workspaces = workspaceManager.createWorkerWorkspaces(moduleRoot, workerCount);
             WorkerPool pool = new ParallelWorkerPool(workspaces.workerRoots(), testExecutor, progressReporter)) {
            return pool.runAll(jobs);
        }
    }

    private SourceAnalysis analyze(Path sourceFile) throws Exception {
        return catalog.analyze(sourceFile);
    }

    private DifferentialSelection selectSites(Path sourceFile, CliArguments parsed, SourceAnalysis analysis) throws Exception {
        if (parsed.mutateAll()) {
            return new DifferentialSelection(analysis.sites(), false);
        }
        if (!parsed.sinceLastRun() && !parsed.lines().isEmpty()) {
            return new DifferentialSelection(analysis.sites(), false);
        }
        var manifest = manifestSupport.read(sourceFile);
        if (manifest.isEmpty()) {
            return new DifferentialSelection(analysis.sites(), false);
        }
        DifferentialManifest previous = manifest.get();
        if (previous.moduleHash().equals(analysis.moduleHash())) {
            return new DifferentialSelection(List.of(), true);
        }
        java.util.Map<String, String> previousHashes = new java.util.LinkedHashMap<>();
        for (MutationScope scope : previous.scopes()) {
            previousHashes.put(scope.id(), scope.semanticHash());
        }
        java.util.Set<String> changedScopes = new java.util.LinkedHashSet<>();
        for (MutationScope scope : analysis.scopes()) {
            if (!scope.semanticHash().equals(previousHashes.get(scope.id()))) {
                changedScopes.add(scope.id());
            }
        }
        List<MutationSite> selected = analysis.sites().stream()
                .filter(site -> changedScopes.contains(site.scopeId()))
                .toList();
        return new DifferentialSelection(selected, false);
    }

    private List<MutationSite> filterByLines(List<MutationSite> sites, Set<Integer> lines) {
        if (lines.isEmpty()) {
            return sites;
        }
        return sites.stream()
                .filter(site -> lines.contains(site.lineNumber()))
                .toList();
    }

    private long mutantTimeoutMillis(long baselineDurationMillis, int timeoutFactor) {
        long baseline = Math.max(1L, baselineDurationMillis);
        return Math.max(1_000L, baseline * timeoutFactor);
    }

    private CoverageSelection filterCoveredSites(Path moduleRoot, List<MutationSite> sites, CoverageReport coverage) {
        List<MutationSite> covered = new ArrayList<>();
        List<MutationSite> uncovered = new ArrayList<>();
        for (MutationSite site : sites) {
            if (coverage.covers(sourceSuffix(moduleRoot, site.file()), site.lineNumber())) {
                covered.add(site);
            } else {
                uncovered.add(site);
            }
        }
        return new CoverageSelection(List.copyOf(covered), List.copyOf(uncovered));
    }

    String sourceSuffix(Path moduleRoot, Path file) {
        String relative = moduleRoot.relativize(file).toString().replace('\\', '/');
        if (relative.startsWith("src/main/java/")) {
            return relative.substring("src/main/java/".length());
        }
        if (relative.startsWith("src/test/java/")) {
            return relative.substring("src/test/java/".length());
        }
        if (relative.startsWith("src/")) {
            return relative.substring("src/".length());
        }
        return relative;
    }

    private Path explicitFile(String arg) {
        Path path = workspaceRoot.resolve(arg).normalize();
        if (java.nio.file.Files.isDirectory(path)) {
            throw new IllegalArgumentException("mutate4java target must be a .java file");
        }
        return path;
    }

    Path moduleRootFor(List<Path> files) {
        Path moduleRoot = findModuleRoot(files.get(0));
        return moduleRoot == null ? workspaceRoot : moduleRoot;
    }

    private Path findModuleRoot(Path file) {
        Path current = java.nio.file.Files.isDirectory(file) ? file : file.getParent();
        while (current != null && current.startsWith(workspaceRoot)) {
            if (java.nio.file.Files.exists(current.resolve("pom.xml"))) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private record ParseOutcome(CliArguments arguments, int exitCode) {

        private static ParseOutcome ok(CliArguments arguments) {
            return new ParseOutcome(arguments, -1);
        }

        private static ParseOutcome exit(int code) {
            return new ParseOutcome(null, code);
        }
    }

    private record CoverageSelection(List<MutationSite> covered, List<MutationSite> uncovered) {
    }

    private void writeManifest(Path sourceFile, SourceAnalysis analysis) throws Exception {
        manifestSupport.write(sourceFile,
                analysis.sourceWithoutManifest(),
                new DifferentialManifest(1, analysis.moduleHash(), analysis.scopes()));
    }
}
