package com.ilt.cms.core.entity.calendar;

import java.time.LocalDate;

public interface CalendarDay {

    boolean isDayMatches(LocalDate localDate);
}
