package mutate4java;

import java.util.Set;

final class CoverageReport {

    private final Set<CoverageSite> coveredLines;
    private final boolean treatAllAsCovered;

    CoverageReport(Set<CoverageSite> coveredLines) {
        this(coveredLines, false);
    }

    private CoverageReport(Set<CoverageSite> coveredLines, boolean treatAllAsCovered) {
        this.coveredLines = coveredLines;
        this.treatAllAsCovered = treatAllAsCovered;
    }

    static CoverageReport allCovered() {
        return new CoverageReport(Set.of(), true);
    }

    boolean covers(String sourcePath, int lineNumber) {
        return treatAllAsCovered || coveredLines.contains(new CoverageSite(sourcePath, lineNumber));
    }
}
