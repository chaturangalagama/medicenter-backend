package com.ilt.cms.core.entity.visit;

import com.ilt.cms.core.entity.PersistedObject;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class VisitPurpose extends PersistedObject {

    private String name;
    private boolean consultationRequired;
    private boolean urgent;
    /**
     * Default value set to 1 to avoid sorting issue
     */
    private byte queuePrefix = 1;

    public VisitPurpose() {
    }

    public VisitPurpose(String name) {
        this.name = name;
    }

    public boolean areParametersValid() {
        return isStringValid(name);
    }

    public void copy(VisitPurpose visitPurpose) {
        name = visitPurpose.getName();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConsultationRequired() {
        return consultationRequired;
    }

    public void setConsultationRequired(boolean consultationRequired) {
        this.consultationRequired = consultationRequired;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public byte getQueuePrefix() {
        return queuePrefix;
    }

    public void setQueuePrefix(byte queuePrefix) {
        this.queuePrefix = queuePrefix;
    }

    @Override
    public String toString() {
        return "VisitPurpose{" +
                "name='" + name + '\'' +
                ", consultationRequired=" + consultationRequired +
                ", urgent=" + urgent +
                ", queuePrefix=" + queuePrefix +
                '}';
    }
}
