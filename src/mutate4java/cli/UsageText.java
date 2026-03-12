package mutate4java.cli;

import mutate4java.project.*;
import mutate4java.report.*;

import mutate4java.model.*;

import mutate4java.*;
import mutate4java.analysis.*;
import mutate4java.coverage.*;
import mutate4java.engine.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;
import mutate4java.selection.*;

import mutate4java.selection.*;

import mutate4java.analysis.*;
import mutate4java.coverage.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;

final class UsageText {

    private UsageText() {
    }

    static String text() {
        return """
                Usage:
                  mutate4java <file.java>                      Mutate one Java source file
                  mutate4java <file.java> --scan              Print mutation-site scan without running tests
                  mutate4java <file.java> --update-manifest   Write embedded manifest without running tests
                  mutate4java <file.java> --lines 12,18       Restrict mutations to specific source lines
                  mutate4java <file.java> --since-last-run    Mutate only scopes changed since embedded manifest
                  mutate4java <file.java> --mutate-all        Ignore embedded manifest and mutate all covered sites
                  mutate4java <file.java> --mutation-warning 50 Warn when selected mutation count exceeds threshold
                  mutate4java <file.java> --max-workers 4     Limit parallel worker count
                  mutate4java <file.java> --timeout-factor 15 Set mutant timeout as baseline multiplier
                  mutate4java <file.java> --test-command CMD  Override the test command used for baseline and mutants
                  mutate4java <file.java> --verbose           Print live worker progress
                  mutate4java --help                          Print this help message
                """;
    }
}

/* mutate4java-manifest
version=1
moduleHash=3ab6a696068a5b3b9aef41c04dc62aa8674cd973c9f19d4df283791d7641fe38
scope.0.id=Y2xhc3M6VXNhZ2VUZXh0I1VzYWdlVGV4dDoyMw
scope.0.kind=class
scope.0.startLine=23
scope.0.endLine=45
scope.0.semanticHash=3a4bc410b3ed5ede6ca675cdf90fbeb1945a9ed052ed635737314e2aa63509d3
scope.1.id=bWV0aG9kOlVzYWdlVGV4dCNjdG9yKDApOjI1
scope.1.kind=method
scope.1.startLine=25
scope.1.endLine=26
scope.1.semanticHash=2c5260a62764098ff916549096f15e840ae635710b3d87f6432fe9b9f3353d2b
scope.2.id=bWV0aG9kOlVzYWdlVGV4dCN0ZXh0KDApOjI4
scope.2.kind=method
scope.2.startLine=28
scope.2.endLine=44
scope.2.semanticHash=132a01a22df2c3d44b998a823d37872dc5027e45d7b8675001708a4ab75c6db5
*/
