package com.ilt.cms.core.entity.consultation;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class DocumentStorage {
    private String id;
    private String name;
    private String description;

    public DocumentStorage() {
    }

    public DocumentStorage(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public boolean areParametersValid() {
        return isStringValid(id, name);
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "DocumentStorage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
