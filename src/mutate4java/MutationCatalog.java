package mutate4java;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static javax.tools.JavaCompiler.CompilationTask;
import static javax.tools.StandardLocation.CLASS_OUTPUT;
import static javax.tools.StandardLocation.CLASS_PATH;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
final class MutationCatalog {

    private final ManifestSupport manifestSupport = new ManifestSupport();

    List<MutationSite> discover(List<Path> files) throws IOException {
        List<MutationSite> sites = new ArrayList<>();
        for (Path file : files) {
            try {
                sites.addAll(analyze(file).sites());
            } catch (IOException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new IOException("Failed analyzing mutations for " + file, ex);
            }
        }
        sites.sort(Comparator.comparing(MutationSite::file).thenComparingInt(MutationSite::start));
        return sites;
    }

    SourceAnalysis analyze(Path file) throws Exception {
        String raw = Files.readString(file);
        String source = manifestSupport.stripManifest(raw);
        List<MutationSite> sites = astSites(file, source);
        List<MutationScope> scopes = astScopes(file, source);
        return new SourceAnalysis(source, sites, scopes, manifestSupport.hashScopes(scopes));
    }

    private List<MutationSite> astSites(Path file, String source) throws IOException {
        List<MutationSite> sites = new ArrayList<>();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.ROOT, null)) {
            Path outputDir = Files.createTempDirectory("mutate4java-classes");
            fileManager.setLocationFromPaths(CLASS_OUTPUT, List.of(outputDir));
            fileManager.setLocationFromPaths(CLASS_PATH, List.of());
            Iterable<? extends JavaFileObject> javaFiles = fileManager.getJavaFileObjects(file.toFile());
            CompilationTask task = compiler.getTask(
                    null,
                    fileManager,
                    diagnostics,
                    List.of("-proc:none", "-implicit:none"),
                    null,
                    javaFiles
            );
            JavacTask javacTask = (JavacTask) task;
            List<CompilationUnitTree> units = new ArrayList<>();
            for (CompilationUnitTree unit : javacTask.parse()) {
                units.add(unit);
            }
            javacTask.analyze();
            Trees trees = Trees.instance(javacTask);
            for (CompilationUnitTree unit : units) {
                new AstMutationScanner(file, source, unit, trees, sites).scan(unit, null);
            }
        }
        return sites;
    }

    private List<MutationScope> astScopes(Path file, String source) throws IOException {
        List<MutationSite> ignoredSites = new ArrayList<>();
        List<MutationScope> scopes = new ArrayList<>();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.ROOT, null)) {
            Path outputDir = Files.createTempDirectory("mutate4java-classes");
            fileManager.setLocationFromPaths(CLASS_OUTPUT, List.of(outputDir));
            fileManager.setLocationFromPaths(CLASS_PATH, List.of());
            Iterable<? extends JavaFileObject> javaFiles = fileManager.getJavaFileObjects(file.toFile());
            CompilationTask task = compiler.getTask(null, fileManager, diagnostics,
                    List.of("-proc:none", "-implicit:none"), null, javaFiles);
            JavacTask javacTask = (JavacTask) task;
            List<CompilationUnitTree> units = new ArrayList<>();
            for (CompilationUnitTree unit : javacTask.parse()) {
                units.add(unit);
            }
            javacTask.analyze();
            Trees trees = Trees.instance(javacTask);
            for (CompilationUnitTree unit : units) {
                AstMutationScanner scanner = new AstMutationScanner(file, source, unit, trees, ignoredSites);
                scanner.scan(unit, null);
                scopes.addAll(scanner.scopes());
            }
        }
        return scopes.stream().sorted(Comparator.comparing(MutationScope::id)).toList();
    }
}
