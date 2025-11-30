package com.learning.tend_calculator.util;

public final class LimitUtil {

    private LimitUtil() {}

    public static boolean isInfinity(String s) {
        return s.equalsIgnoreCase("inf")
                || s.equalsIgnoreCase("+inf")
                || s.equalsIgnoreCase("-inf")
                || s.equalsIgnoreCase("infinity")
                || s.equalsIgnoreCase("-infinity");
    }

    public static Double parsePoint(String s) {
        if (isInfinity(s))
            return null;

        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

}
