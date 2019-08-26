package com.ilt.cms.core.entity.price.master;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class ClinicTimeConstraint implements Condition {

    private String clinicId;
    private Set<DayOfWeek> dayCheck;
    private List<TimeBetween> timeCheck;

    public ClinicTimeConstraint() {
    }

    public ClinicTimeConstraint(String clinicId, Set<DayOfWeek> dayCheck,
                                List<TimeBetween> timeCheck) {
        this.clinicId = clinicId;
        this.dayCheck = dayCheck;
        this.timeCheck = timeCheck;
    }

    @Override
    public boolean match(Object object) {
        if (clinicId.equals(object)) {
            return false;
        }
        LocalTime currentTime = LocalTime.now();
        boolean dayMatch = dayCheck.contains(LocalDate.now().getDayOfWeek());
        if (dayMatch) {
            for (TimeBetween timeBetween : timeCheck) {
                if (currentTime.isAfter(timeBetween.start) && currentTime.isBefore(timeBetween.end)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static class TimeBetween {
        private LocalTime start;
        private LocalTime end;

        public TimeBetween() {
        }

        public TimeBetween(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
        }
    }

    public String getClinicId() {
        return clinicId;
    }

    public Set<DayOfWeek> getDayCheck() {
        return dayCheck;
    }

    public List<TimeBetween> getTimeCheck() {
        return timeCheck;
    }

    @Override
    public String toString() {
        return "ClinicTimeConstraint{" +
                "clinicId='" + clinicId + '\'' +
                ", dayCheck=" + dayCheck +
                ", timeCheck=" + timeCheck +
                '}';
    }
}
