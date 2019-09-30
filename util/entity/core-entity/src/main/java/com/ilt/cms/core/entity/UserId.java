package com.ilt.cms.core.entity;

import com.lippo.commons.util.CommonUtils;

import java.util.Objects;

public class UserId {

    public enum IdType {
        NRIC_PINK, NRIC_BLUE, NRIC, FIN, MIC, PASSPORT, OTHER
    }

    private IdType idType;
    private String number;

    public UserId() {
    }

    public UserId(IdType idType, String number) {
        this.idType = idType;
        this.number = number;
    }

    public UserId update(IdType idType, String number) {
        this.idType = idType;
        this.number = number;
        return this;
    }

    public boolean areParametersValid() {
        return idType != null && CommonUtils.isStringValid(number);
    }

    public IdType getIdType() {
        return idType;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return idType == userId.idType &&
                Objects.equals(number, userId.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idType, number);
    }

    @Override
    public String toString() {
        return "UserId{" +
                "idType=" + idType +
                ", number='" + number + '\'' +
                '}';
    }
}
