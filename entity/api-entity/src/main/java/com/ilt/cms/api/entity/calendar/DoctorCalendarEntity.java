package com.ilt.cms.api.entity.calendar;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DoctorCalendarEntity {

    private String id;
    private String doctorId;
    private String clinicId;
    private List<CalendarTimeSlotEntity> workingDays = new ArrayList<>();
    private List<CalendarTimeSlotEntity> leaves = new ArrayList<>();
    private List<CalendarTimeSlotEntity> blockedTime = new ArrayList<>();

}
