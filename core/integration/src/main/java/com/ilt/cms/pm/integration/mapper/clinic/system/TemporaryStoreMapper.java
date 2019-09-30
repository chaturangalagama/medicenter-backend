package com.ilt.cms.pm.integration.mapper.clinic.system;

import com.ilt.cms.api.entity.store.TemporaryStoreEntity;
import com.ilt.cms.core.entity.TemporaryStore;

public class TemporaryStoreMapper {

    public static TemporaryStore mapToCore(TemporaryStoreEntity temporaryStoreEntity){
        if(temporaryStoreEntity == null){
            return null;
        }
        TemporaryStore temporaryStore = new TemporaryStore(temporaryStoreEntity.getId(), temporaryStoreEntity.getValue());
        return temporaryStore;
    }

    public static  TemporaryStoreEntity mapToEntity(TemporaryStore temporaryStore){
        if(temporaryStore == null){
            return null;
        }
        TemporaryStoreEntity temporaryStoreEntity = new TemporaryStoreEntity(temporaryStore.getId(), temporaryStore.getValue());
        return temporaryStoreEntity;
    }
}
