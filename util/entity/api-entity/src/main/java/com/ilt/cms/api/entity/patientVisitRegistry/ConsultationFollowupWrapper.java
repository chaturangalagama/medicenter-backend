package com.ilt.cms.api.entity.patientVisitRegistry;

import com.ilt.cms.api.entity.patient.PatientEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ConsultationFollowupWrapper {
    ConsultationFollowUp followups;
    PatientEntity patient;
}
