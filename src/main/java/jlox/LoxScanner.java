package jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoxScanner
{
    private final String m_source;
    private final List<Token> m_tokens = new ArrayList<>();
    private static final Map<String, TokenType> m_keywords = new HashMap<>();
    private int m_line = 1;
    private String m_lexeme = "";

    private int m_start;
    private int m_current;

    public LoxScanner (String source)
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

    static {
        m_keywords.put("and",    TokenType.AND);
        m_keywords.put("class",  TokenType.CLASS);
        m_keywords.put("else",   TokenType.ELSE);
        m_keywords.put("false",  TokenType.FALSE);
        m_keywords.put("for",    TokenType.FOR);
        m_keywords.put("fun",    TokenType.FUN);
        m_keywords.put("if",     TokenType.IF);
        m_keywords.put("nil",    TokenType.NIL);
        m_keywords.put("or",     TokenType.OR);
        m_keywords.put("print",  TokenType.PRINT);
        m_keywords.put("return", TokenType.RETURN);
        m_keywords.put("super",  TokenType.SUPER);
        m_keywords.put("this",   TokenType.THIS);
        m_keywords.put("true",   TokenType.TRUE);
        m_keywords.put("var",    TokenType.VAR);
        m_keywords.put("while",  TokenType.WHILE);
    }

    private Map<String, TokenType> keywords ()
    {
        return m_keywords;
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

    private void identifier ()
    {
        while (is_aphanum(peek()))
            advance();
    }

    private boolean is_digit (char ch)
    {
        return ch >= '0' && ch <= '9';
    }

    private boolean is_alpha (char ch)
    {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_';
    }

    private boolean is_aphanum (char ch)
    {
        return is_alpha(ch) || is_digit(ch);
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
            case ' ', '\t', '\r' -> {}
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
                } else if (is_alpha(next))
                {
                    identifier();
                    var name = m_source.substring(m_start, m_current);
                    var token_type = keywords().get(name);
                    token_type = token_type == null ? TokenType.IDENTIFIER : token_type;
                    add_token(token_type, name);
                } else Lox.error(m_line, String.format("Unexpected character: `%c`!", next));
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
