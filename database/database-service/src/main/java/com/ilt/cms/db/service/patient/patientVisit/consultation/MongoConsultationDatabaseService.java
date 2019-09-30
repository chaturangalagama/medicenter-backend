package com.ilt.cms.db.service.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.consultation.Consultation;
import com.ilt.cms.database.patient.patientVisit.consultation.ConsultationDatabaseService;
import com.ilt.cms.repository.patient.patientVisit.consultation.ConsultationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MongoConsultationDatabaseService implements ConsultationDatabaseService {

    private ConsultationRepository consultationRepository;

    public MongoConsultationDatabaseService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    @Override
    public List<Consultation> findByPatientId(String patientId, Pageable pageable) {
        return consultationRepository.findByPatientId(patientId, pageable);
    }

    @Override
    public List<Consultation> findByPatientId(String patientId) {
        return consultationRepository.findByPatientId(patientId);
    }

    @Override
    public List<Consultation> findByPatientIdAndConsultationStartTimeBetween(String patientId, LocalDateTime start, LocalDateTime end) {
        return consultationRepository.findByPatientIdAndConsultationStartTimeBetween(patientId, start, end);
    }

    @Override
    public Page<Consultation> findAllByDoctorIdInAndConsultationStartTimeBetween(List<String> doctorIds, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return consultationRepository.findAllByDoctorIdInAndConsultationStartTimeBetween(doctorIds, start, end, pageable);
    }

    @Override
    public List<Consultation> findFollowUpItems(LocalDate date) {
        return consultationRepository.findFollowUpItems(date);
    }

    @Override
    public Consultation searchById(String consultationId) {
        return consultationRepository.findById(consultationId).orElse(null);
    }

    @Override
    public Consultation save(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    @Override
    public boolean exists(String consultationId) {
        return consultationRepository.existsById(consultationId);
    }
}
