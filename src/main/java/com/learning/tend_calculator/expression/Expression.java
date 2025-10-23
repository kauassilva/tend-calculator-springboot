package com.learning.tend_calculator.expression;

public interface Expression {
    Double evaluate(Double x);
    Boolean isConstant();
    String asString();
}
