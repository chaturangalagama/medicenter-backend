package com.lippo.commons.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommonUtils {

    private static final int INITIAL_VALUE = 100_000;
    private static final int MAX_VALUE = 900_000;

    private static final AtomicLong idGeneratorCounter = new AtomicLong(INITIAL_VALUE);
    private static final AtomicLong idTransactionCounter = new AtomicLong(INITIAL_VALUE);

    //system automatically sets a unique prefix for ID generator
    private static final long MOST_SIGNIFICANT_BITS = Math.abs(UUID.randomUUID().getMostSignificantBits());


    public static boolean isStringValid(String s) {
        return s != null && !s.isEmpty();
    }

    public static boolean isStringValid(String... strings) {
        return Arrays.stream(strings).allMatch(CommonUtils::isStringValid);
    }

    /**
     * Generates a ID and keeps uniqueness across multiple JVM
     *
     * @return a globally unique number
     */
    public static String idGenerator() {
        if (idGeneratorCounter.get() >= MAX_VALUE) {
            idGeneratorCounter.set(INITIAL_VALUE);
        }
        return MOST_SIGNIFICANT_BITS + String.valueOf(System.currentTimeMillis()) + idGeneratorCounter.getAndIncrement();
    }

    public static String transactionId() {
        if (idTransactionCounter.get() >= MAX_VALUE) {
            idTransactionCounter.set(INITIAL_VALUE);
        }
        return MOST_SIGNIFICANT_BITS + System.currentTimeMillis() + String.valueOf(idTransactionCounter.getAndIncrement());
    }

    public static double round(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.HALF_DOWN).doubleValue();
    }
}
