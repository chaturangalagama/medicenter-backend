package com.ilt.cms.db.service.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import com.ilt.cms.database.patient.patientVisit.consultation.ConsultationFollowupDatabaseService;
import com.ilt.cms.repository.patient.patientVisit.consultation.ConsultationFollowupRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MongoConsultationFollowupDatabaseService implements ConsultationFollowupDatabaseService {

    private ConsultationFollowupRepository consultationFollowupRepository;

    public MongoConsultationFollowupDatabaseService(ConsultationFollowupRepository consultationFollowupRepository) {
        this.consultationFollowupRepository = consultationFollowupRepository;
    }

    @Override
    public ConsultationFollowup searchById(String followUpId) {
        return consultationFollowupRepository.findById(followUpId).orElse(null);
    }

    @Override
    public ConsultationFollowup save(ConsultationFollowup followup) {
        return consultationFollowupRepository.save(followup);
    }

    @Override
    public boolean exists(String followUpId) {
        return consultationFollowupRepository.existsById(followUpId);
    }

    @Override
    public List<ConsultationFollowup> findByClinicIdAndFollowupDateBetween(String clinicId, LocalDate startDate, LocalDate endDate, Sort sort) {
        return consultationFollowupRepository.findByClinicIdAndFollowupDateBetween(clinicId, startDate, endDate, sort);
    }
}
