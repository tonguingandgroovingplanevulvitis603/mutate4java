package mutate4java;

import java.util.List;

record SourceAnalysis(String sourceWithoutManifest, List<MutationSite> sites, List<MutationScope> scopes, String moduleHash) {
}
