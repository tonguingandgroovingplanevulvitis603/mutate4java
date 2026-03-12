package mutate4java.coverage;

import mutate4java.project.*;
import mutate4java.report.*;

import mutate4java.model.*;

import mutate4java.*;
import mutate4java.exec.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class CoverageRunner {

    static final long COVERAGE_TIMEOUT_MILLIS = 300_000L;

    private final ProcessCommandExecutor executor;
    private final CoverageCleaner cleaner = new CoverageCleaner();
    public CoverageRunner(ProcessCommandExecutor executor) {
        this.executor = executor;
    }
    public CoverageRun generateCoverage(Path projectRoot) throws Exception {
        Path jacocoDir = projectRoot.resolve("target/site/jacoco");
        Path jacocoExec = projectRoot.resolve("target/jacoco.exec");
        cleaner.deleteStaleCoverage(jacocoDir, jacocoExec);

        CommandResult result = executor.run(List.of(
                "mvn", "-q",
                "org.jacoco:jacoco-maven-plugin:0.8.12:prepare-agent",
                "test",
                "org.jacoco:jacoco-maven-plugin:0.8.12:report"
        ), projectRoot, COVERAGE_TIMEOUT_MILLIS);
        CoverageReport report = result.exitCode() == 0
                ? JacocoLineCoverageParser.parse(jacocoDir.resolve("jacoco.xml"))
                : new CoverageReport(Set.of());
        return new CoverageRun(
                new TestRun(result.exitCode(), result.output(), result.durationMillis(), result.timedOut()),
                report
        );
    }
}

/* mutate4java-manifest
version=1
moduleHash=c68bc7ba9661c1ebc81557c9a8ae1f564b68af69dc3d3bdfdb1232c3377e88ae
scope.0.id=Y2xhc3M6Q292ZXJhZ2VSdW5uZXIjQ292ZXJhZ2VSdW5uZXI6MTU
scope.0.kind=class
scope.0.startLine=15
scope.0.endLine=43
scope.0.semanticHash=bc6de6839634bd8b9821da2c196e4975ddb9b62d3645cfd190929e1087899f49
scope.1.id=ZmllbGQ6Q292ZXJhZ2VSdW5uZXIjQ09WRVJBR0VfVElNRU9VVF9NSUxMSVM6MTc
scope.1.kind=field
scope.1.startLine=17
scope.1.endLine=17
scope.1.semanticHash=6e5c508ee1d9fb99aaf963644e6d2fc5ce1cd711d34b57d45d09baba6d3eec9d
scope.2.id=ZmllbGQ6Q292ZXJhZ2VSdW5uZXIjY2xlYW5lcjoyMA
scope.2.kind=field
scope.2.startLine=20
scope.2.endLine=20
scope.2.semanticHash=e054311929a09448bb9f5fa070ebf5cd46aee39e391c54a1ae6ea4f6d91c1f70
scope.3.id=ZmllbGQ6Q292ZXJhZ2VSdW5uZXIjZXhlY3V0b3I6MTk
scope.3.kind=field
scope.3.startLine=19
scope.3.endLine=19
scope.3.semanticHash=36f4c7e4d9eee22de597df01707b2a19afbccc80cd17a6814874870e814db1e2
scope.4.id=bWV0aG9kOkNvdmVyYWdlUnVubmVyI2N0b3IoMSk6MjE
scope.4.kind=method
scope.4.startLine=21
scope.4.endLine=23
scope.4.semanticHash=0bfa41f3538d9de31a3184d53c3350206581fc85892b3a0727e1404fc9a4bff8
scope.5.id=bWV0aG9kOkNvdmVyYWdlUnVubmVyI2dlbmVyYXRlQ292ZXJhZ2UoMSk6MjQ
scope.5.kind=method
scope.5.startLine=24
scope.5.endLine=42
scope.5.semanticHash=1ef00f5ad94ad168f6d60e0056b0e9ae7fe07caabaf6b8a7a2d5e3189876839d
*/
