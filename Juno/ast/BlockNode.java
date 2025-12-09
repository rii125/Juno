package ast;

import java.util.List;

public class BlockNode extends Node{
    public final List<StatementNode> statements;
    public BlockNode(List<StatementNode> statements) {
        this.statements = statements;
    }
}
