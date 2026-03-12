package mutate4java;

import com.sun.source.tree.AssignmentTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.ReturnTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.UnaryTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

final class AstMutationScanner extends TreePathScanner<Void, Void> {
    private final Path file;
    private final String source;
    private final CompilationUnitTree unit;
    private final Trees trees;
    private final List<MutationSite> sites;
    private final List<MutationScope> scopes = new ArrayList<>();
    private final ManifestSupport manifestSupport = new ManifestSupport();
    private final Deque<String> classNames = new ArrayDeque<>();

    AstMutationScanner(Path file, String source, CompilationUnitTree unit, Trees trees, List<MutationSite> sites) {
        this.file = file;
        this.source = source;
        this.unit = unit;
        this.trees = trees;
        this.sites = sites;
    }

    List<MutationScope> scopes() {
        return List.copyOf(scopes);
    }

    @Override
    public Void visitClass(ClassTree node, Void unused) {
        classNames.push(node.getSimpleName().toString());
        addScope(classScope(node));
        try {
            return super.visitClass(node, unused);
        } finally {
            classNames.pop();
        }
    }

    @Override
    public Void visitMethod(MethodTree node, Void unused) {
        addScope(methodScope(node));
        return super.visitMethod(node, unused);
    }

    @Override
    public Void visitLiteral(LiteralTree node, Void unused) {
        if (node.getValue() instanceof Boolean bool) {
            String original = bool ? "true" : "false";
            String replacement = bool ? "false" : "true";
            int start = (int) trees.getSourcePositions().getStartPosition(unit, node);
            if (start >= 0) {
                sites.add(site(start, start + original.length(), original, replacement));
            }
        } else if (node.getValue() instanceof Integer value && (value == 0 || value == 1)) {
            String original = Integer.toString(value);
            String replacement = value == 0 ? "1" : "0";
            int start = (int) trees.getSourcePositions().getStartPosition(unit, node);
            if (start >= 0) {
                sites.add(site(start, start + original.length(), original, replacement));
            }
        }
        return super.visitLiteral(node, unused);
    }

    @Override
    public Void visitBinary(BinaryTree node, Void unused) {
        String original = operatorText(node.getKind());
        String replacement = operatorReplacement(node.getKind());
        if (original != null && replacement != null) {
            MutationSite mutation = binarySite(node, original, replacement);
            if (mutation != null) {
                sites.add(mutation);
            }
        }
        return super.visitBinary(node, unused);
    }

    @Override
    public Void visitUnary(UnaryTree node, Void unused) {
        MutationSite mutation = unarySite(node);
        if (mutation != null) {
            sites.add(mutation);
        }
        return super.visitUnary(node, unused);
    }

    @Override
    public Void visitReturn(ReturnTree node, Void unused) {
        addNullReplacement(node.getExpression());
        return super.visitReturn(node, unused);
    }

    @Override
    public Void visitVariable(VariableTree node, Void unused) {
        if (isField(getCurrentPath())) {
            addScope(fieldScope(node));
        }
        addNullReplacement(node.getInitializer());
        return super.visitVariable(node, unused);
    }

    @Override
    public Void visitAssignment(AssignmentTree node, Void unused) {
        addNullReplacement(node.getExpression());
        return super.visitAssignment(node, unused);
    }

    private MutationSite binarySite(BinaryTree node, String original, String replacement) {
        long leftEnd = trees.getSourcePositions().getEndPosition(unit, node.getLeftOperand());
        long rightStart = trees.getSourcePositions().getStartPosition(unit, node.getRightOperand());
        if (leftEnd < 0 || rightStart < 0 || leftEnd > rightStart || rightStart > source.length()) {
            return null;
        }
        String between = source.substring((int) leftEnd, (int) rightStart);
        int offset = between.indexOf(original);
        if (offset < 0) {
            return null;
        }
        int start = (int) leftEnd + offset;
        return site(start, start + original.length(), original, replacement);
    }

    private void addNullReplacement(ExpressionTree expression) {
        if (expression == null) {
            return;
        }
        TreePath path = new TreePath(getCurrentPath(), expression);
        TypeMirror type = trees.getTypeMirror(path);
        if (!isReferenceType(type)) {
            return;
        }
        int start = (int) trees.getSourcePositions().getStartPosition(unit, expression);
        int end = (int) trees.getSourcePositions().getEndPosition(unit, expression);
        if (start < 0 || end < 0 || end <= start) {
            return;
        }
        String original = source.substring(start, end);
        if ("null".equals(original)) {
            return;
        }
        sites.add(site(start, end, original, "null"));
    }

    private boolean isReferenceType(TypeMirror type) {
        return type != null
                && type.getKind() != TypeKind.ERROR
                && type.getKind() != TypeKind.VOID
                && !type.getKind().isPrimitive();
    }

    private MutationSite unarySite(UnaryTree node) {
        return switch (node.getKind()) {
            case LOGICAL_COMPLEMENT -> removablePrefixSite(node, "!");
            case UNARY_MINUS -> isNumericUnary(node) ? removablePrefixSite(node, "-") : null;
            default -> null;
        };
    }

    private MutationSite removablePrefixSite(UnaryTree node, String operator) {
        int start = (int) trees.getSourcePositions().getStartPosition(unit, node);
        if (start < 0) {
            return null;
        }
        int operatorStart = skipWhitespace(start);
        if (!source.startsWith(operator, operatorStart)) {
            return null;
        }
        ScopeRef scope = currentScope();
        return new MutationSite(file, lineNumber(operatorStart), operatorStart, operatorStart + operator.length(),
                operator, "", "replace " + operator + " with removed " + operator,
                scope.id(), scope.kind(), scope.startLine(), scope.endLine());
    }

