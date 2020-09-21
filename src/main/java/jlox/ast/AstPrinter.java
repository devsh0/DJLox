package jlox.ast;

public class AstPrinter implements Visitor<String>
{
    private String parenthesize (String name, Expression... expressions)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (var expression : expressions)
        {
            builder.append(" ");
            builder.append(expression.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }

    public String print (Expression expression)
    {
        return expression.accept(this);
    }

    @Override
    public String visit_literal_expresion (LiteralExpression expression)
    {
        String value = expression.value == null ? "nil" : expression.value + "";
        return parenthesize(value);
    }

    @Override
    public String visit_grouping_expression (GroupingExpression expression)
    {
        return parenthesize("group", expression.expression);
    }

    @Override
    public String visit_binary_expression (BinaryExpression expression)
    {
        return parenthesize(expression.operator.lexeme(), expression.lhs, expression.rhs);
    }

    @Override
    public String visit_unary_expression (UnaryExpression expression)
    {
        return parenthesize(expression.operator.lexeme(), expression.operand);
    }
}
