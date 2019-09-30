package com.ilt.cms.api.entity.patientVisitRegistry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.reminder.ReminderStatusEntity;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ConsultationFollowUp {

    private String followupId;
    private String patientId;
    private String patientVisitId;
    private String doctorId;
    private String clinicId;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate followupDate;
    private String remarks;
    private ReminderStatusEntity reminderStatus;
}
