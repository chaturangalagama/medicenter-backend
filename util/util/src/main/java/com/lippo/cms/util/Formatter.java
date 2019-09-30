package com.lippo.cms.util;

import java.text.DecimalFormat;

/*
  By the discussion on slack, it's been sending the currency values as int from BE to FE without formatting.
  In other hand, currencies sending from FE to BE as int values. So the FE has to format the currency values
  by dividing the int values by 100 when they displaying in UI.
 */
@Deprecated
public class Formatter {

    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static String currencyViewFormat(int value) {
        return decimalFormat.format((double) value / 100);
    }

    public static Float currencyViewDecimalFormat(int value) {
        return Float.valueOf(decimalFormat.format((float) value / 100));
    }

    public static int currencySaveFormat(String value) {
        try {
            if (value == null) {
                return 0;
            }
            return (int) (Double.parseDouble(value) * 100);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
