package com.lippo.cms.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculations {

    public static final int SCALE = 2;
    public static final BigDecimal DIVISOR = new BigDecimal(100.0f);
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public static int calculatePercentage(int value, int percentage) {
        return new BigDecimal(value).multiply(new BigDecimal(percentage))
                .divide(DIVISOR, ROUNDING_MODE)
                .setScale(SCALE, ROUNDING_MODE).intValue();
    }

    public static void main(String[] args) {
        int percentage = calculatePercentage(350, 7);
        System.out.println(percentage);
        System.out.println((350 * 7) / 100.0f);
    }


    public static int multiplyWithHalfRoundUp(int leftValue, int rightValue) {
        return leftValue * rightValue;
    }
}
