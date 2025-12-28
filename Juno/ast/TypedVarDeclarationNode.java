package ast;

import parser.*;

public class TypedVarDeclarationNode extends StatementNode {
    public final boolean isFinal;
    public final TypeNode type;
    public final Token name;
    public final ExpressionNode value;

    public TypedVarDeclarationNode(boolean isFinal, TypeNode type, Token name, ExpressionNode value) {
        super(Kind.VAR_DECL);
        this.isFinal = isFinal;
        this.type = type;
        this.name = name;
        this.value = value;
    }
}
