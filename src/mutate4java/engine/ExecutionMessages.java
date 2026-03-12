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

final class ExecutionMessages {

    String extraText(CliArguments parsed,
                     DifferentialSelection differentialSelection,
                     CoverageSelection coverageSelection) {
        StringBuilder extra = new StringBuilder();
        if (differentialSelection.unchangedModule()) {
            extra.append("No mutations need testing.\n");
        }
        if (coverageSelection.covered().size() > parsed.mutationWarning()) {
            extra.append("WARNING: Found ").append(coverageSelection.covered().size())
                    .append(" mutations. Consider splitting this module.\n");
        }
        return extra.toString();
    }
}

/* mutate4java-manifest
version=1
moduleHash=8ac4e0ccf9a88fd31171afb78a49c3558afa107dcc70d9c5ccc3717bac3622f3
scope.0.id=Y2xhc3M6RXhlY3V0aW9uTWVzc2FnZXMjRXhlY3V0aW9uTWVzc2FnZXM6MjM
scope.0.kind=class
scope.0.startLine=23
scope.0.endLine=38
scope.0.semanticHash=99bb414a56169ad26939398a9cc1cad3cadbd1f353cc3b0ba10a6114ec48b1de
scope.1.id=bWV0aG9kOkV4ZWN1dGlvbk1lc3NhZ2VzI2N0b3IoMCk6MjM
scope.1.kind=method
scope.1.startLine=1
scope.1.endLine=38
scope.1.semanticHash=9d30374b3010950b6e546ae3a0dc1a999af49ac06e9d3a2bebe4bccfa2fcc951
scope.2.id=bWV0aG9kOkV4ZWN1dGlvbk1lc3NhZ2VzI2V4dHJhVGV4dCgzKToyNQ
scope.2.kind=method
scope.2.startLine=25
scope.2.endLine=37
scope.2.semanticHash=b0305c46923b2b36fd9591d3985c304a4cf71102cdc8d815270aca7ee8678a32
*/
