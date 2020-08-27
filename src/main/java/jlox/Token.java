package jlox;

public class Token {
    final TokenType m_type;
    final String m_lexeme;
    final Object m_literal;
    final int m_line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        m_type = type;
        m_lexeme = lexeme;
        m_literal = literal;
        m_line = line;
    }

    public String toString() {
        return m_type + " " + m_lexeme + " " + m_literal;
    }
}
