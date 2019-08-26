package com.ilt.cms.inventory.model.common;

import com.ilt.cms.core.entity.PersistedObject;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "uom")
public class UOMMaster extends PersistedObject {
    @Indexed(unique = true)
    private String code;

    private String displayName;

    public UOMMaster() {
    }

    public UOMMaster(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "UOM{" +
                "code='" + code + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
