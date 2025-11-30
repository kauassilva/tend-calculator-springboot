package com.learning.tend_calculator.limit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LimitCalculatorServiceImplTest {
    private final LimitCalculatorServiceImpl service = new LimitCalculatorServiceImpl();

    static Stream<Arguments> limitTestCases() {
        return Stream.of(
                // Expressão        | Ponto        | Resultado Esperado
                Arguments.of("x^2", "2", 4.0),
                Arguments.of("2 * x - 1", "3", 5.0),
                Arguments.of("(x^2 - 1) / (x - 1)", "1", 2.0),
                Arguments.of("3 * x + 7", "-2", 1.0),
                Arguments.of("3 * 9 + 2 * 4", "3", 35.0),
                Arguments.of("9 / 4", "3", 2.25),
                Arguments.of("4 * x^2 + 5 * x - 7", "2", 19.0),
                Arguments.of("x^3 + 4 * x^2 - 3", "-2", 5.0),
                Arguments.of("(x^4 + x^2 - 1) / (x^2 + 5)", "2", 2.111111111111111),
                Arguments.of("(-x^3 + 2 * x) / (x - 1)", "3", -10.5),
                Arguments.of("Sqrt(9*4)", "3", 6.0),
                Arguments.of("CubeRoot((x^2 + 5*x + 3) / (x^2 - 1))", "3", 1.5),
                Arguments.of("(x^2 - 4) / (x - 2)", "2", 4.0),
                Arguments.of("x^2 * sin(1/x)", "0", 0.0),
                Arguments.of("(x^2 + 3*x - 10) / (x + 5)", "-5", -7.0),
                Arguments.of("((2 + x)^2 - 4) / x", "0", 4.0),
                Arguments.of("(Sqrt(2 + x) - Sqrt(2)) / x", "0", 0.353553),
                Arguments.of("(Sqrt(x^2 + 8) - 3) / (x - 1)", "1", 0.3333333333333333),
                Arguments.of("(x - 7) / (x^2 - 49)", "7", 0.07142857142857142),
                Arguments.of("((3 + x)^2 - 9) / x", "0", 6.0),
                Arguments.of("(x - 5) / (x^2 - 25)", "5", 0.1),
                Arguments.of("(x^3 - 1) / (x-1)", "1", 3.0),
                Arguments.of("(Sqrt(1 - 2 * x - x^2) - 1) / x", "0", -1.0),
                Arguments.of("(x^2 - 4 * x + 3) / (x^3 - 1)", "1", -0.6666666666666666),
                Arguments.of("((x - 5)^2 - 25) / x", "0", -10.0)
        );
    }

    @ParameterizedTest(name = "Limite de {0}, quando x -> {1}, deve ser {2}")
    @MethodSource("limitTestCases")
    void computeLimits(String expression, String pointStr, Double expectedValue) {
        LimitResult result = service.computeLimit(expression, LimitContext.at(pointStr));
        assertEquals(expectedValue, result.getValue());
    }

}