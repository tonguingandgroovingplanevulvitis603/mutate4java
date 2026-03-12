package mutate4java.report;

import mutate4java.model.*;

import mutate4java.cli.*;
import mutate4java.engine.*;

import mutate4java.selection.*;

import mutate4java.analysis.*;
import mutate4java.coverage.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;

import java.nio.file.Path;
import java.util.List;

public final class ReportFormatter {

    public String format(Path projectRoot,
                         TestRun baseline,
                         String extra,
                         List<MutationSite> uncovered,
                         List<MutationResult> results) {
        StringBuilder out = new StringBuilder();
        out.append("Baseline tests passed in ").append(baseline.durationMillis()).append(" ms.\n");
        if (extra != null && !extra.isBlank()) {
            out.append(extra);
        }
        for (MutationSite site : uncovered) {
            out.append("UNCOVERED ");
            out.append(projectRoot.relativize(site.file())).append(':');
            out.append(site.lineNumber()).append(' ');
            out.append(site.description()).append('\n');
        }
        for (MutationResult result : results) {
            out.append(result.killed() ? "KILLED " : "SURVIVED ");
            out.append(projectRoot.relativize(result.site().file())).append(':');
            out.append(result.site().lineNumber()).append(' ');
            out.append(result.site().description()).append(" (");
            out.append(result.durationMillis()).append(" ms)\n");
            if (result.timedOut()) {
                out.append("  timed out\n");
            }
        }
        long killed = results.stream().filter(MutationResult::killed).count();
        long survived = results.size() - killed;
        out.append("Coverage: ").append(uncovered.size()).append(" uncovered sites skipped.\n");
        out.append("Summary: ").append(killed).append(" killed, ");
        out.append(survived).append(" survived, ");
        out.append(results.size()).append(" total.\n");
        return out.toString();
    }
}

/* mutate4java-manifest
version=1
moduleHash=de0dfc5187bd3061e96f566ebf15c8a483cf9b20357eb9fbb2175b11d293be9e
scope.0.id=Y2xhc3M6UmVwb3J0Rm9ybWF0dGVyI1JlcG9ydEZvcm1hdHRlcjoxOA
scope.0.kind=class
scope.0.startLine=18
scope.0.endLine=54
scope.0.semanticHash=cc2e05449b0e05227bcfe6953b54a7873fc9e76ea9260d48a04972e37da0dad3
scope.1.id=bWV0aG9kOlJlcG9ydEZvcm1hdHRlciNjdG9yKDApOjE4
scope.1.kind=method
scope.1.startLine=1
scope.1.endLine=54
scope.1.semanticHash=7e4013a7a117020616467b6ee706c84bac65f6d9305278c500ff0eaf0375f036
scope.2.id=bWV0aG9kOlJlcG9ydEZvcm1hdHRlciNmb3JtYXQoNSk6MjA
scope.2.kind=method
scope.2.startLine=20
scope.2.endLine=53
scope.2.semanticHash=54a0dea841634d8a8f03036e0720c5115f5a31fbb1b5a608967fbf134e0f6eaa
*/
