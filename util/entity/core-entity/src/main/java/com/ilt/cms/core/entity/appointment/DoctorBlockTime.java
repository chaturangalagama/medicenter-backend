package com.ilt.cms.core.entity.appointment;

import com.ilt.cms.core.entity.calendar.CalendarDayOfWeek;
import com.ilt.cms.core.entity.calendar.CalendarDayOfYear;
import com.ilt.cms.core.entity.calendar.CalendarTimeSlot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorBlockTime extends CalendarTimeSlot {

    private String reason;

    public DoctorBlockTime() {
    }

    public DoctorBlockTime(LocalDate date, LocalTime start, LocalTime end) {
        super(new CalendarDayOfYear(date), start, end);
        this.reason = reason;
    }

    public DoctorBlockTime(DayOfWeek dayOfWeek , LocalTime start, LocalTime end){
        super(new CalendarDayOfWeek(dayOfWeek), start, end);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
