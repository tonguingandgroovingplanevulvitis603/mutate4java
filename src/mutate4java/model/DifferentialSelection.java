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

import java.util.List;

public record DifferentialSelection(List<MutationSite> selected, boolean unchangedModule) {
}

/* mutate4java-manifest
version=1
moduleHash=d100a6b0045e982c0b69adf9e9f5f69accc97b5146c19424add17d1cae0ac9ea
scope.0.id=Y2xhc3M6RGlmZmVyZW50aWFsU2VsZWN0aW9uI0RpZmZlcmVudGlhbFNlbGVjdGlvbjoxOA
scope.0.kind=class
scope.0.startLine=18
scope.0.endLine=19
scope.0.semanticHash=84e50b4bf336cb4816c8108dca5e3ac808d534b58c7fc5f1bed4a37c95c0f93f
scope.1.id=ZmllbGQ6RGlmZmVyZW50aWFsU2VsZWN0aW9uI3NlbGVjdGVkOjE4
scope.1.kind=field
scope.1.startLine=18
scope.1.endLine=18
scope.1.semanticHash=86a0598a1221f1369c897132ad8c64024634fb4a93e2e6a39c935a9aebe33fec
scope.2.id=ZmllbGQ6RGlmZmVyZW50aWFsU2VsZWN0aW9uI3VuY2hhbmdlZE1vZHVsZToxOA
scope.2.kind=field
scope.2.startLine=18
scope.2.endLine=18
scope.2.semanticHash=3339d5bf5751b9930aa106167a5c7b2151733a4bbafd8bc45708f732f35251d7
scope.3.id=bWV0aG9kOkRpZmZlcmVudGlhbFNlbGVjdGlvbiNjdG9yKDIpOjE4
scope.3.kind=method
scope.3.startLine=1
scope.3.endLine=19
scope.3.semanticHash=47c901e7d50a344e80d566f8aeb96696ef4997983b85ad88002e0d2a2ef6c1f3
*/
