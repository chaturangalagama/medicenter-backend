package com.ilt.cms.api.entity.vital;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VitalEntity {

    private String vitalId;
    private String patientId;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime takenTime;

    private String code;
    private String value;
    private String comment;
}
