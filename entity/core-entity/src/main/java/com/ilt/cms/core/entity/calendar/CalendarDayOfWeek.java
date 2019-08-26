package com.ilt.cms.core.entity.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class CalendarDayOfWeek implements CalendarDay {

    private DayOfWeek dayOfWeek;

    public CalendarDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public boolean isDayMatches(LocalDate localDate) {
        return localDate.getDayOfWeek() == dayOfWeek;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public String toString() {
        return "CalendarDayOfWeek{" +
                "dayOfWeek=" + dayOfWeek +
                '}';
    }
}
