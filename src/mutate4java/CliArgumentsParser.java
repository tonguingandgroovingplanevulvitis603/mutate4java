package mutate4java;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

final class CliArgumentsParser {

    private static final int DEFAULT_TIMEOUT_FACTOR = 10;
    private static final int DEFAULT_MUTATION_WARNING = 50;
    private static final int DEFAULT_MAX_WORKERS = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);

    private CliArgumentsParser() {
    }

    static CliArguments parse(String[] args) {
        ParseState state = new ParseState();
        for (int i = 0; i < args.length; i++) {
            i = parseArgument(args, i, state);
        }
        if (state.help) {
            return new CliArguments(CliMode.HELP, List.of(), Set.of(), false, false,
                    state.timeoutFactor, state.mutationWarning, state.maxWorkers, null, state.verbose);
        }
        validateSelectionFlags(state);
        ensureExactlyOneJavaFile(state.values);
        return new CliArguments(
                CliMode.EXPLICIT_FILES,
                List.copyOf(state.values),
                state.lines,
                state.sinceLastRun,
                state.mutateAll,
                state.timeoutFactor,
                state.mutationWarning,
                state.maxWorkers,
                state.testCommand,
                state.verbose
        );
    }

    private static int parseArgument(String[] args, int index, ParseState state) {
        String arg = args[index];
        return switch (arg) {
            case "--help" -> state.help(index);
            case "--verbose" -> state.verbose(index);
            case "--lines" -> parseFlagValue(args, index, "--lines", value -> state.lines(parseLines(value)));
            case "--since-last-run" -> state.sinceLastRun(index);
            case "--mutate-all" -> state.mutateAll(index);
            case "--timeout-factor" -> parseFlagValue(args, index, "--timeout-factor",
                    value -> state.timeoutFactor(parsePositiveInt(value, "--timeout-factor")));
            case "--mutation-warning" -> parseFlagValue(args, index, "--mutation-warning",
                    value -> state.mutationWarning(parsePositiveInt(value, "--mutation-warning")));
            case "--max-workers" -> parseFlagValue(args, index, "--max-workers",
                    value -> state.maxWorkers(parsePositiveInt(value, "--max-workers")));
            case "--test-command" -> parseFlagValue(args, index, "--test-command", state::testCommand);
            default -> addFileArgument(arg, index, state);
        };
    }

    private static void validateSelectionFlags(ParseState state) {
        if (!state.lines.isEmpty() && state.sinceLastRun) {
            throw new IllegalArgumentException("--lines may not be combined with --since-last-run");
        }
        if (!state.lines.isEmpty() && state.mutateAll) {
            throw new IllegalArgumentException("--lines may not be combined with --mutate-all");
        }
        if (state.sinceLastRun && state.mutateAll) {
            throw new IllegalArgumentException("--since-last-run may not be combined with --mutate-all");
        }
    }

    private static int parseFlagValue(String[] args, int index, String flag, java.util.function.Consumer<String> consumer) {
        int valueIndex = index + 1;
        ensureHasValue(args, valueIndex, flag);
        consumer.accept(args[valueIndex]);
        return valueIndex;
    }

    private static int addFileArgument(String arg, int index, ParseState state) {
        if (arg.startsWith("--")) {
            throw new IllegalArgumentException("Unknown option: " + arg);
        }
        state.values.add(arg);
        return index;
    }

    private static void ensureHasValue(String[] args, int index, String flag) {
        if (index >= args.length || args[index].startsWith("--")) {
            throw new IllegalArgumentException(flag + " requires a value");
        }
    }

    private static Set<Integer> parseLines(String text) {
        Set<Integer> lines = new LinkedHashSet<>();
        for (String part : text.split(",")) {
            if (part.isBlank()) {
                continue;
            }
            lines.add(parsePositiveInt(part.trim(), "--lines"));
        }
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("--lines requires at least one line number");
        }
        return Set.copyOf(lines);
    }

    private static int parsePositiveInt(String text, String flag) {
        try {
            int value = Integer.parseInt(text);
            if (value <= 0) {
                throw new IllegalArgumentException(flag + " must be a positive integer");
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(flag + " must be a positive integer");
        }
    }

    private static void ensureExactlyOneJavaFile(List<String> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("mutate4java requires exactly one Java file");
        }
        if (values.size() != 1) {
            throw new IllegalArgumentException("mutate4java accepts exactly one Java file");
        }
        if (!values.get(0).endsWith(".java")) {
            throw new IllegalArgumentException("mutate4java target must be a .java file");
        }
    }

    private static final class ParseState {
        private boolean help;
        private boolean verbose;
        private Set<Integer> lines = Set.of();
        private boolean sinceLastRun;
        private boolean mutateAll;
        private int timeoutFactor = DEFAULT_TIMEOUT_FACTOR;
        private int mutationWarning = DEFAULT_MUTATION_WARNING;
        private int maxWorkers = DEFAULT_MAX_WORKERS;
        private String testCommand;
        private final List<String> values = new ArrayList<>();

        private int help(int index) {
            help = true;
            return index;
        }

        private int verbose(int index) {
            verbose = true;
            return index;
        }

        private int sinceLastRun(int index) {
            sinceLastRun = true;
            return index;
        }

        private int mutateAll(int index) {
            mutateAll = true;
            return index;
        }

        private void lines(Set<Integer> value) {
            lines = value;
        }

        private void timeoutFactor(int value) {
            timeoutFactor = value;
        }

        private void mutationWarning(int value) {
            mutationWarning = value;
        }

        private void maxWorkers(int value) {
            maxWorkers = value;
        }

        private void testCommand(String value) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("--test-command must not be blank");
            }
            testCommand = value;
        }
    }
}
