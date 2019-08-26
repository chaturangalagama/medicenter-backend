package com.ilt.cms.core.entity.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalendarDayOfYear implements CalendarDay {

    private LocalDate date;

    public CalendarDayOfYear(LocalDate date) {
        this.date = date;
    }

    public CalendarDayOfYear() {
    }

    @Override
    public boolean isDayMatches(LocalDate localDate) {
        return this.date.equals(localDate);
    }

    public boolean withInRange(LocalDate startDate, LocalDate endDate) {
        return this.date.isAfter(startDate) && this.date.isBefore(endDate);
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "CalendarDayOfYear{" +
                "date=" + date +
                '}';
    }
}
