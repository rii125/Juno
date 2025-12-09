package ast;

import java.util.HashMap;
import java.util.Map;


public class TypeChecker {
    private final Map<String, String> variables = new HashMap<>();
    public void check(StatementNode stmt) {
        if (stmt instanceof TypedVarDeclarationNode varDecl) {
            String expected = varDecl.type.toString();
            String actual = inferType(varDecl.value);
            if (!isAssignable(expected, actual)) {
                throw new RuntimeException("型エラー: " + expected + " に " + actual + " は代入できません");
            }
            variables.put(varDecl.name.lexeme, expected);
        }
        if (stmt.kind == StatementNode.Kind.PRINT) {
            StatementNode.Print printStmt = (StatementNode.Print) stmt;
            String actual = inferType(printStmt.expr);
            System.out.println("print文: " + actual + " 型の値を出力");
        }
    }

    public String inferType(ExpressionNode expr) {
        if (expr instanceof LiteralNode lit) {
            switch (lit.token.type) {
                case NUMBER -> { return "int"; }
                case STRING -> { return "string"; }
                case TRUE, FALSE -> { return "bool"; }
            }
        }
        if (expr instanceof VariableNode var) {
            return variables.getOrDefault(var.name.lexeme, "unknown");
        }
        return "unknown";
    }
    private boolean isAssignable(String expected, String actual) {
        if (expected.equals(actual)) return true;
        if (expected.equals("any")) return true;
        return false;
    }
}
