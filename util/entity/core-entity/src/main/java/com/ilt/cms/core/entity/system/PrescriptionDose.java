package com.ilt.cms.core.entity.system;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;

import java.util.Map;

public class PrescriptionDose extends PersistedObject {

    private String code;
    private String description;
    private Map<String, String> i18nDescription;
    private Status status;


    public PrescriptionDose() {
    }

    public PrescriptionDose(String code, String description, Map<String, String> i18nDescription, Status status) {
        this.code = code;
        this.description = description;
        this.i18nDescription = i18nDescription;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getI18nDescription() {
        return i18nDescription;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "PrescriptionDose{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", i18nDescription=" + i18nDescription +
                ", status=" + status +
                '}';
    }
}
