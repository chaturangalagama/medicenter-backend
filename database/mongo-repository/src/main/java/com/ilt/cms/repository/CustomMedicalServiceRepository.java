package com.ilt.cms.repository;

import com.ilt.cms.repository.clinic.MedicalServiceRepository;
import com.ilt.cms.core.entity.service.MedicalService;
import com.ilt.cms.core.entity.service.MedicalServiceItem;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class CustomMedicalServiceRepository {

    private final MongoTemplate mongoTemplate;
    private MedicalServiceRepository medicalServiceRepository;


    public CustomMedicalServiceRepository(MongoTemplate mongoTemplate, MedicalServiceRepository medicalServiceRepository) {
        this.mongoTemplate = mongoTemplate;
        this.medicalServiceRepository = medicalServiceRepository;
    }

    //todo come back to this issue, even when using elemMatch, its returning the full list
    public MedicalServiceItem findMedicalServiceItem(String medicalServiceItemId) {

        Criteria elemMatch = Criteria.where("medicalServiceItemList")
                .elemMatch(Criteria.where("id").is(medicalServiceItemId));
        MedicalService medicalService = mongoTemplate.findOne(Query.query(elemMatch), MedicalService.class);
        return medicalService != null && medicalService.getMedicalServiceItemList().size() > 0
                ? medicalService.getMedicalServiceItemList().stream()
                .filter(medicalServiceItem -> medicalServiceItem.getId().equals(medicalServiceItemId))
                .findFirst().get() : null;
    }

    //todo come back to this issue, even when using elemMatch, its returning the full list
    public MedicalServiceItem findMedicalServiceItem(String medicalServiceId, String medicalServiceItemId) {
        Criteria elemMatch = Criteria.where("_id").is(medicalServiceId)
                .andOperator(Criteria.where("medicalServiceItemList")
                        .elemMatch(Criteria.where("id").is(medicalServiceItemId)));
        MedicalService medicalService = mongoTemplate.findOne(Query.query(elemMatch), MedicalService.class);
        return medicalService != null && medicalService.getMedicalServiceItemList().size() > 0
                ? medicalService.getMedicalServiceItemList().stream()
                .filter(medicalServiceItem -> medicalServiceItem.getId().equals(medicalServiceItemId))
                .findFirst().get() : null;

    }


    public MedicalServiceRepository getMedicalServiceRepository() {
        return medicalServiceRepository;
    }
}
