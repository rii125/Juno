package ast;

public class StatementNode extends Node{
    public enum Kind { VAR_DECL, PRINT,}
    public final Kind kind;

    protected StatementNode(Kind kind) {
        this.kind = kind;
    }

    public static class Print extends StatementNode {
        public final ExpressionNode expr;

        public Print(ExpressionNode expr) {
            super(Kind.PRINT);
            this.expr = expr;
        }
    }
}
