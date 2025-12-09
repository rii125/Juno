package parser;

import ast.*;

import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public MainFunctionNode parse() {
        consume(TokenType.PUBLIC, "public が必要です");
        consume(TokenType.STATIC, "static が必要です");
        consume(TokenType.MAIN, "main が必要です");
        consume(TokenType.LPAREN, "( が必要です");
        consume(TokenType.RPAREN, ") が必要です");
        consume(TokenType.LBRACE, "{ が必要です");

        List<StatementNode> statements = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            statements.add(parseDeclaration());
        }

        consume(TokenType.RBRACE, "} が必要です");
        return new MainFunctionNode(new BlockNode(statements));
    }

    private StatementNode parseDeclaration() {
        if (match(TokenType.VAR)) {
            Token name = consume(TokenType.IDENTIFIER, "変数名が必要です");
            consume(TokenType.COLON, ": が必要です");
            Token typeToken = advance();
            TypeNode type = new TypeNode(typeToken, null);
            consume(TokenType.EQUAL, "= が必要です");
            ExpressionNode value = parseExpression(); // ← 修正
            consume(TokenType.SEMICOLON, "; が必要です");
            return new TypedVarDeclarationNode(type, name, value);
        }

        if (match(TokenType.PRINT)) {
            consume(TokenType.LPAREN, "( が必要です");
            ExpressionNode expr = parseExpression();
            consume(TokenType.RPAREN, ") が必要です");
            consume(TokenType.SEMICOLON, "; が必要です");
            return new StatementNode.Print(expr);
        }
        throw new RuntimeException("[構文エラー] 文が不正です → " + peek());
    }

    private ExpressionNode parseExpression() {
        if (match(TokenType.NUMBER, TokenType.STRING, TokenType.TRUE, TokenType.FALSE)) {
            return new LiteralNode(previous());
        }
        if (match(TokenType.IDENTIFIER)) {
            return new VariableNode(previous());
        }
        throw new RuntimeException("[構文エラー] 式が不正です → " + peek());
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException("[構文エラー] " + message + " → " + peek());
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1); // ← 修正
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }
}
