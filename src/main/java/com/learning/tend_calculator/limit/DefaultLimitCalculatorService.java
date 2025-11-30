package com.learning.tend_calculator.limit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "symja.enabled", havingValue = "false")
public class DefaultLimitCalculatorService implements LimitCalculatorService {

    private final LimitCalculator delegate = new LimitCalculator();

    @Override
    public LimitResult computeLimit(String expressionStr, LimitContext context) {
        return delegate.computeLimit(expressionStr, context);
    }
}

