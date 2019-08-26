package com.ilt.cms.api.entity.consultation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MedicalCertificate {
    public enum Half {
        AM_LAST, PM_FIRST
    }

    private String purpose;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate startDate;
    private double numberOfDays;
    private String referenceNumber;
    private String remark;
    private Half halfDayOption;
}
