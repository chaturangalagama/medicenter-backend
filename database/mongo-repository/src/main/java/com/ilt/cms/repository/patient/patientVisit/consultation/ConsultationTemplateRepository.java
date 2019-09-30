package com.ilt.cms.repository.patient.patientVisit.consultation;

import com.ilt.cms.core.entity.consultation.ConsultationTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationTemplateRepository extends MongoRepository<ConsultationTemplate, String> {


}
