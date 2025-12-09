package ast;

import parser.*;

public class TypeNode extends Node{
    public final Token baseType;
    public TypeNode(Token baseType, String s) {
        this.baseType = baseType;
    }

    @Override
    public String toString() {
        return baseType.lexeme;
    }
}
