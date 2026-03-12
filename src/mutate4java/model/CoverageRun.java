package mutate4java.model;

import mutate4java.project.*;
import mutate4java.report.*;

import mutate4java.cli.*;
import mutate4java.engine.*;

import mutate4java.selection.*;

import mutate4java.analysis.*;
import mutate4java.coverage.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;

public record CoverageRun(TestRun baseline, CoverageReport report) {
}

/* mutate4java-manifest
version=1
moduleHash=f948cb436a92872480c15e5a3cc61d2e0432275e6bf868fae22d1ef28bc2e732
scope.0.id=Y2xhc3M6Q292ZXJhZ2VSdW4jQ292ZXJhZ2VSdW46MTY
scope.0.kind=class
scope.0.startLine=16
scope.0.endLine=17
scope.0.semanticHash=883514053771daafa709b22988089bb2dfc88c4f950daf0078dfd461f2a48385
scope.1.id=ZmllbGQ6Q292ZXJhZ2VSdW4jYmFzZWxpbmU6MTY
scope.1.kind=field
scope.1.startLine=16
scope.1.endLine=16
scope.1.semanticHash=fa42908ea8a39560f4d3e2a71f3b9616ac12a2a50160f0d9ffc623c132930a6e
scope.2.id=ZmllbGQ6Q292ZXJhZ2VSdW4jcmVwb3J0OjE2
scope.2.kind=field
scope.2.startLine=16
scope.2.endLine=16
scope.2.semanticHash=dc2336483aa1f963fe252a42af3c42ba79e3ce39ea8d06f14aa7261f3c7463f6
scope.3.id=bWV0aG9kOkNvdmVyYWdlUnVuI2N0b3IoMik6MTY
scope.3.kind=method
scope.3.startLine=1
scope.3.endLine=17
scope.3.semanticHash=11fb81bc8fc683ca45d1256be6e2135bda047003a8a934623cb4d3cfa946fcd6
*/
