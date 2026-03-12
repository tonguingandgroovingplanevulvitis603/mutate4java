package mutate4java;

import java.nio.file.Path;

record MutationSite(Path file,
                    int lineNumber,
                    int start,
                    int end,
                    String originalText,
                    String replacementText,
                    String description,
                    String scopeId,
                    String scopeKind,
                    int scopeStartLine,
                    int scopeEndLine) {

    MutationSite(Path file,
                 int lineNumber,
                 int start,
                 int end,
                 String originalText,
                 String replacementText,
                 String description) {
        this(file, lineNumber, start, end, originalText, replacementText, description,
                "scope:" + lineNumber, "unknown", lineNumber, lineNumber);
    }
}
