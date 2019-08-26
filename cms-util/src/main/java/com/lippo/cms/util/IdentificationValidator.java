package com.lippo.cms.util;

public class IdentificationValidator {

    private static final String CHECK_REGEX = "^[STFG]\\d{7}[A-Z]$";

    public static void main(String[] args) {
        System.out.println(isNRICValid("S8422100G"));
        System.out.println(isFINValid("G0567956M"));
    }

    private static int[] MULTIPLES = {2, 7, 6, 5, 4, 3, 2};

    public static boolean isNRICValid(String nricStr) {

        if (!nricStr.matches(CHECK_REGEX)) {
            return false;
        }
        char[] nricArray = nricStr.toCharArray();
        char firstChar = nricArray[0];
        char lastChar = nricArray[nricStr.length() - 1];

        if (firstChar != 'S' && firstChar != 'T') {
            return false;
        }

        int numericNric = Integer.parseInt(nricStr.substring(1, nricStr.length() - 1));
        int total = 0;
        int count = 0;
        while (numericNric != 0) {
            total += numericNric % 10 * MULTIPLES[MULTIPLES.length - (1 + count++)];
            numericNric /= 10;
        }

        char[] outputs;
        if (firstChar == 'S') {
            outputs = new char[]{'J', 'Z', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A'};
        } else {
            outputs = new char[]{'G', 'F', 'E', 'D', 'C', 'B', 'A', 'J', 'Z', 'I', 'H'};
        }
        return lastChar == outputs[total % 11];
    }


    public static boolean isFINValid(String finStr) {

        if (!finStr.matches(CHECK_REGEX)) {
            return false;
        }
        int total = 0;
        int count = 0;

        char[] fin = finStr.toCharArray();
        char first = fin[0];
        char last = fin[fin.length - 1];

        if (first != 'F' && first != 'G') {
            return false;
        }

        int numericNric = Integer.parseInt(finStr.substring(1, finStr.length() - 1));
        while (numericNric != 0) {
            total += numericNric % 10 * MULTIPLES[MULTIPLES.length - (1 + count++)];
            numericNric /= 10;
        }

        char[] outputs;
        if (first == 'F') {
            outputs = new char[]{'X', 'W', 'U', 'T', 'R', 'Q', 'P', 'N', 'M', 'L', 'K'};
        } else {
            outputs = new char[]{'R', 'Q', 'P', 'N', 'M', 'L', 'K', 'X', 'W', 'U', 'T'};
        }
        return last == outputs[total % 11];
    }

}

