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
        LimitResult directResult = tryDirectSubstitution(expression, a, steps);

        if (directResult != null) {
            return directResult;
        }

        steps.add("Substituição direta indeterminada, inciando aproximação numérica");
        return approximateByLateralLimits(expression, a, context, steps);
    }

    private LimitResult approximateByLateralLimits(Expression expression, Double point, LimitContext context, List<String> steps) {
        LateralApproximation approx = new LateralApproximation();
        double delta = 1e-1;

        for (int i = 0; i <context.getMaxInteration(); i++) {
            double leftVal = expression.evaluate(point - delta);
            double rightVal = expression.evaluate(point + delta);

            if (hasNaNValues(leftVal, rightVal)) {
                delta /= 2;
                continue;
            }

            LimitResult infinityResult = checkBothSidesInfinite(leftVal, rightVal, steps);
            if (infinityResult != null) {
                return infinityResult;
            }

            if (approx.hasPreviousValues()) {
                LimitResult convergenceResult = checkLateralConvergence(leftVal, rightVal, approx, delta, context, i, steps);

                if (convergenceResult != null) {
                    return convergenceResult;
                }
            }

            approx.update(leftVal, rightVal);
            delta /= 2.0;
        }

        return handleNonConvergence(approx.getLeftVal(), approx.getRightVal(), steps);
    }

    private LimitResult handleNonConvergence(double leftVal, double rightVal, List<String> steps) {
        if (Double.isNaN(leftVal) || Double.isNaN(rightVal)) {
            return LimitResult.undetermined(steps);
        }
        if (Math.abs(leftVal - rightVal) > 1e-2) {
            steps.add("Não houve convergência bilateral");
            return LimitResult.differentSides(steps);
        }

        double avg = (leftVal + rightVal) / 2.0;
        return LimitResult.finite(avg, steps);
    }

    private LimitResult checkLateralConvergence(double leftVal, double rightVal, LateralApproximation approx, double delta, LimitContext context, int iteration, List<String> steps) {
        double diffLeft = Math.abs(leftVal - approx.getPrevLeft());
        double diffRight = Math.abs(rightVal - approx.getPrevRight());
        double diffSides = Math.abs(leftVal - rightVal);

        if (allDifferencesAreSmall(diffLeft, diffRight, diffSides, context.getEpsilon())) {
            approx.incrementStableCount();
        } else {
            approx.resetStableCount();
        }

        if (approx.isStable()) {
            double result = (leftVal + rightVal) / 2.0;
            steps.add("Convergência numérica alcançada com delta=" + delta);
            return LimitResult.finite(result, steps);
        }
        if (sidesHaveDivergedInSign(iteration, diffSides, leftVal, rightVal)) {
            steps.add("Valores laterais divergem em sinal após iterações");
            return LimitResult.differentSides(steps);
        }

        return null;
    }

    private boolean sidesHaveDivergedInSign(int iteration, double diffSides, double leftVal, double rightVal) {
        return iteration > 5 && diffSides > 1 && Math.signum(leftVal) != Math.signum(rightVal);
    }

    private boolean allDifferencesAreSmall(double diffLeft, double diffRight, double diffSides, Double epsilon) {
        return diffSides < epsilon && diffLeft < epsilon && diffRight < epsilon;
    }

    private LimitResult checkBothSidesInfinite(double leftVal, double rightVal, List<String> steps) {
        if (Double.isInfinite(leftVal) && Double.isInfinite(rightVal)) {
            return leftVal > 0 ? LimitResult.positiveInfinite(steps) : LimitResult.negativeInfinite(steps);
        }
        return null;
    }

    private boolean hasNaNValues(double leftVal, double rightVal) {
        return Double.isNaN(leftVal) && Double.isNaN(rightVal);
    }

    private LimitResult tryDirectSubstitution(Expression expression, Double a, List<String> steps) {
        Double direct = expression.evaluate(a);

        if (!Double.isNaN(direct) && !Double.isInfinite(direct)) {
            steps.add("Substituição direta bem sucedida");
            return LimitResult.finite(direct, steps);
        }

        return null;
    }

    private LimitResult limitAtInfinity(Expression expression, LimitContext context, List<String> steps) {
        steps.add("Aproximação numérica para infinito");
        double x = getInitialValueForInfinity(context);
        double prev = Double.NaN;

        for (int i = 0; i <context.getMaxInteration(); i++) {
            Double val = expression.evaluate(x);

            if (Double.isNaN(val)) {
                x = doubleXMagnitude(x);
                continue;
            }

            LimitResult infinityResult = checkIfInfinity(val, steps);

            if (infinityResult != null) {
                return infinityResult;
            }
            if (!Double.isNaN(prev)) {
                LimitResult convergenceResult = checkConvergence(val, prev, x, context, steps);

                if (convergenceResult != null) {
                    return convergenceResult;
                }
            }

            prev = val;
            x = doubleXMagnitude(x);
        }

        return LimitResult.undetermined(steps);
    }

    private LimitResult checkConvergence(Double currentVal, double prevVal, double x, LimitContext context, List<String> steps) {
        double diff = Math.abs(currentVal - prevVal);

        if (hasConverged(diff, context.getEpsilon())) {
            steps.add("Convergência detectada em x=" + x);
            return LimitResult.finite(currentVal, steps);
        }
        if (isTendingToInfinity(currentVal, prevVal)) {
            return currentVal > 0 ? LimitResult.positiveInfinite(steps) : LimitResult.negativeInfinite(steps);
        }

        return null;
    }

    private boolean isTendingToInfinity(Double currentVal, double prevVal) {
        return Math.abs(currentVal) > 1e6 && Math.abs(prevVal) > 1e6;
    }

    private boolean hasConverged(double difference, Double epsilon) {
        return difference < epsilon;
    }

    private LimitResult checkIfInfinity(Double value, List<String> steps) {
        if (Double.isInfinite(value)) {
            return value > 0 ? LimitResult.positiveInfinite(steps) : LimitResult.negativeInfinite(steps);
        }

        return null;
    }

    private double doubleXMagnitude(double x) {
        return x * 2;
    }

    private double getInitialValueForInfinity(LimitContext context) {
        return context.getToPositiveInfinity() ? 10 : -10;
    }

    private static class LateralApproximation {
        private double prevLeft = Double.NaN;
        private double prevRight = Double.NaN;
        private double leftVal = Double.NaN;
        private double rightVal = Double.NaN;
        private int stableCount = 0;

        boolean hasPreviousValues() {
            return !Double.isNaN(prevLeft) && !Double.isNaN(prevRight);
        }

        void update(double left, double right) {
            this.prevLeft = this.leftVal;
            this.prevRight = this.rightVal;
            this.leftVal = left;
            this.rightVal = right;
        }

        void incrementStableCount() {
            stableCount++;
        }

        void resetStableCount() {
            stableCount = 0;
        }

        boolean isStable() {
            return stableCount >= 3;
        }

        public double getPrevLeft() {
            return prevLeft;
        }

        public double getPrevRight() {
            return prevRight;
        }

        public double getLeftVal() {
            return leftVal;
        }

        public double getRightVal() {
            return rightVal;
        }

        public int getStableCount() {
            return stableCount;
        }
    }

}
