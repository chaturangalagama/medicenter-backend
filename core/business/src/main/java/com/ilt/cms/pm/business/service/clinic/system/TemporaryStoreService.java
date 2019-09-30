package com.ilt.cms.pm.business.service.clinic.system;

import com.ilt.cms.core.entity.TemporaryStore;
import com.ilt.cms.database.clinic.system.TemporaryStoreDatabaseService;
import org.springframework.stereotype.Service;

@Service
public class TemporaryStoreService {

    private TemporaryStoreDatabaseService temporaryStoreDatabaseService;

    public TemporaryStoreService(TemporaryStoreDatabaseService temporaryStoreDatabaseService) {
        this.temporaryStoreDatabaseService = temporaryStoreDatabaseService;
    }

    public TemporaryStore storeValue(String key, String value) {
        TemporaryStore storedValue = temporaryStoreDatabaseService.findOne(key);
        if (storedValue == null) {
            storedValue = new TemporaryStore(key, value);
        } else {
            storedValue.setValue(value);
        }
        temporaryStoreDatabaseService.save(storedValue);
        return storedValue;
    }

    public TemporaryStore retrieveValue(String key) {
        return temporaryStoreDatabaseService.findOne(key);
    }
}
