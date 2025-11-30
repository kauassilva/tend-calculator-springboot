package com.learning.tend_calculator.limit;

public final class LimitContext {

    private final Double point; // null if infinity
    private final boolean toPositiveInfinity;

    private LimitContext(Double point, boolean toPositiveInfinity) {
        this.point = point;
        this.toPositiveInfinity = toPositiveInfinity;
    }

    public static LimitContext at(String pointStr) {
        String pointStrLowerCase = pointStr.toLowerCase();

        if (pointStrLowerCase.equals("inf") || pointStrLowerCase.equals("+inf") || pointStrLowerCase.equals("infinity")) {
            return new LimitContext(null, true);
        }
        if (pointStrLowerCase.equals("-inf") || pointStrLowerCase.equals("-infinity")) {
            return new LimitContext(null, false);
        }

        return new LimitContext(Double.parseDouble(pointStr), false);
    }

    public boolean isInfinity() {
        return point == null;
    }

    public Double getPoint() {
        return point;
    }

    public boolean getToPositiveInfinity() {
        return toPositiveInfinity;
    }

}
