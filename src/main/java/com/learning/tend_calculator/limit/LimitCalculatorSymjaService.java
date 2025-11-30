package com.learning.tend_calculator.limit;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(name = "symja.enabled", havingValue = "true", matchIfMissing = true)
public class LimitCalculatorSymjaService implements LimitCalculatorService {

    private final ExprEvaluator evaluator = new ExprEvaluator();

    @Override
    public LimitResult computeLimit(String expressionStr, LimitContext context) {
        List<String> steps = new ArrayList<>();
        steps.add("Using Symja engine");

        try {
            String pointSpec = buildPointSpec(context);
            String expr = "Limit(" + expressionStr + ", " + pointSpec + ")";
            steps.add("Symja expression: " + expr);

            IExpr res = evaluator.eval(expr);

            if (res == null) {
                return LimitResult.undetermined(steps);
            }

            String r = res.toString();

            // try to parse numeric result
            try {
                double val = Double.parseDouble(r);
                return LimitResult.finite(val, steps);
            } catch (NumberFormatException ex) {
                // Not a direct number. Try Simplify or N
                IExpr simplified = evaluator.eval("Simplify(" + expr + ")");
                String sr = simplified.toString();
                try {
                    double val = Double.parseDouble(sr);
                    return LimitResult.finite(val, steps);
                } catch (NumberFormatException ex2) {
                    // If still not numeric, return symbolic/undetermined
                    steps.add("Symja result: " + sr);
                    return LimitResult.undetermined(steps);
                }
            }

        } catch (Exception e) {
            return LimitResult.error("Symja evaluation falhou: " + e.getMessage());
        }
    }

    private String buildPointSpec(LimitContext context) {
        if (context.isInfinity()) {
            // Symja may interpret Infinity as "Infinity"; use +Infinity or -Infinity
            return context.getToPositiveInfinity() ? "x->Infinity" : "x->-Infinity";
        } else {
            Double point = context.getPoint();
            // Use plain double formatting (no locale)
            if (point == null) return "x->0";
            if (point.longValue() == point) {
                return "x->" + point.longValue();
            }
            return "x->" + point;
        }
    }
}

