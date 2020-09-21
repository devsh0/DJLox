package jlox;

public class Token
{
    private final TokenType m_type;
    private final String m_lexeme;
    private final Object m_literal;
    private final int m_line;

    Token (TokenType type, String lexeme, Object literal, int line)
    {
        m_type = type;
        m_lexeme = lexeme;
        m_literal = literal;
        m_line = line;
    }

    public TokenType type ()
    {
        return m_type;
    }

    public String lexeme ()
    {
        return m_lexeme;
    }

    public Object literal ()
    {
        return m_literal;
    }

    public int line ()
    {
        return m_line;
    }

    public String toString ()
    {
        String format = "{" + "TokenType: %s, Lexeme: %s, Literal: %s}";
        return String.format(format, m_type, m_lexeme, m_literal);
    }
}
