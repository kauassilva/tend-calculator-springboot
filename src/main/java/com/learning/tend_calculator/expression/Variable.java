package com.learning.tend_calculator.expression;

public class Variable implements Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public Double evaluate(Double x) {
        return x;
    }

    @Override
    public Boolean isConstant() {
        return false;
    }

    @Override
    public String asString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
