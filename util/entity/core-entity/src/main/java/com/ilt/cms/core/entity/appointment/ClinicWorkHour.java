package com.ilt.cms.core.entity.appointment;

import com.ilt.cms.core.entity.calendar.CalendarDayOfWeek;
import com.ilt.cms.core.entity.calendar.CalendarTimeSlot;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class ClinicWorkHour extends CalendarTimeSlot {

    public ClinicWorkHour() {
    }

    public ClinicWorkHour(DayOfWeek dayOfWeek, LocalTime start, LocalTime end) {
        super(new CalendarDayOfWeek(dayOfWeek), start, end);
    }
}