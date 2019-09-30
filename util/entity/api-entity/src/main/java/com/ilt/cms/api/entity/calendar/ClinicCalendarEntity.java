package com.ilt.cms.api.entity.calendar;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClinicCalendarEntity {

    private String id;
    private String clinicId;
    private int avgConsultationTime;
    private int maxWaitTime;
    private List<CalendarTimeSlotEntity> operationHours = new ArrayList<>();
    private List<CalendarTimeSlotEntity> clinicHolidays = new ArrayList<>();

}
