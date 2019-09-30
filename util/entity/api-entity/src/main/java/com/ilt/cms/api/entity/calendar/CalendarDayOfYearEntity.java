package com.ilt.cms.api.entity.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CalendarDayOfYearEntity{

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate date;


}
