package ast;

import parser.*;

public class VariableNode extends ExpressionNode {
    public final Token name;
    public VariableNode(Token name) { this.name = name; }

    @Override
    public String toString() {
        return name.lexeme;
    }
}