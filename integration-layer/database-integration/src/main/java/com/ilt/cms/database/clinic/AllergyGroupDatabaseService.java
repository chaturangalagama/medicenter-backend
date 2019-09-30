package com.ilt.cms.database.clinic;

import com.ilt.cms.core.entity.allergy.AllergyGroup;

import java.util.List;

public interface AllergyGroupDatabaseService {


    boolean allergyCodeExists(String groupCode);

    boolean allergyCodeExists(String groupCode, String excludeValidationCode);

    AllergyGroup findFirstByGroupCode(String name);

    AllergyGroup save(AllergyGroup allergyGroup);

    List<AllergyGroup> findAll();

    void delete(String allergyGroupId);

    AllergyGroup findOne(String allergyGroupId);


}
