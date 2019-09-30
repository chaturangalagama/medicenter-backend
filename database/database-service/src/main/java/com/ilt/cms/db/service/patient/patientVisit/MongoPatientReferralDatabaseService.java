package com.ilt.cms.db.service.patient.patientVisit;

import com.ilt.cms.core.entity.consultation.PatientReferral;
import com.ilt.cms.database.patient.patientVisit.PatientReferralDatabaseService;
import com.ilt.cms.repository.patient.patientVisit.PatientReferralRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MongoPatientReferralDatabaseService implements PatientReferralDatabaseService {

    private PatientReferralRepository patientReferralRepository;

    public MongoPatientReferralDatabaseService(PatientReferralRepository patientReferralRepository) {
        this.patientReferralRepository = patientReferralRepository;
    }


    @Override
    public PatientReferral searchById(String referralId) {
        return patientReferralRepository.findById(referralId).orElse(null);
    }

    @Override
    public PatientReferral create(PatientReferral referral) {
        return patientReferralRepository.save(referral);
    }

    @Override
    public boolean exists(String referralId) {
        return patientReferralRepository.existsById(referralId);
    }

    @Override
    public List<PatientReferral> findByDoctorIdAndStartTime(String doctorId, LocalDateTime start, LocalDateTime end) {
        return patientReferralRepository.findAllByPatientReferrals_doctorIdAndPatientReferrals_appointmentDateTimeBetween(doctorId, start, end);
    }
}
