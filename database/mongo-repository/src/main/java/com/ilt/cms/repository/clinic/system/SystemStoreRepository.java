package com.ilt.cms.repository.clinic.system;

import com.ilt.cms.core.entity.system.SystemStore;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemStoreRepository extends MongoRepository<SystemStore, String> {

    SystemStore findByKey(String key);

}
