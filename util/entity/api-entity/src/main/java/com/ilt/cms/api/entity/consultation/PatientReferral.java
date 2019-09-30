package com.ilt.cms.api.entity.consultation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.doctor.SpecialityEntity;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PatientReferral {
    private String referralId;
    private List<PatientReferralDetails> patientReferrals = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class PatientReferralDetails {
        private SpecialityEntity.Practice practice;
        private boolean externalReferral;
        private ExternalReferralDetails externalReferralDetails;
        private String clinicId;
        private String doctorId;
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        private LocalDateTime appointmentDateTime;
        private String memo;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class ExternalReferralDetails {
        private String doctorName;
        private String address;
        private String phoneNumber;
    }
}
