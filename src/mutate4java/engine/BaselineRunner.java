package mutate4java.engine;

import mutate4java.project.*;
import mutate4java.report.*;

import mutate4java.model.*;

import mutate4java.*;
import mutate4java.analysis.*;
import mutate4java.cli.*;
import mutate4java.coverage.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;
import mutate4java.selection.*;

import mutate4java.selection.*;

import mutate4java.analysis.*;
import mutate4java.coverage.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;

import java.io.PrintStream;
import java.nio.file.Path;

final class BaselineRunner {

    private final CoverageRunner coverageRunner;
    private final PrintStream err;

    BaselineRunner(CoverageRunner coverageRunner, PrintStream err) {
        this.coverageRunner = coverageRunner;
        this.err = err;
    }

    CoverageRun run(CliArguments parsed,
                    TestCommandExecutor executor,
                    Path moduleRoot,
                    ProgressReporter progressReporter) throws Exception {
        progressReporter.baselineStarting(moduleRoot);
        CoverageRun coverageRun = parsed.testCommand() == null
                ? coverageRunner.generateCoverage(moduleRoot)
                : new CoverageRun(executor.runTests(moduleRoot, 0L), CoverageReport.allCovered());
        progressReporter.baselineFinished(coverageRun.baseline());
        return coverageRun;
    }

    int fail(TestRun baseline) {
        if (baseline.timedOut()) {
            err.println("Baseline tests timed out.");
        }
        err.println("Baseline tests failed.");
        err.print(baseline.output());
        return 2;
    }
}

/* mutate4java-manifest
version=1
moduleHash=04f9de2dbd248d979d39d720282ace790a5dfb395df6e81c71d6974f3128269e
scope.0.id=Y2xhc3M6QmFzZWxpbmVSdW5uZXIjQmFzZWxpbmVSdW5uZXI6MjY
scope.0.kind=class
scope.0.startLine=26
scope.0.endLine=56
scope.0.semanticHash=75c8a468ca99f3604490a3da50f7c84fe85dab7156ea803a4d3cd1a9f9914cff
scope.1.id=ZmllbGQ6QmFzZWxpbmVSdW5uZXIjY292ZXJhZ2VSdW5uZXI6Mjg
scope.1.kind=field
scope.1.startLine=28
scope.1.endLine=28
scope.1.semanticHash=d92a1ba1476f655cf1babf2fbbc9b36f71fd57e400859cf09e46a1253a04c184
scope.2.id=ZmllbGQ6QmFzZWxpbmVSdW5uZXIjZXJyOjI5
scope.2.kind=field
scope.2.startLine=29
scope.2.endLine=29
scope.2.semanticHash=0f12a462a677e93faaa05787bc46db2cace278490b3a801f721c167aedea712a
scope.3.id=bWV0aG9kOkJhc2VsaW5lUnVubmVyI2N0b3IoMik6MzE
scope.3.kind=method
scope.3.startLine=31
scope.3.endLine=34
scope.3.semanticHash=d5d3d8a68c0409d2e78d7ce37a218417246e1f76196560fea8c506e86a988d3e
scope.4.id=bWV0aG9kOkJhc2VsaW5lUnVubmVyI2ZhaWwoMSk6NDg
scope.4.kind=method
scope.4.startLine=48
scope.4.endLine=55
scope.4.semanticHash=851c6d4cbbfcb15aa3682241c19bdad48d5f04d12e0bfe6a72f8c881d586f686
scope.5.id=bWV0aG9kOkJhc2VsaW5lUnVubmVyI3J1big0KTozNg
scope.5.kind=method
scope.5.startLine=36
scope.5.endLine=46
scope.5.semanticHash=5712af769ab79174ec956641ecebd59d3a88a22e53548fc5b13e8b2b8b6b2173
*/
