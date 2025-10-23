package com.learning.tend_calculator.limit;

import com.learning.tend_calculator.expression.Expression;
import com.learning.tend_calculator.parse.ExpressionParser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LimitCalculator {

    private final ExpressionParser parser = new ExpressionParser();

    public LimitResult computeLimit(String expressionStr, LimitContext context) {
        List<String> steps = new ArrayList<>();
        Expression expression;

        try {
            expression = parser.parse(expressionStr);
            steps.add("Parser OK");
        } catch (Exception e) {
            return LimitResult.error("Parsing falhou: " + e.getMessage());
        }

        if (context.isInfinity())
            return limitAtInfinity(expression, context, steps);
        else
            return limitAtFinite(expression, context, steps);
    }

    private LimitResult limitAtFinite(Expression expression, LimitContext context, List<String> steps) {
        Double a = context.getPoint();
        Double direct = expression.evaluate(a);

        if (!Double.isNaN(direct) && !Double.isInfinite(direct)) {
            steps.add("Substituição direta bem sucedida");
            return LimitResult.finite(direct, steps);
        } else {
            steps.add("Substituição direta indeterminada, inciando aproximação numérica");
        }

        double prevLeft = Double.NaN, prevRight = Double.NaN;
        double leftVal = Double.NaN, rightVal = Double.NaN;
        double delta = 1e-1;
        int stableCount = 0;

        for (int i = 0; i <context.getMaxInteration(); i++) {
            double xLeft = a - delta;
            double xRight = a + delta;
            leftVal = expression.evaluate(xLeft);
            rightVal = expression.evaluate(xRight);

            if (Double.isNaN(leftVal) || Double.isNaN(rightVal)) {
                delta /= 2;
                continue;
            }
            if (Double.isInfinite(leftVal) && Double.isInfinite(rightVal)) {
                return leftVal > 0 ? LimitResult.positiveInfinite(steps) : LimitResult.negativeInfinite(steps);
            }
            if (!Double.isNaN(prevLeft) && !Double.isNaN(prevRight)) {
                double diffLeft = Math.abs(leftVal - prevLeft);
                double diffRight = Math.abs(rightVal - prevRight);
                double diffSides = Math.abs(leftVal - rightVal);

                if (diffSides < context.getEpsilon() && diffLeft < context.getEpsilon() && diffRight < context.getEpsilon()) {
                    stableCount++;
                } else {
                    stableCount = 0;
                }
                if (stableCount >= 3) {
                    double result = (leftVal + rightVal) / 2.0;
                    steps.add("Convergência numérica alcançada com delta=" + delta);
                    return LimitResult.finite(result, steps);
                }
                if (i > 5 && diffSides > 1 && Math.signum(leftVal) != Math.signum(rightVal)) {
                    steps.add("Valores laterais divergem em sinal após iterações");
                    return LimitResult.differentSides(steps);
                }
            }

            prevLeft = leftVal;
            prevRight = rightVal;
            delta /= 2.0;
        }

        // verificar oscilacao simples
        if (Double.isNaN(leftVal) || Double.isNaN(rightVal))
            return LimitResult.undetermined(steps);
        if (Math.abs(leftVal - rightVal) > 1e-2) {
            steps.add("Não houve convergência bilateral");
            return LimitResult.differentSides(steps);
        }

        double avg = (leftVal + rightVal) / 2.0;
        return LimitResult.finite(avg, steps);
    }

    private LimitResult limitAtInfinity(Expression expression, LimitContext context, List<String> steps) {
        steps.add("Aproximação numérica para infinito");
        double x = context.getToPositiveInfinity() ? 10 : -10;
        double prev = Double.NaN;

        for (int i = 0; i <context.getMaxInteration(); i++) {
            Double val = expression.evaluate(x);

            if (Double.isNaN(val)) {
                x = context.getToPositiveInfinity() ? x * 2 : x * 2;
                continue;
            }
            if (Double.isInfinite(val)) {
                return val > 0 ? LimitResult.positiveInfinite(steps) : LimitResult.negativeInfinite(steps);
            }
            if (!Double.isNaN(prev)) {
                double diff = Math.abs(val - prev);

                if (diff < context.getEpsilon()) {
                    steps.add("Convergência detectada em x=" + x);
                    return LimitResult.finite(val, steps);
                }
                if (Math.abs(val) > 1e6 && Math.abs(prev) > 1e6) {
                    return val > 0 ? LimitResult.positiveInfinite(steps) : LimitResult.negativeInfinite(steps);
                }
            }

            prev = val;
            x = context.getToPositiveInfinity() ? x * 2 : x * 2; // dobra magnitude
        }

        return LimitResult.undetermined(steps);
    }

}
