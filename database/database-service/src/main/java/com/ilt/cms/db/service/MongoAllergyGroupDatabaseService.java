package com.ilt.cms.db.service;


import com.ilt.cms.core.entity.allergy.AllergyGroup;
import com.ilt.cms.database.allergy.AllergyGroupDatabaseService;
import com.ilt.cms.repository.spring.AllergyGroupRepository;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoAllergyGroupDatabaseService implements AllergyGroupDatabaseService {

    private AllergyGroupRepository allergyGroupRepository;

    public MongoAllergyGroupDatabaseService(AllergyGroupRepository allergyGroupRepository){
        this.allergyGroupRepository = allergyGroupRepository;
    }


    @Override
    public boolean allergyCodeExists(String groupCode) {
        return allergyGroupRepository.allergyCodeExists(groupCode);
    }

    @Override
    public boolean allergyCodeExists(String groupCode, String excludeValidationCode) {
        return allergyGroupRepository.allergyCodeExists(groupCode, excludeValidationCode);
    }

    @Override
    public AllergyGroup findFirstByGroupCode(String name) {
        return allergyGroupRepository.findFirstByGroupCode(name);
    }

    @Override
    public AllergyGroup save(AllergyGroup allergyGroup) {
        return allergyGroupRepository.save(allergyGroup);
    }

    @Override
    public List<AllergyGroup> findAll() {
        return allergyGroupRepository.findAll();
    }

    @Override
    public void delete(String allergyGroupId) {
        allergyGroupRepository.deleteById(allergyGroupId);
    }

    @Override
    public AllergyGroup findOne(String allergyGroupId) {
        return allergyGroupRepository.findById(allergyGroupId).orElse(null);
    }
}
