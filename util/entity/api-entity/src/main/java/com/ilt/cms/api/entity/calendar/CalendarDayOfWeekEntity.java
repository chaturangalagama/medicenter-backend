package com.ilt.cms.api.entity.calendar;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CalendarDayOfWeekEntity{


    private DayOfWeek dayOfWeek;

}
