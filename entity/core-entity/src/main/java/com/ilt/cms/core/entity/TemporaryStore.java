package com.ilt.cms.core.entity;

public class TemporaryStore extends PersistedObject {

    private String value;

    public TemporaryStore() {
    }

    public TemporaryStore(String key, String value) {
        this.id = key;
        this.value = value;
    }

    public String getKey() {
        return super.id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TemporaryStore{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
