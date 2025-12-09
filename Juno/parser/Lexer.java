package parser;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    public List<Token> tokenize(String source) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while (i < source.length()) {
            char c = source.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            if (Character.isDigit(c)) {
                StringBuilder num = new StringBuilder();
                while (i < source.length() && Character.isDigit(source.charAt(i))) {
                    num.append(source.charAt(i++));
                }
                tokens.add(new Token(TokenType.NUMBER, num.toString()));
                continue;
            }

            if (c == '"') {
                i++;
                StringBuilder str = new StringBuilder();
                while (i < source.length() && source.charAt(i) != '"') {
                    str.append(source.charAt(i++));
                }
                i++; // 終了の " を読み飛ばす
                tokens.add(new Token(TokenType.STRING, str.toString()));
                continue;
            }

            switch (c) {
                case '=' -> { tokens.add(new Token(TokenType.EQUAL, "=")); i++;}
                case ';' -> { tokens.add(new Token(TokenType.SEMICOLON, ";")); i++;}
                case ':' -> { tokens.add(new Token(TokenType.COLON, ":")); i++;}
                case '(' -> { tokens.add(new Token(TokenType.LPAREN, "(")); i++;}
                case ')' -> {tokens.add(new Token(TokenType.RPAREN, ")")); i++;}
                case '{' -> { tokens.add(new Token(TokenType.LBRACE, "{")); i++;}
                case '}' -> { tokens.add(new Token(TokenType.RBRACE, "}")); i++;}
                default -> {
                    if (Character.isLetter(c)) {
                        StringBuilder word = new StringBuilder();
                        while (i < source.length() && Character.isLetter(source.charAt(i))) {
                            word.append(source.charAt(i++));
                        }
                        TokenType type = keywordType(word.toString());
                        tokens.add(new Token(type, word.toString()));
                        continue;
                    }
                    throw new RuntimeException("未知の文字: " + c);
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private TokenType keywordType(String word) {
        return switch (word) {
            case "public" -> TokenType.PUBLIC;
            case "static" -> TokenType.STATIC;
            case "main"   -> TokenType.MAIN;
            case "var"    -> TokenType.VAR;
            case "print"  -> TokenType.PRINT;
            case "int"    -> TokenType.TYPE_INT;
            case "string" -> TokenType.TYPE_STRING;
            case "bool"   -> TokenType.TYPE_BOOL;
            case "float"  -> TokenType.TYPE_FLOAT;
            case "any"    -> TokenType.TYPE_ANY;
            case "true"   -> TokenType.TRUE;
            case "false"  -> TokenType.FALSE;
            default       -> TokenType.IDENTIFIER;
        };
    }
}
