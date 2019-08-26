package com.ilt.cms.core.entity.vital;

import com.ilt.cms.core.entity.PersistedObject;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

public class Vital extends PersistedObject {

    @Indexed
    private String patientId;
    private LocalDateTime takenTime;

    private String code;
    private String value;
    private String comment;

    public Vital() {
    }

    public Vital(String patientId, LocalDateTime takenTime, String code, String value, String comment) {
        this.patientId = patientId;
        this.takenTime = takenTime;
        this.code = code;
        this.value = value;
        this.comment = comment;
    }

    public String getPatientId() {
        return patientId;
    }

    public LocalDateTime getTakenTime() {
        return takenTime;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public void setTakenTime(LocalDateTime takenTime) {
        this.takenTime = takenTime;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Vital{" +
                "patientId='" + patientId + '\'' +
                ", takenTime=" + takenTime +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(patientId, code, value);
    }

    public void copyVitals(Vital vital) {
        takenTime =  LocalDateTime.now();
        code = vital.code;
        value =  vital.value;
        comment = vital.comment;
    }
}
