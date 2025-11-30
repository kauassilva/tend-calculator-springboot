package com.learning.tend_calculator.limit;

import java.util.List;
import java.util.StringJoiner;

public final class LimitResult {

    private final LimitType type;
    private final Double value; // apenas se FINITE
    private final String representation; // optional symbolic representation (e.g. "9/4")
    private final List<String> steps;

    public LimitResult(LimitType type, Double value, String representation, List<String> steps) {
        this.type = type;
        this.value = value;
        this.representation = representation;
        this.steps = steps;
    }

    public String format() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nResultado: ");

        switch (type) {
            case FINITE -> {
                if (representation != null) {
                    stringBuilder.append(representation).append(" ou ").append(value);
                } else if (value != null) {
                    stringBuilder.append(value);
                } else {
                    stringBuilder.append("Indeterminado");
                }
            }
            case POSITIVE_INFINITY -> stringBuilder.append("+∞");
            case NEGATIVE_INFINITY -> stringBuilder.append("-∞");
            case DOES_NOT_EXIST_OSCILLATORY -> stringBuilder.append("Não existe (oscilatório)");
            case DOES_NOT_EXIST_DIFFERENT_ONE_SIDED -> stringBuilder.append("Não existe (limites literais diferentes)");
            case UNDETERMINED -> stringBuilder.append("Indeterminado");
            case ERROR -> stringBuilder.append("Erro");
        }

        if (!steps.isEmpty()) {
            stringBuilder.append("\nAnálises:");
            StringJoiner joiner = new StringJoiner("\n - ");
            steps.forEach(joiner::add);
            stringBuilder.append("\n - ").append(joiner);
        }

        return stringBuilder.toString();
    }

    public static LimitResult finite(Double value, String representation, List<String> steps) {
        return new LimitResult(LimitType.FINITE, value, representation, steps);
    }

    public static LimitResult undetermined(List<String> steps) {
        return new LimitResult(LimitType.UNDETERMINED, null, null, steps);
    }

    public static LimitResult error(String message) {
        return new LimitResult(LimitType.ERROR, null, null, List.of(message));
    }

    public LimitType getType() {
        return type;
    }

    public Double getValue() {
        return value;
    }
}
