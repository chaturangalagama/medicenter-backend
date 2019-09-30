package com.ilt.cms.database.clinic.system;

import com.ilt.cms.core.entity.Postcode;

public interface PostcodeDatabaseService {
    Postcode findFirstByCode(String code);
}
