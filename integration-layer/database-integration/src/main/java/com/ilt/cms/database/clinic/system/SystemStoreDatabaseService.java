package com.ilt.cms.database.clinic.system;

import com.ilt.cms.core.entity.system.SystemStore;

import java.util.List;

public interface SystemStoreDatabaseService {

    List<SystemStore> findAll();
}
