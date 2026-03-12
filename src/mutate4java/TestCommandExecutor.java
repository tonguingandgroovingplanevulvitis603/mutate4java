package mutate4java;

import mutate4java.project.*;
import mutate4java.report.*;

import mutate4java.model.*;

import mutate4java.cli.*;
import mutate4java.engine.*;

import mutate4java.selection.*;

import mutate4java.analysis.*;
import mutate4java.coverage.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;

import java.io.IOException;
import java.nio.file.Path;

public interface TestCommandExecutor {

    TestRun runTests(Path projectRoot, long timeoutMillis) throws IOException, InterruptedException;

    default TestCommandExecutor withCommand(String command) {
        return this;
    }
}

/* mutate4java-manifest
version=1
moduleHash=a7023870590bd11bf0706c273891af04e9407f4c0556f991813d296399718d50
scope.0.id=Y2xhc3M6VGVzdENvbW1hbmRFeGVjdXRvciNUZXN0Q29tbWFuZEV4ZWN1dG9yOjIx
scope.0.kind=class
scope.0.startLine=21
scope.0.endLine=28
scope.0.semanticHash=06afdb84bf373453eea8bdcbf3ffa9acbede3dec1dac0a736db5a31fa1012b5e
scope.1.id=bWV0aG9kOlRlc3RDb21tYW5kRXhlY3V0b3IjcnVuVGVzdHMoMik6MjM
scope.1.kind=method
scope.1.startLine=23
scope.1.endLine=23
scope.1.semanticHash=8c6238ff865450926a0f839f24a855cc017e6b0c368b956fb1533dc8cba194d4
scope.2.id=bWV0aG9kOlRlc3RDb21tYW5kRXhlY3V0b3Ijd2l0aENvbW1hbmQoMSk6MjU
scope.2.kind=method
scope.2.startLine=25
scope.2.endLine=27
scope.2.semanticHash=6e2c904dc36f19cdd2dc030c4aa729f7e4e31b3b8d1f523764340f147f264b22
*/
