package com.learning.tend_calculator.limit;

public interface LimitCalculatorService {
    LimitResult computeLimit(String expressionStr, LimitContext context);
}

