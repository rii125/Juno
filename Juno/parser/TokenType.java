package parser;

public enum TokenType {
    // Keyword
    PUBLIC,
    STATIC,
    VOID,
    MAIN,
    VAR,
    PRINT,
    // Type
    TYPE_INT,
    TYPE_FLOAT,
    TYPE_STRING,
    TYPE_BOOL,
    TYPE_ANY,
    TYPE_LIST,
    // Literal
    NUMBER,
    STRING,
    TRUE,
    FALSE,
    // Symbol
    EQUAL, // =
    SEMICOLON, // ;
    COLON, // :
    LPAREN, // (
    RPAREN, // )
    RBRACE, // {
    LBRACE, // }
    LT, // <
    RT, // >
    // Identifier,termination
    IDENTIFIER,
    EOF,
}