    private int skipWhitespace(int index) {
        int current = index;
        while (current < source.length() && Character.isWhitespace(source.charAt(current))) {
            current++;
        }
        return current;
    }

    private boolean isNumericUnary(UnaryTree node) {
        TypeMirror type = trees.getTypeMirror(getCurrentPath());
        return type != null
                && type.getKind().isPrimitive()
                && type.getKind() != TypeKind.BOOLEAN;
    }

    private String operatorText(Tree.Kind kind) {
        return switch (kind) {
            case PLUS -> "+";
            case MINUS -> "-";
            case MULTIPLY -> "*";
            case DIVIDE -> "/";
            case CONDITIONAL_AND -> "&&";
            case CONDITIONAL_OR -> "||";
            case EQUAL_TO -> "==";
            case NOT_EQUAL_TO -> "!=";
            case GREATER_THAN -> ">";
            case GREATER_THAN_EQUAL -> ">=";
            case LESS_THAN -> "<";
            case LESS_THAN_EQUAL -> "<=";
            default -> null;
        };
    }

    private String operatorReplacement(Tree.Kind kind) {
        return switch (kind) {
            case PLUS -> isNumericBinary(getCurrentPath()) ? "-" : null;
            case MINUS -> "+";
            case MULTIPLY -> "/";
            case DIVIDE -> "*";
            case CONDITIONAL_AND -> "||";
            case CONDITIONAL_OR -> "&&";
            case EQUAL_TO -> "!=";
            case NOT_EQUAL_TO -> "==";
            case GREATER_THAN -> ">=";
            case GREATER_THAN_EQUAL -> ">";
            case LESS_THAN -> "<=";
            case LESS_THAN_EQUAL -> "<";
            default -> null;
        };
    }

    private boolean isNumericBinary(TreePath path) {
        TypeMirror type = trees.getTypeMirror(path);
        return type != null && type.getKind().isPrimitive() && type.getKind() != TypeKind.BOOLEAN;
    }

    private MutationSite site(int start, int end, String original, String replacement) {
        ScopeRef scope = currentScope();
        return new MutationSite(file, lineNumber(start), start, end, original, replacement,
                "replace " + original + " with " + replacement,
                scope.id(), scope.kind(), scope.startLine(), scope.endLine());
    }

    private int lineNumber(int index) {
        int line = 1;
        for (int i = 0; i < index; i++) {
            if (source.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }

    private void addScope(MutationScope scope) {
        if (scopes.stream().noneMatch(existing -> existing.id().equals(scope.id()))) {
            scopes.add(scope);
        }
    }

    private MutationScope classScope(ClassTree node) {
        return scope(scopeId("class", node.getSimpleName().toString(), node),
                "class", node);
    }

    private MutationScope methodScope(MethodTree node) {
        String name = node.getName().contentEquals("<init>") ? "ctor" : node.getName().toString();
        String detail = name + "(" + node.getParameters().size() + ")";
        return scope(scopeId("method", detail, node), "method", node);
    }

    private MutationScope fieldScope(VariableTree node) {
        return scope(scopeId("field", node.getName().toString(), node), "field", node);
    }

    private MutationScope scope(String id, String kind, Tree node) {
        int start = (int) trees.getSourcePositions().getStartPosition(unit, node);
        int end = (int) trees.getSourcePositions().getEndPosition(unit, node);
        if (start < 0 || end <= start) {
            start = 0;
            end = source.length();
        }
        return new MutationScope(
                id,
                kind,
                lineNumber(start),
                lineNumber(Math.max(start, end - 1)),
                manifestSupport.hash(source.substring(start, end))
        );
    }

    private String scopeId(String kind, String detail, Tree node) {
        int start = (int) trees.getSourcePositions().getStartPosition(unit, node);
        List<String> names = new ArrayList<>(classNames);
        java.util.Collections.reverse(names);
        String prefix = String.join(".", names);
        if (!prefix.isBlank()) {
            prefix += "#";
        }
        return kind + ":" + prefix + detail + ":" + lineNumber(Math.max(start, 0));
    }

    private ScopeRef currentScope() {
        TreePath path = getCurrentPath();
        while (path != null) {
            Tree leaf = path.getLeaf();
            if (leaf instanceof MethodTree method) {
                MutationScope scope = methodScope(method);
                addScope(scope);
                return ScopeRef.from(scope);
            }
            if (leaf instanceof VariableTree variable && isField(path)) {
                MutationScope scope = fieldScope(variable);
                addScope(scope);
                return ScopeRef.from(scope);
            }
            if (leaf instanceof ClassTree type) {
                MutationScope scope = classScope(type);
                addScope(scope);
                return ScopeRef.from(scope);
            }
            path = path.getParentPath();
        }
        return new ScopeRef("file:" + file.getFileName(), "file", 1, lineNumber(source.length()));
    }

    private boolean isField(TreePath path) {
        TreePath parent = path.getParentPath();
        return parent != null && parent.getLeaf() instanceof ClassTree;
    }

    private record ScopeRef(String id, String kind, int startLine, int endLine) {
        private static ScopeRef from(MutationScope scope) {
            return new ScopeRef(scope.id(), scope.kind(), scope.startLine(), scope.endLine());
        }
    }
}
