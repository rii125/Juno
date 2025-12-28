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

            // ★★★ 数字（int / float）処理はここだけに統合 ★★★
            if (Character.isDigit(c)) {
                StringBuilder num = new StringBuilder();
                boolean hasDot = false;

                while (i < source.length()) {
                    char ch = source.charAt(i);

                    if (Character.isDigit(ch)) {
                        num.append(ch);
                        i++;
                    }
                    else if (ch == '.' && !hasDot) {
                        hasDot = true;
                        num.append(ch);
                        i++;
                    }
                    else {
                        break;
                    }
                }

                tokens.add(new Token(TokenType.NUMBER, num.toString()));
                continue;
            }

            // 文字列
            if (c == '"') {
                i++;
                StringBuilder str = new StringBuilder();
                while (i < source.length() && source.charAt(i) != '"') {
                    str.append(source.charAt(i++));
                }
                i++; // 終了の "
                tokens.add(new Token(TokenType.STRING, str.toString()));
                continue;
            }

            // 記号
            switch (c) {
                case '=' -> { tokens.add(new Token(TokenType.EQUAL, "=")); i++; continue; }
                case ';' -> { tokens.add(new Token(TokenType.SEMICOLON, ";")); i++; continue; }
                case ':' -> { tokens.add(new Token(TokenType.COLON, ":")); i++; continue; }
                case '(' -> { tokens.add(new Token(TokenType.LPAREN, "(")); i++; continue; }
                case ')' -> { tokens.add(new Token(TokenType.RPAREN, ")")); i++; continue; }
                case '{' -> { tokens.add(new Token(TokenType.LBRACE, "{")); i++; continue; }
                case '}' -> { tokens.add(new Token(TokenType.RBRACE, "}")); i++; continue; }
            }

            // ★ キーワード / 識別子
            if (Character.isLetter(c)) {
                StringBuilder word = new StringBuilder();
                while (i < source.length() && Character.isLetter(source.charAt(i))) {
                    word.append(source.charAt(i++));
                }
                TokenType type = keywordType(word.toString());
                tokens.add(new Token(type, word.toString()));
                continue;
            }

            // どれにも当てはまらない
            throw new RuntimeException("未知の文字: " + c);
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
            case "final"  -> TokenType.FINAL;
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