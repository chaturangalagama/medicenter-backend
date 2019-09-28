package com.ilt.cms.api.entity.patientVisitRegistry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.file.FileMetadataEntity;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatientVisitRegistryEntity {
    public enum PatientVisitState {
        INITIAL, CONSULT, POST_CONSULT, PAYMENT, COMPLETE
    }

    private String id;
    private int _migrationSyncId;
    private int _migrationSyncPatientId;

    private String patientId;
    private String clinicId;
    private String visitNumber;

    private String consultationId;
    private PatientVisitState visitState;
    private String remark;
    private String billPaymentId;
    private String preferredDoctorId;
    private VisitPurposeEntity visitPurposeEntity;
    private List<FileMetadataEntity> fileMetaData = new ArrayList<>();
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime startTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime endTime;
    private VisitTimeChit visitTimeChit;

    private Map<String, Object> ctx = new HashMap<>();

}
