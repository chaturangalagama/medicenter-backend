package com.ilt.cms.core.entity.appointment;

import com.ilt.cms.core.entity.calendar.CalendarDayOfYear;
import com.ilt.cms.core.entity.calendar.CalendarTimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

public class Holiday extends CalendarTimeSlot {

    private String displayName;

    private String description;

    public Holiday() {
    }

    public Holiday(String displayName, String description, LocalDate date, LocalTime start, LocalTime end) {
        super(new CalendarDayOfYear(date), start, end);
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
