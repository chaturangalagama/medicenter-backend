package com.ilt.cms.pm.integration.mapper.clinic;

import com.ilt.cms.core.entity.Postcode;

public class PostcodeMapper {

    public static Postcode mapToCore(com.ilt.cms.api.entity.common.Postcode postcodeEntity){
        if(postcodeEntity == null){
            return null;
        }
        return new Postcode(postcodeEntity.getCode(), postcodeEntity.getAddress());

    }

    public static com.ilt.cms.api.entity.common.Postcode mapToEntity(Postcode postcode){
        if(postcode == null){
            return null;
        }
        return new com.ilt.cms.api.entity.common.Postcode(postcode.getId(), postcode.getCode(), postcode.getAddress());
    }
}
