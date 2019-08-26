package com.ilt.cms.database.store;

import com.ilt.cms.core.entity.item.ItemFilter;
import com.ilt.cms.core.entity.system.SystemStore;

import java.util.List;

public interface SystemStoreDatabaseService {

    List<SystemStore> findAll();
}
