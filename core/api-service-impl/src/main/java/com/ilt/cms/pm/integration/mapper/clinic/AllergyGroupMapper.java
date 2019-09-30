package com.ilt.cms.pm.integration.mapper.clinic;

import com.ilt.cms.api.entity.allergy.AllergyGroupEntity;
import com.ilt.cms.core.entity.allergy.AllergyGroup;

public class AllergyGroupMapper {

    public static AllergyGroupEntity mapToEntity(AllergyGroup allergy) {
        if(allergy == null){
            return null;
        }
        AllergyGroupEntity allergyGroupEntity = new AllergyGroupEntity();
        allergyGroupEntity.setId(allergy.getId());
        allergyGroupEntity.setGroupCode(allergy.getGroupCode());
        allergyGroupEntity.setDrugIds(allergy.getDrugIds());
        allergyGroupEntity.setDescription(allergy.getDescription());

        return allergyGroupEntity;
    }

    public static AllergyGroup mapToCore(AllergyGroupEntity allergy) {
        if(allergy == null){
            return null;
        }
        AllergyGroup allergyGroup = new AllergyGroup();
        allergyGroup.setId(allergy.getId());
        allergyGroup.setGroupCode(allergy.getGroupCode());
        allergyGroup.setDrugIds(allergy.getDrugIds());
        allergyGroup.setDescription(allergy.getDescription());
        return allergyGroup;
    }
}
