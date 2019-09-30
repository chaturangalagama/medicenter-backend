package com.ilt.cms.api.entity.consultation;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ConsultationEntity {
    private String consultationId;
    private String patientId;
    private String consultationNotes;
    private String memo;
    private String clinicNotes;
    private String doctorId;
    private String clinicId;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime consultationStartTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime consultationEndTime;
}
