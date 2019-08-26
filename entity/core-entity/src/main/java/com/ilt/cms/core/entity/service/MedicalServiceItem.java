package com.ilt.cms.core.entity.service;

import com.ilt.cms.core.entity.UserPaymentOption;
import com.ilt.cms.core.entity.charge.Charge;

import java.util.Objects;

public class MedicalServiceItem {

    private String id;
    private String name;
    private String code;
    private String description;
    private Charge chargeAmount;
    private UserPaymentOption priceAdjustment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Charge getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(Charge chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public UserPaymentOption getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(UserPaymentOption priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalServiceItem that = (MedicalServiceItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MedicalServiceItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", chargeAmount=" + chargeAmount +
                ", priceAdjustment=" + priceAdjustment +
                '}';
    }
}
