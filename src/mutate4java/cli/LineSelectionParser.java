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

import java.util.LinkedHashSet;
import java.util.Set;

final class LineSelectionParser {

    private final IntegerArgumentParser integers = new IntegerArgumentParser();

    Set<Integer> parse(String text) {
        Set<Integer> lines = new LinkedHashSet<>();
        for (String part : text.split(",")) {
            if (!part.isBlank()) {
                lines.add(integers.parsePositiveInt(part.trim(), "--lines"));
            }
        }
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("--lines requires at least one line number");
        }
        return Set.copyOf(lines);
    }
}

/* mutate4java-manifest
version=1
moduleHash=550b1aea07f0f751af0dc86f3842e74bf9fb95c5d8e167a1796c8c418017c3e9
scope.0.id=Y2xhc3M6TGluZVNlbGVjdGlvblBhcnNlciNMaW5lU2VsZWN0aW9uUGFyc2VyOjI2
scope.0.kind=class
scope.0.startLine=26
scope.0.endLine=42
scope.0.semanticHash=ee927c0ef1d52fde8767fdd92dc40076a6dd5a94539a248966a732e7598c1380
scope.1.id=ZmllbGQ6TGluZVNlbGVjdGlvblBhcnNlciNpbnRlZ2VyczoyOA
scope.1.kind=field
scope.1.startLine=28
scope.1.endLine=28
scope.1.semanticHash=062a16f8af0e1767ed813374fc22e50ee4184e83a3ace5cdd70702abccd7203b
scope.2.id=bWV0aG9kOkxpbmVTZWxlY3Rpb25QYXJzZXIjY3RvcigwKToyNg
scope.2.kind=method
scope.2.startLine=1
scope.2.endLine=42
scope.2.semanticHash=4954f32f494bb4370a48b50c01a638c4ed455418eb908e77f22421d089fc90ac
scope.3.id=bWV0aG9kOkxpbmVTZWxlY3Rpb25QYXJzZXIjcGFyc2UoMSk6MzA
scope.3.kind=method
scope.3.startLine=30
scope.3.endLine=41
scope.3.semanticHash=304190b689296d291b14fb76bb64948d60eebd5b26a3657b50c6045adcedbe8a
*/
