package com.ilt.cms.db.service.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.consultation.ConsultationTemplate;
import com.ilt.cms.database.patient.patientVisit.consultation.ConsultationTemplateDatabaseService;
import com.ilt.cms.repository.patient.patientVisit.consultation.ConsultationTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoConsultationTemplateDatabaseService implements ConsultationTemplateDatabaseService {

    private ConsultationTemplateRepository consultationTemplateRepository;

    public MongoConsultationTemplateDatabaseService(ConsultationTemplateRepository consultationTemplateRepository){
        this.consultationTemplateRepository = consultationTemplateRepository;
    }
    @Override
    public ConsultationTemplate findOne(String consultationTemplateId) {
        return consultationTemplateRepository.findById(consultationTemplateId).orElse(null);
    }

    @Override
    public List<ConsultationTemplate> findAll() {
        return consultationTemplateRepository.findAll();
    }
}
