package mutate4java;

import java.util.List;
import java.util.Set;

record CliArguments(CliMode mode,
                    List<String> fileArgs,
                    Set<Integer> lines,
                    boolean sinceLastRun,
                    boolean mutateAll,
                    int timeoutFactor,
                    int mutationWarning,
                    int maxWorkers,
                    String testCommand,
                    boolean verbose) {
}
