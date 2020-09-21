package jlox.ast;

public interface Visitor<T>
{
    T visit_literal_expresion (LiteralExpression expression);
    T visit_grouping_expression (GroupingExpression expression);
    T visit_binary_expression (BinaryExpression expression);
    T visit_unary_expression (UnaryExpression expression);
}
