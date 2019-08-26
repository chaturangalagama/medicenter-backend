package com.ilt.cms.database;

import com.ilt.cms.core.entity.Postcode;

public interface PostcodeDatabaseService {
    Postcode findFirstByCode(String code);
}
