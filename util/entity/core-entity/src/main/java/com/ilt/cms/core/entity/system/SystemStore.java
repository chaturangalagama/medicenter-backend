package com.ilt.cms.core.entity.system;

import com.ilt.cms.core.entity.PersistedObject;

import java.util.ArrayList;
import java.util.List;

public class SystemStore extends PersistedObject {

    private String key;
    private List<Object> values = new ArrayList<>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
