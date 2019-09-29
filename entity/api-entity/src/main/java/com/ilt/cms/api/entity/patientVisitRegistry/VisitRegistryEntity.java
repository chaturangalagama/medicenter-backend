package com.ilt.cms.api.entity.patientVisitRegistry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.diagnosis.DiagnosisEntity;
import com.ilt.cms.api.entity.medical.PatientReferralEntity;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VisitRegistryEntity {

    public enum Priority {

        HIGH, NORMAL, LOW
    }

    public enum PatientVisitState {
        INITIAL, CONSULT, POST_CONSULT, PAYMENT, COMPLETE
    }

    private String visitId;
    private String visitNumber;
    private String patientId;
    private String clinicId;
    private String preferredDoctorId;
    private String visitPurpose;
    private Priority priority;
    private PatientReferralEntity patientReferralEntity;
    private PatientVisitState visitStatus;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime startTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime endTime;
    private String remark;



    //Not mapped used only for data transfer
    private String clinicName;
    private List<DiagnosisEntity> diagnosisEntities;

    private PatientVisitRegistry.PatientQueue patientQueue;
}
