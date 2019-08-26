package com.ilt.cms.report.ui.util;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Formatter {

    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    
    public static List<String> asList(String commaSeparateValue) {
        if (commaSeparateValue == null || commaSeparateValue.length() == 0 || "null".equals(commaSeparateValue)) {
            return new ArrayList<>();
        }
        return Stream.of(commaSeparateValue.replaceAll("\"", "").split(",")).map(String::trim).collect(Collectors.toList());
    }

    public static Float currencyDecimalFormat(int value) {
        return Float.valueOf(decimalFormat.format((float) value / 100));
    }

    public static Date dateConvert(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date dateConvert(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
