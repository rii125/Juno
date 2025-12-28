package ast;

import parser.Token;

public class TypeNode extends Node {
    public final Token baseType;

    public TypeNode(Token baseType) {
        this.baseType = baseType;
    }

    @Override
    public String toString() {
        return baseType.lexeme;
    }
}