package com.learning.tend_calculator.limit;

import com.learning.tend_calculator.parse.ExpressionParser;
import com.learning.tend_calculator.expression.Expression;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(name = "symja.enabled", havingValue = "true")
public class LimitCalculatorSymjaService implements LimitCalculatorService {

    private final ExprEvaluator evaluator = new ExprEvaluator();
    private final ExpressionParser parser = new ExpressionParser();

    @Override
    public LimitResult computeLimit(String expressionStr, LimitContext context) {
        List<String> steps = new ArrayList<>();

        // First try direct substitution using existing parser (to detect indeterminate)
        boolean directTried = false;
        if (!context.isInfinity()) {
            directTried = true;
            try {
                Expression expression = parser.parse(expressionStr);
                Double val = expression.evaluate(context.getPoint());
                if (val != null && !Double.isNaN(val) && !Double.isInfinite(val)) {
                    // direct substitution succeeded -> return it
                    steps.add("Parser OK");
                    steps.add("Substituição direta bem sucedida");
                    return LimitResult.finite(val, steps);
                } else {
                    // mark that direct substitution was indeterminate and proceed to Symja
                    steps.add("Substituição direta indeterminada");
                }
            } catch (Exception e) {
                // parsing/evaluation failed -> mark as indeterminate
                steps.add("Substituição direta indeterminada");
            }
        }

        // Use Symja for symbolic evaluation
        steps.add("Using Symja engine");
        try {
            String pointSpec = buildPointSpec(context);
            String expr = "Limit(" + expressionStr + ", " + pointSpec + ")";
            steps.add("Symja expression: " + expr);

            IExpr res = evaluator.eval(expr);

            if (res == null) {
                return LimitResult.undetermined(steps);
            }

            String r = res.toString().trim();

            // Always include Symja result in steps
            steps.add("Symja result: " + r);

            // Try to interpret Symja result as numeric
            Double numeric = tryParseNumeric(r);
            if (numeric != null) {
                return LimitResult.finite(numeric, r, steps);
            }

            // Try Simplify(...) or N(...) to get numeric (but still return Symja representation if possible)
            try {
                IExpr simplified = evaluator.eval("Simplify(" + expr + ")");
                String sr = simplified.toString().trim();
                Double numeric2 = tryParseNumeric(sr);
                if (numeric2 != null) {
                    steps.add("Symja result (simplified): " + sr);
                    return LimitResult.finite(numeric2, sr, steps);
                }
            } catch (Exception ignored) {
            }

            try {
                IExpr n = evaluator.eval("N(" + expr + ")");
                String nr = n.toString().trim();
                Double numeric3 = tryParseNumeric(nr);
                if (numeric3 != null) {
                    steps.add("Symja result (numeric): " + nr);
                    return LimitResult.finite(numeric3, nr, steps);
                }
            } catch (Exception ignored) {
            }

            // If not numeric, still return representation from Symja as the result (user requested to show Symja result)
            return LimitResult.finite(null, r, steps);

        } catch (Exception e) {
            return LimitResult.error("Symja evaluation falhou: " + e.getMessage());
        }
    }

    private String buildPointSpec(LimitContext context) {
        if (context.isInfinity()) {
            return context.getToPositiveInfinity() ? "x->Infinity" : "x->-Infinity";
        } else {
            Double point = context.getPoint();
            if (point == null) return "x->0";
            if (point.longValue() == point.doubleValue()) {
                return "x->" + point.longValue();
            }
            return "x->" + point;
        }
    }

    private Double tryParseNumeric(String s) {
        if (s == null) return null;
        s = s.trim();
        // try plain double
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ignored) {
        }

        // try fraction like a/b or (a/b)
        String frac = s;
        if (frac.startsWith("(") && frac.endsWith(")")) {
            frac = frac.substring(1, frac.length() - 1).trim();
        }

        // Accept formats like -?\d+\s*/\s*\d+
        if (frac.matches("-?\\d+\\s*/\\s*\\d+")) {
            String[] parts = frac.split("/");
            try {
                double num = Double.parseDouble(parts[0].trim());
                double den = Double.parseDouble(parts[1].trim());
                return num / den;
            } catch (NumberFormatException ignored) {
            }
        }

        // try to evaluate simple rational with Symja again by calling N on the string
        try {
            IExpr n = evaluator.eval("N(" + s + ")");
            String nr = n.toString().trim();
            return Double.parseDouble(nr);
        } catch (Exception ignored) {
        }

        return null;
    }
}
