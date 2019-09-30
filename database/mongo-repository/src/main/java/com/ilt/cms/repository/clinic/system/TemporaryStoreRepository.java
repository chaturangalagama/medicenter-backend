package com.ilt.cms.repository.clinic.system;

import com.ilt.cms.core.entity.TemporaryStore;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TemporaryStoreRepository extends MongoRepository<TemporaryStore, String> {

    void deleteAllByLastModifiedDateLessThanEqual(Date lastModifiedDate);
}
