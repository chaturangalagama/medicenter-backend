package com.ilt.cms.api.entity.calendar;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CalendarTimeSlotEntity {

    private CalendarDayOfWeekEntity calendarDayWeek;
    private CalendarDayOfYearEntity calendarDayYear;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_TIME_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_TIME_FORMAT)
    private LocalTime start;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_TIME_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_TIME_FORMAT)
    private LocalTime end;

}
