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

public final class CliExecutionFactory {

    public CliExecution create(Path workspaceRoot,
                               PrintStream out,
                               PrintStream err,
                               TestCommandExecutor executor,
                               CoverageRunner coverageRunner,
                               WorkspaceManager workspaceManager,
                               ProgressReporter verboseProgressReporter,
                               ProjectLayout layout) {
        ManifestSupport manifestSupport = new ManifestSupport();
        return new CliExecution(
                workspaceRoot,
                out,
                err,
                executor,
                coverageRunner,
                verboseProgressReporter,
                new MutationCatalog(),
                new ReportFormatter(),
                manifestSupport,
                layout,
                new DifferentialSelector(manifestSupport),
                new MutationCoverageFilter(layout),
                new MutationExecution(workspaceManager),
                new ScanReportFormatter(workspaceRoot)
        );
    }
}

/* mutate4java-manifest
version=1
moduleHash=a684ab4c53e06fcf788cb560fbfe1515d976502ceec79ef920aefbda26560988
scope.0.id=Y2xhc3M6Q2xpRXhlY3V0aW9uRmFjdG9yeSNDbGlFeGVjdXRpb25GYWN0b3J5OjI2
scope.0.kind=class
scope.0.startLine=26
scope.0.endLine=54
scope.0.semanticHash=5391b843791cd472f60a6cae18d420def2a765ecdf0edcc5b7268817fab0c121
scope.1.id=bWV0aG9kOkNsaUV4ZWN1dGlvbkZhY3RvcnkjY3JlYXRlKDgpOjI4
scope.1.kind=method
scope.1.startLine=28
scope.1.endLine=53
scope.1.semanticHash=bb0d16eed00805386f17c84230fcb997b89d0838eee96075625ac51e40d38de6
scope.2.id=bWV0aG9kOkNsaUV4ZWN1dGlvbkZhY3RvcnkjY3RvcigwKToyNg
scope.2.kind=method
scope.2.startLine=1
scope.2.endLine=54
scope.2.semanticHash=d739422561aeddc06958c935783996e713f7125fb838151dfb63c3dbd46a230d
*/
