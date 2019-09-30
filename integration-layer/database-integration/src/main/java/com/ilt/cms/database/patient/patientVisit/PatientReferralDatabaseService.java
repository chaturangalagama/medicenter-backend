package com.ilt.cms.database.patient.patientVisit;

import com.ilt.cms.core.entity.consultation.PatientReferral;

import java.time.LocalDateTime;
import java.util.List;

public interface PatientReferralDatabaseService {

    PatientReferral searchById(String referralId);

    PatientReferral create(PatientReferral referral);

    boolean exists(String referralId);

    List<PatientReferral> findByDoctorIdAndStartTime(String doctorId, LocalDateTime start, LocalDateTime end);
}
