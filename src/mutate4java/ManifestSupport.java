package mutate4java;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class ManifestSupport {

    private static final String START = "/* mutate4java-manifest\n";
    private static final String END = "*/";

    Optional<DifferentialManifest> read(Path sourceFile) throws Exception {
        String raw = Files.readString(sourceFile);
        int start = manifestStart(raw);
        if (start < 0) {
            return Optional.empty();
        }
        int end = raw.indexOf(END, start);
        if (end < 0) {
            return Optional.empty();
        }
        String body = raw.substring(start + START.length(), end).trim();
        return Optional.of(parse(body));
    }

    String stripManifest(String rawSource) {
        int start = manifestStart(rawSource);
        if (start < 0) {
            return rawSource;
        }
        return rawSource.substring(0, start).stripTrailing() + "\n";
    }

    void write(Path sourceFile, String sourceWithoutManifest, DifferentialManifest manifest) throws Exception {
        String updated = sourceWithoutManifest.stripTrailing() + "\n\n" + serialize(manifest);
        Files.writeString(sourceFile, updated);
    }

    String hashScopes(List<MutationScope> scopes) {
        StringBuilder out = new StringBuilder();
        scopes.stream()
                .sorted(Comparator.comparing(MutationScope::id))
                .forEach(scope -> out.append(scope.id()).append('|').append(scope.semanticHash()).append('\n'));
        return hash(out.toString());
    }

    String hash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte value : bytes) {
                hex.append(String.format("%02x", value));
            }
            return hex.toString();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to hash manifest content", ex);
        }
    }

    private DifferentialManifest parse(String body) {
        Map<Integer, Map<String, String>> scopes = new LinkedHashMap<>();
        int version = 1;
        String moduleHash = "";
        for (String line : body.split("\n")) {
            if (line.isBlank()) {
                continue;
            }
            int separator = line.indexOf('=');
            if (separator < 0) {
                continue;
            }
            String key = line.substring(0, separator);
            String value = line.substring(separator + 1);
            if ("version".equals(key)) {
                version = Integer.parseInt(value);
            } else if ("moduleHash".equals(key)) {
                moduleHash = value;
            } else if (key.startsWith("scope.")) {
                parseScopeLine(scopes, key, value);
            }
        }
        List<MutationScope> parsedScopes = scopes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> toScope(entry.getValue()))
                .toList();
        return new DifferentialManifest(version, moduleHash, parsedScopes);
    }

    private void parseScopeLine(Map<Integer, Map<String, String>> scopes, String key, String value) {
        String[] parts = key.split("\\.");
        if (parts.length != 3) {
            return;
        }
        int index = Integer.parseInt(parts[1]);
        scopes.computeIfAbsent(index, ignored -> new LinkedHashMap<>()).put(parts[2], value);
    }

    private MutationScope toScope(Map<String, String> values) {
        return new MutationScope(
                decode(values.get("id")),
                values.get("kind"),
                Integer.parseInt(values.get("startLine")),
                Integer.parseInt(values.get("endLine")),
                values.get("semanticHash")
        );
    }

    private String serialize(DifferentialManifest manifest) {
        StringBuilder out = new StringBuilder();
        out.append(START);
        out.append("version=").append(manifest.version()).append('\n');
        out.append("moduleHash=").append(manifest.moduleHash()).append('\n');
        for (int i = 0; i < manifest.scopes().size(); i++) {
            MutationScope scope = manifest.scopes().get(i);
            out.append("scope.").append(i).append(".id=").append(encode(scope.id())).append('\n');
            out.append("scope.").append(i).append(".kind=").append(scope.kind()).append('\n');
            out.append("scope.").append(i).append(".startLine=").append(scope.startLine()).append('\n');
            out.append("scope.").append(i).append(".endLine=").append(scope.endLine()).append('\n');
            out.append("scope.").append(i).append(".semanticHash=").append(scope.semanticHash()).append('\n');
        }
        out.append(END).append('\n');
        return out.toString();
    }

    private String encode(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String decode(String value) {
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }

    private int manifestStart(String raw) {
        int start = raw.lastIndexOf(START);
        if (start < 0) {
            return -1;
        }
        String tail = raw.substring(start);
        return tail.trim().endsWith(END) ? start : -1;
    }
}
