package ast;

import parser.*;

public class TypedVarDeclarationNode extends StatementNode{
    public final TypeNode type;
    public final Token name;
    public final ExpressionNode value;

    public TypedVarDeclarationNode(TypeNode type, Token name, ExpressionNode value) {
        super(Kind.VAR_DECL);
        this.type = type;
        this.name = name;
        this.value = value;
    }
}
