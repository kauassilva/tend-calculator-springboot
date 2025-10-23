package com.learning.tend_calculator.expression;

public final class BinaryOperator implements Expression {
    private final Expression left;
    private final Expression right;
    private final Operator operator;

    public BinaryOperator(Expression left, Expression right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public Double evaluate(Double x) {
        Double a = left.evaluate(x);
        Double b = right.evaluate(x);

        return switch (operator) {
            case ADD -> a + b;
            case SUBTRACT -> a - b;
            case MULTIPLY -> a * b;
            case DIVIDE -> b == 0 ? Double.NaN : a /b;
            case POWER -> Math.pow(a, b);
        };
    }

    @Override
    public Boolean isConstant() {
        return left.isConstant() && right.isConstant();
    }

    @Override
    public String asString() {
        return "(" + left.asString() + operatorSymbol() + right.asString() + ")";
    }

    private String operatorSymbol() {
        return switch (operator) {
            case ADD -> "+";
            case SUBTRACT -> "-";
            case MULTIPLY -> "*";
            case DIVIDE -> "/";
            case POWER -> "^";
        };
    }
}
