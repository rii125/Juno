package ast;

public class MainFunctionNode extends Node{
    public final BlockNode block;

    public MainFunctionNode(BlockNode block) {
        this.block = block;
    }
}
