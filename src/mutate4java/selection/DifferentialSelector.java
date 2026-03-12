package mutate4java.selection;

import mutate4java.project.*;
import mutate4java.report.*;

import mutate4java.model.*;

import mutate4java.*;
import mutate4java.manifest.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public final class DifferentialSelector {

    private final ManifestSupport manifestSupport;
    private final ChangedScopeFinder changedScopeFinder;

    public DifferentialSelector(ManifestSupport manifestSupport) {
        this.manifestSupport = manifestSupport;
        this.changedScopeFinder = new ChangedScopeFinder(manifestSupport);
    }

    public DifferentialSelection select(Path sourceFile, CliArguments parsed, SourceAnalysis analysis) throws Exception {
        if (parsed.mutateAll()) {
            return new DifferentialSelection(analysis.sites(), false);
        }
        if (!parsed.sinceLastRun() && !parsed.lines().isEmpty()) {
            return new DifferentialSelection(analysis.sites(), false);
        }
        Set<String> changedScopes = changedScopeIds(sourceFile, analysis);
        if (changedScopes.isEmpty() && manifestSupport.read(sourceFile).isEmpty()) {
            return new DifferentialSelection(analysis.sites(), false);
        }
        if (changedScopes.isEmpty()) {
            return new DifferentialSelection(List.of(), true);
        }
        List<MutationSite> selected = analysis.sites().stream()
                .filter(site -> changedScopes.contains(site.scopeId()))
                .toList();
        return new DifferentialSelection(selected, false);
    }

    public Set<String> changedScopeIds(Path sourceFile, SourceAnalysis analysis) throws Exception {
        return changedScopeFinder.changedScopeIds(sourceFile, analysis);
    }
}

/* mutate4java-manifest
version=1
moduleHash=7c2a34c02df6047a62eed1be52371d016c4a07bf8037bb177f9edc455d44b86c
scope.0.id=Y2xhc3M6RGlmZmVyZW50aWFsU2VsZWN0b3IjRGlmZmVyZW50aWFsU2VsZWN0b3I6MTU
scope.0.kind=class
scope.0.startLine=15
scope.0.endLine=48
scope.0.semanticHash=54bac6135486f937e5db87b5931714ae003491c526b71256fd7a7aae9503c905
scope.1.id=ZmllbGQ6RGlmZmVyZW50aWFsU2VsZWN0b3IjY2hhbmdlZFNjb3BlRmluZGVyOjE4
scope.1.kind=field
scope.1.startLine=18
scope.1.endLine=18
scope.1.semanticHash=fc4e59b94bc92b74030de30c51b4f1d01cb532c277ef308db1ad8e130ccba1e3
scope.2.id=ZmllbGQ6RGlmZmVyZW50aWFsU2VsZWN0b3IjbWFuaWZlc3RTdXBwb3J0OjE3
scope.2.kind=field
scope.2.startLine=17
scope.2.endLine=17
scope.2.semanticHash=3b18e7680a38456f29abcc80d1cf6ae7426fd744949d701cb129be20a8759a6a
scope.3.id=bWV0aG9kOkRpZmZlcmVudGlhbFNlbGVjdG9yI2NoYW5nZWRTY29wZUlkcygyKTo0NQ
scope.3.kind=method
scope.3.startLine=45
scope.3.endLine=47
scope.3.semanticHash=664916cd2948a22a4bd2b85b8421012340e1d7464237e5cb93c08f392bb2cc1f
scope.4.id=bWV0aG9kOkRpZmZlcmVudGlhbFNlbGVjdG9yI2N0b3IoMSk6MjA
scope.4.kind=method
scope.4.startLine=20
scope.4.endLine=23
scope.4.semanticHash=53715d99a51cc343d443c7ce5f6034e14227e38078304e477c493b59b8ffbda4
scope.5.id=bWV0aG9kOkRpZmZlcmVudGlhbFNlbGVjdG9yI3NlbGVjdCgzKToyNQ
scope.5.kind=method
scope.5.startLine=25
scope.5.endLine=43
scope.5.semanticHash=940a529fb7fc4726c273e0bd9ee9dcc64384252dab1861d642df7e345f06b903
*/
