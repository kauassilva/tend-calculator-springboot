package com.learning.tend_calculator.limit;

import java.util.List;
import java.util.StringJoiner;

public final class LimitResult {

    private final LimitType type;
    private final Double value; // apenas se FINITE
    private final List<String> steps;

    public LimitResult(LimitType type, Double value, List<String> steps) {
        this.type = type;
        this.value = value;
        this.steps = steps;
    }

    public String format() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Resultado: ");

        switch (type) {
            case FINITE -> stringBuilder.append(value);
            case POSITIVE_INFINITY -> stringBuilder.append("+∞");
            case NEGATIVE_INFINITY -> stringBuilder.append("-∞");
            case DOES_NOT_EXIST_OSCILLATORY -> stringBuilder.append("Não existe (oscilatório)");
            case DOES_NOT_EXIST_DIFFERENT_ONE_SIDED -> stringBuilder.append("Não existe (limites literais diferentes)");
            case UNDETERMINED -> stringBuilder.append("Indeterminado");
            case ERROR -> stringBuilder.append("Erro");
        }

        if (!steps.isEmpty()) {
            stringBuilder.append("\nPassos:");
            StringJoiner joiner = new StringJoiner("\n - ");
            steps.forEach(joiner::add);
            stringBuilder.append("\n - ").append(joiner);
        }

        return stringBuilder.toString();
    }

    public static LimitResult finite(double value, List<String> steps) {
        return new LimitResult(LimitType.FINITE, value, steps);
    }

    public static LimitResult positiveInfinite(List<String> steps) {
        return new LimitResult(LimitType.POSITIVE_INFINITY, null, steps);
    }

    public static LimitResult negativeInfinite(List<String> steps) {
        return new LimitResult(LimitType.NEGATIVE_INFINITY, null, steps);
    }

    public static LimitResult oscillatory(List<String> steps) {
        return new LimitResult(LimitType.DOES_NOT_EXIST_OSCILLATORY, null, steps);
    }

    public static LimitResult differentSides(List<String> steps) {
        return new LimitResult(LimitType.DOES_NOT_EXIST_DIFFERENT_ONE_SIDED, null, steps);
    }

    public static LimitResult undetermined(List<String> steps) {
        return new LimitResult(LimitType.UNDETERMINED, null, steps);
    }

    public static LimitResult error(String message) {
        return new LimitResult(LimitType.ERROR, null, List.of(message));
    }

    public LimitType getType() {
        return type;
    }

    public Double getValue() {
        return value;
    }

    public List<String> getSteps() {
        return steps;
    }
}
