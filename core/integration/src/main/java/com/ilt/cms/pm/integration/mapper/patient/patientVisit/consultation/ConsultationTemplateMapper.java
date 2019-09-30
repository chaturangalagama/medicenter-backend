package com.ilt.cms.pm.integration.mapper.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.consultation.ConsultationTemplate;

public class ConsultationTemplateMapper {

    public static ConsultationTemplate mapToCore(com.ilt.cms.api.entity.consultation.ConsultationTemplate consultationTemplateEntity){
        if(consultationTemplateEntity == null){
            return null;
        }
        ConsultationTemplate consultationTemplate = new ConsultationTemplate();
        consultationTemplate.setType(consultationTemplateEntity.getType());
        consultationTemplate.setName(consultationTemplateEntity.getName());
        consultationTemplate.setTemplate(consultationTemplateEntity.getTemplate());

        return consultationTemplate;
    }

    public static com.ilt.cms.api.entity.consultation.ConsultationTemplate mapToEntity(ConsultationTemplate consultationTemplate){
        if(consultationTemplate == null){
            return null;
        }
        com.ilt.cms.api.entity.consultation.ConsultationTemplate consultationTemplateEntity = new com.ilt.cms.api.entity.consultation.ConsultationTemplate();
        consultationTemplateEntity.setType(consultationTemplate.getType());
        consultationTemplateEntity.setName(consultationTemplate.getName());
        consultationTemplateEntity.setTemplate(consultationTemplate.getTemplate());
        return consultationTemplateEntity;
    }

}
