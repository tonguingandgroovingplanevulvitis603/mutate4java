package mutate4java;

import java.util.List;

record DifferentialSelection(List<MutationSite> selected, boolean unchangedModule) {
}
