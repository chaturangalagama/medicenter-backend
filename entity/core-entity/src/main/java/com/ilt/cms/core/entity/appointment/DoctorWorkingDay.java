package com.ilt.cms.core.entity.appointment;

import com.ilt.cms.core.entity.calendar.CalendarDayOfWeek;
import com.ilt.cms.core.entity.calendar.CalendarTimeSlot;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DoctorWorkingDay extends CalendarTimeSlot {

    public DoctorWorkingDay() {
    }

    public DoctorWorkingDay(DayOfWeek dayOfWeek, LocalTime start, LocalTime end) {
        super(new CalendarDayOfWeek(dayOfWeek), start, end);
    }
}
