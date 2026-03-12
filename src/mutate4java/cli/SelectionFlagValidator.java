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

final class SelectionFlagValidator {

    void validate(CliArgumentParseState state) {
        validateScanConflicts(state);
        validateUpdateManifestConflicts(state);
        validateLineConflicts(state);
        validateDifferentialConflicts(state);
    }

    private void validateScanConflicts(CliArgumentParseState state) {
        reject(state.scan && state.sinceLastRun, "--scan may not be combined with --since-last-run");
        reject(state.scan && state.mutateAll, "--scan may not be combined with --mutate-all");
        reject(state.scan && state.updateManifest, "--scan may not be combined with --update-manifest");
    }

    private void validateUpdateManifestConflicts(CliArgumentParseState state) {
        reject(state.updateManifest && state.sinceLastRun, "--update-manifest may not be combined with --since-last-run");
        reject(state.updateManifest && state.mutateAll, "--update-manifest may not be combined with --mutate-all");
        reject(state.updateManifest && !state.lines.isEmpty(), "--update-manifest may not be combined with --lines");
    }

    private void validateLineConflicts(CliArgumentParseState state) {
        reject(!state.lines.isEmpty() && state.sinceLastRun, "--lines may not be combined with --since-last-run");
        reject(!state.lines.isEmpty() && state.mutateAll, "--lines may not be combined with --mutate-all");
    }

    private void validateDifferentialConflicts(CliArgumentParseState state) {
        reject(state.sinceLastRun && state.mutateAll, "--since-last-run may not be combined with --mutate-all");
    }

    private void reject(boolean invalid, String message) {
        if (invalid) {
            throw new IllegalArgumentException(message);
        }
    }
}

/* mutate4java-manifest
version=1
moduleHash=827068a1817d9d9cd024cc9c40a48b4aa4d460c0e494337e553a383df231a43e
scope.0.id=Y2xhc3M6U2VsZWN0aW9uRmxhZ1ZhbGlkYXRvciNTZWxlY3Rpb25GbGFnVmFsaWRhdG9yOjIz
scope.0.kind=class
scope.0.startLine=23
scope.0.endLine=58
scope.0.semanticHash=c691598dda7fae7d48d60beeee2e6806733dd70bd16db4305123db615fbd1238
scope.1.id=bWV0aG9kOlNlbGVjdGlvbkZsYWdWYWxpZGF0b3IjY3RvcigwKToyMw
scope.1.kind=method
scope.1.startLine=1
scope.1.endLine=58
scope.1.semanticHash=5b3485509948ef5c30b9cfe3209609f87e1697a3503c322133301c6ac70dac51
scope.2.id=bWV0aG9kOlNlbGVjdGlvbkZsYWdWYWxpZGF0b3IjcmVqZWN0KDIpOjUz
scope.2.kind=method
scope.2.startLine=53
scope.2.endLine=57
scope.2.semanticHash=beb57718ee98cd2e44786f9f7764444772cd20995646adb1e5db64f62ba5b5c1
scope.3.id=bWV0aG9kOlNlbGVjdGlvbkZsYWdWYWxpZGF0b3IjdmFsaWRhdGUoMSk6MjU
scope.3.kind=method
scope.3.startLine=25
scope.3.endLine=30
scope.3.semanticHash=448299af2d6633bd5069d5506965d6a2d9698272fec3c3e65a022419a4792b28
scope.4.id=bWV0aG9kOlNlbGVjdGlvbkZsYWdWYWxpZGF0b3IjdmFsaWRhdGVEaWZmZXJlbnRpYWxDb25mbGljdHMoMSk6NDk
scope.4.kind=method
scope.4.startLine=49
scope.4.endLine=51
scope.4.semanticHash=2556eb12f3c09c56dc5bc50e4490d9cbd79b624e511a337ac3be878a70a282f9
scope.5.id=bWV0aG9kOlNlbGVjdGlvbkZsYWdWYWxpZGF0b3IjdmFsaWRhdGVMaW5lQ29uZmxpY3RzKDEpOjQ0
scope.5.kind=method
scope.5.startLine=44
scope.5.endLine=47
scope.5.semanticHash=1362d0e99f0b95042cd22c8527a30f07743a33a67c96a6e0ee75fbf4c7d840c6
scope.6.id=bWV0aG9kOlNlbGVjdGlvbkZsYWdWYWxpZGF0b3IjdmFsaWRhdGVTY2FuQ29uZmxpY3RzKDEpOjMy
scope.6.kind=method
scope.6.startLine=32
scope.6.endLine=36
scope.6.semanticHash=1de6d148b5985ce1f1418f8f096789c77eb5c65a3a5d7639100b1ed560ca2bb3
scope.7.id=bWV0aG9kOlNlbGVjdGlvbkZsYWdWYWxpZGF0b3IjdmFsaWRhdGVVcGRhdGVNYW5pZmVzdENvbmZsaWN0cygxKTozOA
scope.7.kind=method
scope.7.startLine=38
scope.7.endLine=42
scope.7.semanticHash=64c11a5fc6ca47c3b1df7630507fee08ebb091a7d8e85782f0fc13ace2d805d1
*/
