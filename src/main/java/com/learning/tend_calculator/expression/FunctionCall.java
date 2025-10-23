package com.learning.tend_calculator.expression;

import java.util.List;

public class FunctionCall implements Expression {
    private final String name;
    private final List<Expression> args;

    public FunctionCall(String name, List<Expression> args) {
        if (args == null || args.size() != 1)
            throw new IllegalArgumentException("Função suporta 1 argumento");

        this.name = name;
        this.args = args;
    }

    @Override
    public Double evaluate(Double x) {
        Double value = args.getFirst().evaluate(x);

        return switch (name) {
            case "sen" -> Math.sin(value);
            case "cos" -> Math.cos(value);
            case "tg" -> Math.tan(value);
            case "exp" -> Math.exp(value);
            case "log" -> Math.log(value);
            case "abs" -> Math.abs(value);
            default -> Double.NaN;
        };
    }

    @Override
    public Boolean isConstant() {
        return args.stream().allMatch(Expression::isConstant);
    }

    @Override
    public String asString() {
        return name + "(" + args.getFirst().asString() + ")";
    }
}
