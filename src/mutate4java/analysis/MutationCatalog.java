package mutate4java.analysis;

import mutate4java.project.*;
import mutate4java.report.*;

import mutate4java.model.*;

import mutate4java.*;
import mutate4java.manifest.*;

import com.sun.source.tree.AssignmentTree;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public final class MutationCatalog {

    private final ManifestSupport manifestSupport = new ManifestSupport();
    private final JavaSourceCompiler compiler = new JavaSourceCompiler();
    public List<MutationSite> discover(List<Path> files) throws IOException {
        List<MutationSite> sites = new ArrayList<>();
        for (Path file : files) {
            try {
                sites.addAll(analyze(file).sites());
            } catch (IOException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new IOException("Failed analyzing mutations for " + file, ex);
            }
        }
        sites.sort(Comparator.comparing(MutationSite::file).thenComparingInt(MutationSite::start));
        return sites;
    }
    public SourceAnalysis analyze(Path file) throws Exception {
        String raw = Files.readString(file);
        String source = manifestSupport.stripManifest(raw);
        List<MutationSite> sites = astSites(file, source);
        List<MutationScope> scopes = astScopes(file, source);
        return new SourceAnalysis(source, sites, scopes, manifestSupport.hashScopes(scopes));
    }

    private List<MutationSite> astSites(Path file, String source) throws IOException {
        List<MutationSite> sites = new ArrayList<>();
        CompiledSource compiled = compiler.compile(file);
        for (var unit : compiled.units()) {
            new AstMutationScanner(file, source, unit, compiled.trees(), sites).scan(unit, null);
        }
        return sites;
    }

    private List<MutationScope> astScopes(Path file, String source) throws IOException {
        List<MutationSite> ignoredSites = new ArrayList<>();
        List<MutationScope> scopes = new ArrayList<>();
        CompiledSource compiled = compiler.compile(file);
        for (var unit : compiled.units()) {
            AstMutationScanner scanner = new AstMutationScanner(file, source, unit, compiled.trees(), ignoredSites);
            scanner.scan(unit, null);
            scopes.addAll(scanner.scopes());
        }
        return scopes.stream().sorted(Comparator.comparing(MutationScope::id)).toList();
    }
}

/* mutate4java-manifest
version=1
moduleHash=bc4b3edc2fe4a134db63947a0f661f556e63e039d5815bbfbed095542fbab02c
scope.0.id=Y2xhc3M6TXV0YXRpb25DYXRhbG9nI011dGF0aW9uQ2F0YWxvZzoxOA
scope.0.kind=class
scope.0.startLine=18
scope.0.endLine=64
scope.0.semanticHash=e9a7033a55c325d11f3219b1b640f0d9b59380adcc1eaa79a8bb8996eef29a22
scope.1.id=ZmllbGQ6TXV0YXRpb25DYXRhbG9nI2NvbXBpbGVyOjIx
scope.1.kind=field
scope.1.startLine=21
scope.1.endLine=21
scope.1.semanticHash=27593d311c762939ff55a08c8c9c0b83af67c9ec1ccf62e27810b5ce8fb8ae59
scope.2.id=ZmllbGQ6TXV0YXRpb25DYXRhbG9nI21hbmlmZXN0U3VwcG9ydDoyMA
scope.2.kind=field
scope.2.startLine=20
scope.2.endLine=20
scope.2.semanticHash=eb7aed4f5d767d2c4dc362fe2a437cf6397eed6b1274f9817ce9aa8747f5e4e3
scope.3.id=bWV0aG9kOk11dGF0aW9uQ2F0YWxvZyNhbmFseXplKDEpOjM2
scope.3.kind=method
scope.3.startLine=36
scope.3.endLine=42
scope.3.semanticHash=1f05b4261af0d9aed720bcd1f33e0e32a4721a9ef9abff072f0c4086f67d96ee
scope.4.id=bWV0aG9kOk11dGF0aW9uQ2F0YWxvZyNhc3RTY29wZXMoMik6NTM
scope.4.kind=method
scope.4.startLine=53
scope.4.endLine=63
scope.4.semanticHash=e02d2f270d2831318428d9b5a4de2480982dc6b647abbf15541799240b407a12
scope.5.id=bWV0aG9kOk11dGF0aW9uQ2F0YWxvZyNhc3RTaXRlcygyKTo0NA
scope.5.kind=method
scope.5.startLine=44
scope.5.endLine=51
scope.5.semanticHash=a8020371754249ec30405dc6b8504d749694fa3a54fdbece7e15ebd1270d0068
scope.6.id=bWV0aG9kOk11dGF0aW9uQ2F0YWxvZyNjdG9yKDApOjE4
scope.6.kind=method
scope.6.startLine=1
scope.6.endLine=64
scope.6.semanticHash=13d73541899b9083ad158111663537b668ae58d4cc8d0cb22897c7c21192e414
scope.7.id=bWV0aG9kOk11dGF0aW9uQ2F0YWxvZyNkaXNjb3ZlcigxKToyMg
scope.7.kind=method
scope.7.startLine=22
scope.7.endLine=35
scope.7.semanticHash=fa38d67aa7d2796828dcb23a7758faf0310a2092566c9732a53c18e75ad7cf02
*/
