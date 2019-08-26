package com.ilt.cms.core.entity.system;

import com.ilt.cms.core.entity.PersistedObject;
import org.springframework.data.mongodb.core.index.Indexed;

public abstract class Config extends PersistedObject {

    @Indexed(unique = true)
    private String code;

    private String description;


}
