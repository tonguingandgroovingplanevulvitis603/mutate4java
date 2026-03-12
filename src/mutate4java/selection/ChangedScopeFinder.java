package mutate4java.selection;

import mutate4java.project.*;
import mutate4java.report.*;

import mutate4java.model.*;

import mutate4java.*;
import mutate4java.coverage.*;
import mutate4java.manifest.*;

import mutate4java.analysis.*;
import mutate4java.coverage.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

final class ChangedScopeFinder {

    private final ManifestSupport manifestSupport;

    ChangedScopeFinder(ManifestSupport manifestSupport) {
        this.manifestSupport = manifestSupport;
    }

    Set<String> changedScopeIds(Path sourceFile, SourceAnalysis analysis) throws Exception {
        var manifest = manifestSupport.read(sourceFile);
        if (manifest.isEmpty()) {
            return Set.of();
        }
        DifferentialManifest previous = manifest.get();
        if (previous.moduleHash().equals(analysis.moduleHash())) {
            return Set.of();
        }
        Map<String, String> previousHashes = new LinkedHashMap<>();
        for (MutationScope scope : previous.scopes()) {
            previousHashes.put(scope.id(), scope.semanticHash());
        }
        Set<String> changedScopes = new LinkedHashSet<>();
        for (MutationScope scope : analysis.scopes()) {
            if (!scope.semanticHash().equals(previousHashes.get(scope.id()))) {
                changedScopes.add(scope.id());
            }
        }
        return Set.copyOf(changedScopes);
    }
}

/* mutate4java-manifest
version=1
moduleHash=25a040293f6d28985d260ca6144bfedfc3af1a08171534f00f75482b7c48722d
scope.0.id=Y2xhc3M6Q2hhbmdlZFNjb3BlRmluZGVyI0NoYW5nZWRTY29wZUZpbmRlcjoyMw
scope.0.kind=class
scope.0.startLine=23
scope.0.endLine=52
scope.0.semanticHash=70d84a89443c7bf0d540aaadaf001c9efbbfef70aa49ccc20f71bad86e63fb87
scope.1.id=ZmllbGQ6Q2hhbmdlZFNjb3BlRmluZGVyI21hbmlmZXN0U3VwcG9ydDoyNQ
scope.1.kind=field
scope.1.startLine=25
scope.1.endLine=25
scope.1.semanticHash=3b18e7680a38456f29abcc80d1cf6ae7426fd744949d701cb129be20a8759a6a
scope.2.id=bWV0aG9kOkNoYW5nZWRTY29wZUZpbmRlciNjaGFuZ2VkU2NvcGVJZHMoMik6MzE
scope.2.kind=method
scope.2.startLine=31
scope.2.endLine=51
scope.2.semanticHash=ab5fa2ed09e78c0fb26a2cf78b057ea6bd992084c5fa7398df28dc10363a68dd
scope.3.id=bWV0aG9kOkNoYW5nZWRTY29wZUZpbmRlciNjdG9yKDEpOjI3
scope.3.kind=method
scope.3.startLine=27
scope.3.endLine=29
scope.3.semanticHash=a7ba28ae9642105bbc7bdf7c40a631eac5ae6df21a366e6a3c834a782d804aa5
*/
