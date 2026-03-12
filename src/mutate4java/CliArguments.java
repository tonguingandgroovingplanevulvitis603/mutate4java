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

import java.util.List;
import java.util.Set;

public record CliArguments(CliMode mode,
                    List<String> fileArgs,
                    Set<Integer> lines,
                    boolean scan,
                    boolean updateManifest,
                    boolean sinceLastRun,
                    boolean mutateAll,
                    int timeoutFactor,
                    int mutationWarning,
                    int maxWorkers,
                    String testCommand,
                    boolean verbose) {
}

/* mutate4java-manifest
version=1
moduleHash=d589eb572a7f665f065ab749f77fb2c9739f015d9b9b79a5f1d66d6c0219121a
scope.0.id=Y2xhc3M6Q2xpQXJndW1lbnRzI0NsaUFyZ3VtZW50czoyMQ
scope.0.kind=class
scope.0.startLine=21
scope.0.endLine=33
scope.0.semanticHash=8b09d0d41d5153d18800349ed3f04b2c4aae15f9e56b937ecf8f56af4b18c32b
scope.1.id=ZmllbGQ6Q2xpQXJndW1lbnRzI2ZpbGVBcmdzOjIy
scope.1.kind=field
scope.1.startLine=22
scope.1.endLine=22
scope.1.semanticHash=f80baf7e7953a4656e562aaf16e4f07371e3273850635d8563b47631b1dad17c
scope.2.id=ZmllbGQ6Q2xpQXJndW1lbnRzI2xpbmVzOjIz
scope.2.kind=field
scope.2.startLine=23
scope.2.endLine=23
scope.2.semanticHash=8a75ee2e82541226ee127d34811baf42045b1c69d8b37903c1fb78cde148348a
scope.3.id=ZmllbGQ6Q2xpQXJndW1lbnRzI21heFdvcmtlcnM6MzA
scope.3.kind=field
scope.3.startLine=30
scope.3.endLine=30
scope.3.semanticHash=cb9e580b0dbcba22d95de3dcbc46151b4a0fd0354ec55d5550643f4bd515f69a
scope.4.id=ZmllbGQ6Q2xpQXJndW1lbnRzI21vZGU6MjE
scope.4.kind=field
scope.4.startLine=21
scope.4.endLine=21
scope.4.semanticHash=b09c0d200fb0d0f48cf18d6f308f4a995a0bdc16401be97c6b5ac3be74abcc23
scope.5.id=ZmllbGQ6Q2xpQXJndW1lbnRzI211dGF0ZUFsbDoyNw
scope.5.kind=field
scope.5.startLine=27
scope.5.endLine=27
scope.5.semanticHash=45b9ddd73abb90f7661c5660a536201133ef36f9d37780c314976b8369329893
scope.6.id=ZmllbGQ6Q2xpQXJndW1lbnRzI211dGF0aW9uV2FybmluZzoyOQ
scope.6.kind=field
scope.6.startLine=29
scope.6.endLine=29
scope.6.semanticHash=db70f05471a31f019eeea08e8c09a2a723520a01342f16ffb145bae63dd1da10
scope.7.id=ZmllbGQ6Q2xpQXJndW1lbnRzI3NjYW46MjQ
scope.7.kind=field
scope.7.startLine=24
scope.7.endLine=24
scope.7.semanticHash=7f10f199c6d010c2ad9bfc4c060bbff97d152c95e333e09f9e5f223e7657e8dc
scope.8.id=ZmllbGQ6Q2xpQXJndW1lbnRzI3NpbmNlTGFzdFJ1bjoyNg
scope.8.kind=field
scope.8.startLine=26
scope.8.endLine=26
scope.8.semanticHash=e3aab04e984210af337a062a2e746b648c5424dd0129379132745921c8fbc330
scope.9.id=ZmllbGQ6Q2xpQXJndW1lbnRzI3Rlc3RDb21tYW5kOjMx
scope.9.kind=field
scope.9.startLine=31
scope.9.endLine=31
scope.9.semanticHash=fcaba91778b2be1d513e1fa0c2dfed255038dc22dc4a56902203442ff3a99cbb
scope.10.id=ZmllbGQ6Q2xpQXJndW1lbnRzI3RpbWVvdXRGYWN0b3I6Mjg
scope.10.kind=field
scope.10.startLine=28
scope.10.endLine=28
scope.10.semanticHash=1b3ea5376e0c3c20dc9146685258db39ce3876efb7e4fa354f84d6a18bd61237
scope.11.id=ZmllbGQ6Q2xpQXJndW1lbnRzI3VwZGF0ZU1hbmlmZXN0OjI1
scope.11.kind=field
scope.11.startLine=25
scope.11.endLine=25
scope.11.semanticHash=7ffdcb49b2b92703aeea63659102c2dd1bb4465c7da15555e74cef6ff4e51a22
scope.12.id=ZmllbGQ6Q2xpQXJndW1lbnRzI3ZlcmJvc2U6MzI
scope.12.kind=field
scope.12.startLine=32
scope.12.endLine=32
scope.12.semanticHash=bb1902dc1650551d06c41dfc0f5c22f3860a3d2377acd2928894bea2b86ccfb6
scope.13.id=bWV0aG9kOkNsaUFyZ3VtZW50cyNjdG9yKDEyKToyMQ
scope.13.kind=method
scope.13.startLine=1
scope.13.endLine=33
scope.13.semanticHash=6ec9ee2b70cfa16ec68dcb8a43ecef2fa51249febb6d95be9a31dd99f350df10
*/
