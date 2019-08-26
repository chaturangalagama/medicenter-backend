package com.ilt.cms.report.ui.validator;


import java.util.regex.Pattern;

public class CommonValidator {

    private static final Pattern integerPattern = Pattern.compile("-?\\d+?");

    public boolean isEmpty(String value) {
        return !(value != null && !value.equals(""));
    }

    public static boolean isInteger(String s) {
        return s != null && integerPattern.matcher(s).matches();
    }

}
