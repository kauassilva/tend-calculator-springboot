package com.learning.tend_calculator.limit;

public final class LimitContext {

    public enum Direction {TWO_SIDED, LEFT, RIGHT}

    private final Double point; // null if infinity
    private final boolean toPositiveInfinity;
    private final boolean toNegativeInfinity;
    private final Direction direction;
    private final Double epsilon;
    private final Integer maxInteration;

    private LimitContext(Double point,
                        boolean toPositiveInfinity,
                        boolean toNegativeInfinity,
                        Direction direction,
                        Double epsilon,
                        Integer maxInteration) {
        this.point = point;
        this.toPositiveInfinity = toPositiveInfinity;
        this.toNegativeInfinity = toNegativeInfinity;
        this.direction = direction;
        this.epsilon = epsilon;
        this.maxInteration = maxInteration;
    }

    public static LimitContext at(String pointStr) {
        String pointStrLowerCase = pointStr.toLowerCase();

        if (pointStrLowerCase.equals("inf") || pointStrLowerCase.equals("+inf") || pointStrLowerCase.equals("infinity")) {
            return new LimitContext(null, true, false, Direction.TWO_SIDED, 1e-6, 40);
        }
        if (pointStrLowerCase.equals("-inf") || pointStrLowerCase.equals("-infinity")) {
            return new LimitContext(null, false, true, Direction.TWO_SIDED, 1e-6, 40);
        }

        return new LimitContext(Double.parseDouble(pointStr), false, false, Direction.TWO_SIDED, 1e-6, 40);
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

    public boolean getToNegativeInfinity() {
        return toNegativeInfinity;
    }

    public Direction getDirection() {
        return direction;
    }

    public Double getEpsilon() {
        return epsilon;
    }

    public Integer getMaxInteration() {
        return maxInteration;
    }
}
