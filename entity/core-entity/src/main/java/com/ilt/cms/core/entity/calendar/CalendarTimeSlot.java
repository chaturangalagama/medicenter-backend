package com.ilt.cms.core.entity.calendar;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CalendarTimeSlot {

    private CalendarDay calendarDay;
    private LocalTime start;
    private LocalTime end;

    public CalendarTimeSlot() {
    }

    public CalendarTimeSlot(CalendarDay calendarDay, LocalTime start, LocalTime end) {
        this.calendarDay = calendarDay;
        this.start = start;
        this.end = end;
    }

    public boolean withinRange(LocalDateTime range) {
        LocalTime localTime = range.toLocalTime();
        return calendarDay.isDayMatches(range.toLocalDate())
                && localTime.isBefore(end)
                && localTime.isAfter(start);
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public CalendarDay getCalendarDay() {
        return calendarDay;
    }


    @Override
    public String toString() {
        return "CalendarTimeSlot{" +
                "calendarDay=" + calendarDay +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
