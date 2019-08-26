package com.ilt.cms.pm.integration.mapper;

import com.ilt.cms.core.entity.UserId;


public class UserIdMapper {

    public static com.ilt.cms.api.entity.common.UserId mapToEntity(UserId userId) {
        if(userId == null){
            return null;
        }
        com.ilt.cms.api.entity.common.UserId  userIdEntity = new com.ilt.cms.api.entity.common.UserId (
                com.ilt.cms.api.entity.common.UserId.IdType.valueOf(userId.getIdType().name()),userId.getNumber());

        return userIdEntity;
    }

    public static UserId mapToCore(com.ilt.cms.api.entity.common.UserId userIdEntity) {
        if(userIdEntity == null){
            return null;
        }
        UserId userId = new UserId(UserId.IdType.valueOf(userIdEntity.getIdType().name()), userIdEntity.getNumber());

        return userId;
    }
}
