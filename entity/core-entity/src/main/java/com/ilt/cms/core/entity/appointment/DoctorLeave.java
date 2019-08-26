package com.ilt.cms.core.entity.appointment;

import com.ilt.cms.core.entity.calendar.CalendarDayOfYear;
import com.ilt.cms.core.entity.calendar.CalendarTimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorLeave extends CalendarTimeSlot {

    private String type;

    private String reason;

    public DoctorLeave() {
    }

    public DoctorLeave(String type, String reason, LocalDate date, LocalTime start, LocalTime end) {
        super(new CalendarDayOfYear(date), start, end);
        this.type = type;
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
