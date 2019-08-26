package com.ilt.cms.database.consultation;

import com.ilt.cms.core.entity.consultation.ConsultationTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ConsultationTemplateDatabaseService {
    public ConsultationTemplate findOne(String consultationTemplateId);

    public List<ConsultationTemplate> findAll();
}
