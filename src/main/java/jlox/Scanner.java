package jlox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scanner
{
    private final String m_source;
    private final List<Token> m_tokens = new ArrayList<>();
    private int m_line = 1;
    private String m_lexeme = "";

    private int m_start;
    private int m_current;

    public Scanner (String source)
    {
        m_source = source;
    }

    private boolean eof_reached ()
    {
        return m_current >= m_source.length();
    }

    private char advance ()
    {
        return m_source.charAt(m_current++);
    }

    private void add_token (TokenType type)
    {
        m_tokens.add(new Token(type, "", null, m_line));
    }

    private void add_token (TokenType type, Object literal)
    {
        String lexeme = m_source.substring(m_start, m_current);
        m_tokens.add(new Token(type, lexeme, literal, m_line));
    }

    private boolean match (char ch)
    {
        if (eof_reached() || m_source.charAt(m_current) == ch)
            return false;
        m_current++;
        return true;
    }

    private char peek ()
    {
        if (eof_reached()) return '\0';
        return m_source.charAt(m_current);
    }

    private char peek_next ()
    {
        if (m_current + 1 >= m_source.length()) return '\0';
        return m_source.charAt(m_current + 1);
    }

    private void string ()
    {
        char next;
        while ((next = advance()) != '"')
        {
            if (eof_reached())
            {
                Lox.error(m_line, "Unexpected end of string!");
                return;
            }

            if (next == '\n') m_line++;
        }
    }

    private void number ()
    {
        while (is_digit(peek())) advance();

        if (peek() == '.' && is_digit(peek_next()))
        {
            advance();
            while (is_digit(peek())) advance();
        }
    }

    private boolean is_digit (char ch)
    {
        return ch >= '0' && ch <= '9';
    }

    private void scan_token ()
    {
        char next = advance();
        switch (next)
        {
            // Simple Tokens.
            case '{' -> add_token(TokenType.LEFT_BRACE);
            case '}' -> add_token(TokenType.RIGHT_BRACE);
            case '(' -> add_token(TokenType.LEFT_PAREN);
            case ')' -> add_token(TokenType.RIGHT_PAREN);
            case ',' -> add_token(TokenType.COMMA);
            case ';' -> add_token(TokenType.SEMICOLON);
            case '+' -> add_token(TokenType.PLUS);
            case '-' -> add_token(TokenType.MINUS);
            case '*' -> add_token(TokenType.STAR);
            case '.' -> add_token(TokenType.DOT);

            // Ambiguous Tokens.
            case '!' -> add_token(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
            case '>' -> add_token(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
            case '<' -> add_token(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
            case '=' -> add_token(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);

            // Comment.
            case '/' -> {
                if (match('/'))
                    while (!eof_reached() && peek() != '\n') advance();
                else add_token(TokenType.SLASH);
            }

            // Whitespaces.
            case ' ', '\t', '\r' -> {
            }
            case '\n' -> m_line++;

            // String literals.
            case '"' -> {
                string();
                var string = m_source.substring(m_start + 1, m_current - 1);
                add_token(TokenType.STRING, string);
            }
            default -> {
                if (is_digit(next))
                {
                    number();
                    var number = Double.parseDouble(m_source.substring(m_start, m_current));
                    add_token(TokenType.NUMBER, number);
                }
                else Lox.error(m_line, String.format("Unexpected character: `%c`!", next));
            }
        }
    }

    public List<Token> scan ()
    {
        while (!eof_reached())
        {
            m_start = m_current;
            scan_token();
        }
        return m_tokens;
    }
}
