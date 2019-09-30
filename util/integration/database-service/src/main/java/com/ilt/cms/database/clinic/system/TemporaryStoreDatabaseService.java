package com.ilt.cms.database.clinic.system;

import com.ilt.cms.core.entity.TemporaryStore;

import java.util.Date;

public interface TemporaryStoreDatabaseService {
    void deleteAllByLastModifiedDateLessThanEqual(Date lastModifiedDate);

    TemporaryStore findOne(String id);

    TemporaryStore save(TemporaryStore temporaryStore);
}
