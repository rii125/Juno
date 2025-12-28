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
            statements.add(parseStatement());
        }

        consume(TokenType.RBRACE, "} が必要です");
        return new MainFunctionNode(new BlockNode(statements));
    }

    private StatementNode parseStatement() {
        // var / final
        if (match(TokenType.VAR) || match(TokenType.FINAL)) {
            boolean isFinal = previous().type == TokenType.FINAL;

            Token name = consume(TokenType.IDENTIFIER, "変数名が必要です");
            consume(TokenType.COLON, ": が必要です");

            TypeNode type = parseType();

            ExpressionNode value = null;
            if (match(TokenType.EQUAL)) {
                value = parseExpression();
            }

            consume(TokenType.SEMICOLON, "; が必要です");

            return new TypedVarDeclarationNode(isFinal, type, name, value);
        }
        // assignment: IDENTIFIER "=" expression ";"
        if (check(TokenType.IDENTIFIER)) {
            Token name = advance(); // IDENTIFIER を消費

            if (match(TokenType.EQUAL)) {
                ExpressionNode value = parseExpression();
                consume(TokenType.SEMICOLON, "; が必要です");
                return new AssignmentNode(name, value);
            }

            throw new RuntimeException("代入文が不正です → " + peek());
        }


        // print
        if (match(TokenType.PRINT)) {
            consume(TokenType.LPAREN, "( が必要です");
            ExpressionNode expr = parseExpression();
            consume(TokenType.RPAREN, ") が必要です");
            consume(TokenType.SEMICOLON, "; が必要です");
            return new StatementNode.Print(expr);
        }

        throw new RuntimeException("未知の文です: " + peek());
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

    private TypeNode parseType() {
        Token typeToken = consumeType();
        return new TypeNode(typeToken);
    }

    private Token consumeType() {
        if (match(TokenType.TYPE_INT)) return previous();
        if (match(TokenType.TYPE_STRING)) return previous();
        if (match(TokenType.TYPE_BOOL)) return previous();
        if (match(TokenType.TYPE_FLOAT)) return previous();
        if (match(TokenType.TYPE_ANY)) return previous();
        throw new RuntimeException("型が必要です");
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
        return tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }
}