package com.learning.tend_calculator.expression;

public record Constant(double value) implements Expression {
    @Override
    public Double evaluate(Double x) {
        return value;
    }

    @Override
    public Boolean isConstant() {
        return true;
    }

    @Override
    public String asString() {
        return Double.toString(value);
    }
}
