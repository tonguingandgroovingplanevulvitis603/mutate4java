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

public record CommandResult(int exitCode, String output, long durationMillis, boolean timedOut) {
}

/* mutate4java-manifest
version=1
moduleHash=02d4227464971577d61261d5c859671d152fa98dc6c9600c351bce064b0ee550
scope.0.id=Y2xhc3M6Q29tbWFuZFJlc3VsdCNDb21tYW5kUmVzdWx0OjE2
scope.0.kind=class
scope.0.startLine=16
scope.0.endLine=17
scope.0.semanticHash=2a52bd3c1837dbb92d23f0e979fc1b18ba700870ef7bd63f728c6d07082df24e
scope.1.id=ZmllbGQ6Q29tbWFuZFJlc3VsdCNkdXJhdGlvbk1pbGxpczoxNg
scope.1.kind=field
scope.1.startLine=16
scope.1.endLine=16
scope.1.semanticHash=aa9aa63ad847ef3f927bafd418f3e392b391e4c0075d48a91d10d27edf303bc3
scope.2.id=ZmllbGQ6Q29tbWFuZFJlc3VsdCNleGl0Q29kZToxNg
scope.2.kind=field
scope.2.startLine=16
scope.2.endLine=16
scope.2.semanticHash=22d20a5f7c9173958dfb701f79fb99a4bb0b0451e48a6d3c48c78a1f0d2ef019
scope.3.id=ZmllbGQ6Q29tbWFuZFJlc3VsdCNvdXRwdXQ6MTY
scope.3.kind=field
scope.3.startLine=16
scope.3.endLine=16
scope.3.semanticHash=a7385d49a8e8309a95b92e10b7c0f0563448f907f09582aa98f5020ce07e6008
scope.4.id=ZmllbGQ6Q29tbWFuZFJlc3VsdCN0aW1lZE91dDoxNg
scope.4.kind=field
scope.4.startLine=16
scope.4.endLine=16
scope.4.semanticHash=15d6b8ef15a135d7a7402858996de81a1a61f74338bfc1158a2c3dd58a852ac8
scope.5.id=bWV0aG9kOkNvbW1hbmRSZXN1bHQjY3Rvcig0KToxNg
scope.5.kind=method
scope.5.startLine=1
scope.5.endLine=17
scope.5.semanticHash=dd24944fb3e6732941d8f73f7b6bf1e2cdd9809fa4fd440d5d903e3a911b2a78
*/
