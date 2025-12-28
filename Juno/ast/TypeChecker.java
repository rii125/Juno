package ast;

import parser.TokenType;

import java.util.HashMap;
import java.util.Map;

public class TypeChecker {

    private final Map<String, String> variables = new HashMap<>();

    public void check(BlockNode block) {
        for (StatementNode stmt : block.statements) {
            checkStatement(stmt);
        }
    }

    private void checkStatement(StatementNode stmt) {
        if (stmt instanceof TypedVarDeclarationNode varDecl) {
            String expected = varDecl.type.toString();

            if (varDecl.isFinal) {
                if (varDecl.value == null) {
                    throw new RuntimeException("final 変数 " + varDecl.name.lexeme + " は初期化が必要です");
                }

                if (varDecl.value instanceof LiteralNode lit &&
                        lit.token.type == TokenType.STRING &&
                        lit.token.lexeme.equals("")) {

                    throw new RuntimeException("final 変数 " + varDecl.name.lexeme + " に空文字は設定できません");
                }
            }


            if (!varDecl.isFinal && varDecl.value == null) {
                variables.put(varDecl.name.lexeme, expected);
                return;
            }

            String actual = inferType(varDecl.value);
            if (!isAssignable(expected, actual)) {
                throw new RuntimeException(
                        "型エラー: " + expected + " に " + actual + " は代入できません"
                );
            }

            variables.put(varDecl.name.lexeme, expected);
            return;
        }

        if (stmt instanceof AssignmentNode assign) {
            String expected = variables.get(assign.name.lexeme);
            if (expected == null) {
                throw new RuntimeException("未宣言の変数です: " + assign.name.lexeme);
            }

            String actual = inferType(assign.value);
            if (!isAssignable(expected, actual)) {
                throw new RuntimeException(
                        "型エラー: " + expected + " に " + actual + " は代入できません"
                );
            }

            return;
        }

        if (stmt instanceof StatementNode.Print printStmt) {
            String actual = inferType(printStmt.expr);
            System.out.println("print文: " + actual + " 型の値を出力");
            return;
        }
    }

    private String inferType(ExpressionNode expr) {
        if (expr instanceof LiteralNode lit) {
            return switch (lit.token.type) {
                case NUMBER -> {
                    if (lit.token.lexeme.contains(".")) yield "float";
                    else yield "int";
                }
                case STRING -> "string";
                case TRUE, FALSE -> "bool";
                default -> "unknown";
            };
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
