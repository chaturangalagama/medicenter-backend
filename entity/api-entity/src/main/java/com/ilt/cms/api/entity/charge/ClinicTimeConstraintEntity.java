package com.ilt.cms.api.entity.charge;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ClinicTimeConstraintEntity implements Condition{


    private String clinicId;
    private Set<DayOfWeek> dayCheck;
    private List<TimeBetween> timeCheck;
    @Override
    public boolean match(Object object) {
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class TimeBetween {
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
        @DateTimeFormat(pattern = "HH:mm:ss")
        private LocalTime start;
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
        @DateTimeFormat(pattern = "HH:mm:ss")
        private LocalTime end;
    }
}
