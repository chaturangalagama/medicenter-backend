package com.ilt.cms.core.entity.calendar;

import com.ilt.cms.core.entity.PersistedObject;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClinicCalendar extends PersistedObject {

    @Indexed(unique = true)
    private String clinicId;
    private int avgConsultationTime;
    private int maxWaitTime;
    private List<CalendarTimeSlot> operationHours = new ArrayList<>();
    private List<CalendarTimeSlot> clinicHolidays = new ArrayList<>();


    public boolean isWithinClinicHours(LocalDateTime range) {
        return operationHours.stream()
                .anyMatch(calendarTimeSlot -> calendarTimeSlot.withinRange(range))
                && clinicHolidays.stream()
                .noneMatch(calendarTimeSlot -> calendarTimeSlot.withinRange(range));
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public void setOperationHours(List<CalendarTimeSlot> operationHours) {
        this.operationHours = operationHours;
    }

    public void setClinicHolidays(List<CalendarTimeSlot> clinicHolidays) {
        this.clinicHolidays = clinicHolidays;
    }

    public String getClinicId() {
        return clinicId;
    }

    public int getAvgConsultationTime() {
        return avgConsultationTime;
    }

    public void setAvgConsultationTime(int avgConsultationTime) {
        this.avgConsultationTime = avgConsultationTime;
    }

    public int getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(int maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public List<CalendarTimeSlot> getOperationHours() {
        return operationHours;
    }

    public List<CalendarTimeSlot> getClinicHolidays() {
        return clinicHolidays;
    }

    @Override
    public String toString() {
        return "ClinicCalendar{" +
                "clinicId='" + clinicId + '\'' +
                ", avgConsultationTime=" + avgConsultationTime +
                ", maxWaitTime=" + maxWaitTime +
                ", operationHours=" + operationHours +
                ", clinicHolidays=" + clinicHolidays +
                ", id='" + id + '\'' +
                '}';
    }
}
