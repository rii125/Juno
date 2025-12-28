package ast;

import parser.Token;

public class AssignmentNode extends StatementNode {
    public final Token name;
    public final ExpressionNode value;

    public AssignmentNode(Token name, ExpressionNode value) {
        super(Kind.ASSIGNMENT);
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Assign " + name.lexeme + " = " + value;
    }
}