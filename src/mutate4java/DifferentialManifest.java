package mutate4java;

import java.util.List;

record DifferentialManifest(int version, String moduleHash, List<MutationScope> scopes) {
}
