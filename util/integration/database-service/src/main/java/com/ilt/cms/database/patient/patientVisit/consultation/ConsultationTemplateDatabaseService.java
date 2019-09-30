package com.ilt.cms.database.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.consultation.ConsultationTemplate;

import java.util.List;

public interface ConsultationTemplateDatabaseService {
    public ConsultationTemplate findOne(String consultationTemplateId);

    public List<ConsultationTemplate> findAll();
}
