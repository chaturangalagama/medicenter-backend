package com.ilt.cms.core.entity.calendar;

import com.ilt.cms.core.entity.PersistedObject;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CompoundIndexes({
        @CompoundIndex(name = "index_unique", unique = true, def = "{'doctorId' : 1, 'clinicId' : 1}")
})
public class DoctorCalendar extends PersistedObject {

    private String doctorId;
    private String clinicId;
    private List<CalendarTimeSlot> workingDays = new ArrayList<>();
    private List<CalendarTimeSlot> leaves = new ArrayList<>();
    private List<CalendarTimeSlot> blockedTime = new ArrayList<>();


    public boolean isDoctorAvailable(LocalDateTime range) {
        return workingDays.stream()
                .anyMatch(calendarTimeSlot -> calendarTimeSlot.withinRange(range))
                && leaves.stream()
                .noneMatch(calendarTimeSlot -> calendarTimeSlot.withinRange(range))
                && blockedTime.stream()
                .noneMatch(calendarTimeSlot -> calendarTimeSlot.withinRange(range));
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public List<CalendarTimeSlot> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(List<CalendarTimeSlot> workingDays) {
        this.workingDays = workingDays;
    }

    public List<CalendarTimeSlot> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<CalendarTimeSlot> leaves) {
        this.leaves = leaves;
    }

    public List<CalendarTimeSlot> getBlockedTime() {
        return blockedTime;
    }

    public void setBlockedTime(List<CalendarTimeSlot> blockedTime) {
        this.blockedTime = blockedTime;
    }

    @Override
    public String toString() {
        return "DoctorCalendar{" +
                "doctorId='" + doctorId + '\'' +
                ", clinicId='" + clinicId + '\'' +
                ", workingDays=" + workingDays +
                ", leaves=" + leaves +
                ", blockedTime=" + blockedTime +
                '}';
    }
}
