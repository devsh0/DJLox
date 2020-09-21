package jlox;

import jlox.ast.*;
import org.junit.jupiter.api.Test;

class LoxTest {
    @Test
    void test_main ()
    {
        Lox.main("main.lox");
    }

    @Test
    void test_silly_ast_printer ()
    {
        var tok_minus = new Token(TokenType.MINUS, "-", null, 1);
        var unary = new UnaryExpression(new LiteralExpression(123), tok_minus);
        var group = new GroupingExpression(new LiteralExpression(45.67));
        var tok_star = new Token(TokenType.STAR, "*", null, 1);
        Expression binary = new BinaryExpression(unary, group, tok_star);
        var printer = new AstPrinter();
        System.out.println(printer.print(binary));
    }
}