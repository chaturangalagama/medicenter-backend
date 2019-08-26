package com.ilt.cms.db.service;

import com.ilt.cms.core.entity.system.SystemStore;
import com.ilt.cms.database.store.SystemStoreDatabaseService;
import com.ilt.cms.repository.spring.system.SystemStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoSystemStoreDatabaseService implements SystemStoreDatabaseService {

    private SystemStoreRepository systemStoreRepository;
    private MongoTemplate mongoTemplate;

    public MongoSystemStoreDatabaseService(SystemStoreRepository systemStoreRepository, MongoTemplate mongoTemplate) {
        this.systemStoreRepository = systemStoreRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<SystemStore> findAll() {
        return systemStoreRepository.findAll();
    }
}
