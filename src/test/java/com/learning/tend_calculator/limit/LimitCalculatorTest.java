package com.learning.tend_calculator.limit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest("cli.enabled=false")
class LimitCalculatorTest {

    @Autowired
    LimitCalculator calculator;

    @Test
    void testCase1() {
        LimitResult r = calculator.computeLimit("x^2", LimitContext.at("3"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(9, r.getValue());
    }

    @Test
    void testCase2() {
        LimitResult r = calculator.computeLimit("2 * x - 1", LimitContext.at("3"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(5, r.getValue());
    }

    @Test
    void testCase3() {
        LimitResult r = calculator.computeLimit("(x^2 - 1) / (x - 1)", LimitContext.at("1"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(2, r.getValue());
    }

    @Test
    void testCase4() {
        LimitResult r = calculator.computeLimit("3 * x + 7", LimitContext.at("-2"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(1, r.getValue());
    }

    @Test
    void testCase5() {
        LimitResult r = calculator.computeLimit("3 * 9 + 2 * 4", LimitContext.at("3"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(35, r.getValue());
    }

    @Test
    void testCase6() {
        LimitResult r = calculator.computeLimit("9 / 4", LimitContext.at("3"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(2.25, r.getValue());
    }

    @Test
    void testCase7() {
        LimitResult r = calculator.computeLimit("4 * x^2 + 5 * x - 7", LimitContext.at("2"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(19, r.getValue());
    }

    @Test
    void testCase8() {
        LimitResult r = calculator.computeLimit("x^3 + 4 * x^2 - 3", LimitContext.at("-2"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(5, r.getValue());
    }

    @Test
    void testCase9() {
        LimitResult r = calculator.computeLimit("(x^4 + x^2 - 1) / (x^2 + 5)", LimitContext.at("2"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(2.111111111111111, r.getValue());
    }

    @Test
    void testCase10() {
        LimitResult r = calculator.computeLimit("(-x^3 + 2 * x) / (x - 1)", LimitContext.at("3"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(-10.5, r.getValue());
    }

    @Test
    void testCase11() {
        LimitResult r = calculator.computeLimit("Sqrt(9*4)", LimitContext.at("3"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(6, r.getValue());
    }

    @Test
    void testCase12() {
        LimitResult r = calculator.computeLimit("CubeRoot((x^2 + 5*x + 3) / (x^2 - 1))", LimitContext.at("3"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(1.5, r.getValue());
    }

    @Test
    void testCase13() {
        LimitResult r = calculator.computeLimit("(x^2 - 4) / (x - 2)", LimitContext.at("2"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase14() {
        LimitResult r = calculator.computeLimit("x^2 * sin(1/x)", LimitContext.at("0"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase15() {
        LimitResult r = calculator.computeLimit("(x^2 + 3*x - 10) / (x + 5)", LimitContext.at("-5"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase16() {
        LimitResult r = calculator.computeLimit("((2 + x)^2 - 4) / x", LimitContext.at("0"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase17() {
        LimitResult r = calculator.computeLimit("(Sqrt(2 + x) - Sqrt(2)) / x", LimitContext.at("0"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase18() {
        LimitResult r = calculator.computeLimit("(Sqrt(x^2 + 8) - 3) / (x - 1)", LimitContext.at("1"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase19() {
        LimitResult r = calculator.computeLimit("(x - 7) / (x^2 - 49)", LimitContext.at("7"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase20() {
        LimitResult r = calculator.computeLimit("((3 + x)^2 - 9) / x", LimitContext.at("0"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase21() {
        LimitResult r = calculator.computeLimit("(x - 5) / (x^2 - 25)", LimitContext.at("5"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase22() {
        LimitResult r = calculator.computeLimit("(x^3 - 1) / (x-1)", LimitContext.at("1"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase23() {
        LimitResult r = calculator.computeLimit("(Sqrt(1 - 2 * x - x^2) - 1) / x", LimitContext.at("0"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase24() {
        LimitResult r = calculator.computeLimit("(x^2 - 4 * x + 3) / (x^3 - 1)", LimitContext.at("1"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }

    @Test
    void testCase25() {
        LimitResult r = calculator.computeLimit("((x - 5)^2 - 25) / x", LimitContext.at("0"));
        assertEquals(LimitType.FINITE, r.getType());
        assertEquals(4, r.getValue());
    }



}