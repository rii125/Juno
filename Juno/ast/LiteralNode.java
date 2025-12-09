package ast;

import parser.*;

public class LiteralNode extends ExpressionNode{
    public final Token token;
    public LiteralNode(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return token.lexeme;
    }
}
