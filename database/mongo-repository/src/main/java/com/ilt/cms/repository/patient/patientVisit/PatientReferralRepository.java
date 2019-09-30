package com.ilt.cms.repository.patient.patientVisit;

import com.ilt.cms.core.entity.consultation.PatientReferral;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PatientReferralRepository extends MongoRepository<PatientReferral, String> {

    List<PatientReferral> findAllByPatientReferrals_doctorIdAndPatientReferrals_appointmentDateTimeBetween(String doctorId, LocalDateTime start, LocalDateTime end);
}
