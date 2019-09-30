package com.ilt.cms.db.service.clinic.system;

import com.ilt.cms.core.entity.TemporaryStore;
import com.ilt.cms.database.clinic.system.TemporaryStoreDatabaseService;
import com.ilt.cms.repository.clinic.system.TemporaryStoreRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MongoTemporaryStoreDatabaseService implements TemporaryStoreDatabaseService {

    private TemporaryStoreRepository storeRepository;

    public MongoTemporaryStoreDatabaseService(TemporaryStoreRepository temporaryStoreRepository){
        this.storeRepository = temporaryStoreRepository;
    }
    @Override
    public void deleteAllByLastModifiedDateLessThanEqual(Date lastModifiedDate) {
        storeRepository.deleteAllByLastModifiedDateLessThanEqual(lastModifiedDate);
    }

    @Override
    public TemporaryStore findOne(String id) {
        return storeRepository.findById(id).orElse(null);
    }

    @Override
    public TemporaryStore save(TemporaryStore temporaryStore) {
        return storeRepository.save(temporaryStore);
    }
}
