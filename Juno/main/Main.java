package main;

import parser.*;
import ast.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws Exception {
        String source = Files.readString(Paths.get("test.jn"));

//        out.println("1 Connect!");

        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenize(source);

//        out.println("2 Connect!");

        Parser parser = new Parser(tokens);
        MainFunctionNode ast = parser.parse();

//        out.println("3 Connect!");

        printAst(ast, 0);
    }

    private static void printAst(Object node, int i) {
        String pad = " ".repeat(i);

        if (node instanceof MainFunctionNode main) {
            out.println(pad + "MainFunction {");
            for (StatementNode stmt : main.block.statements) {
                printAst(stmt, i + 2);
            }
            out.println(pad + "}");
        } else if (node instanceof ast.TypedVarDeclarationNode varDecl) {
            out.println(pad + "VarDecl " + varDecl.name.lexeme
                    + ": " + varDecl.type + " = " + varDecl.value);
        } else if (node instanceof StatementNode.Print printStmt) {
            out.println(pad + "Print(" + printStmt.expr + ")");
        } else if (node instanceof ast.LiteralNode lit) {
            out.println(pad + "Literal(" + lit.token.lexeme + ")");
        } else if (node instanceof ast.VariableNode var) {
            out.println(pad + "Variable(" + var.name.lexeme + ")");
        } else {
            out.println(pad + node);
        }
    }
}

