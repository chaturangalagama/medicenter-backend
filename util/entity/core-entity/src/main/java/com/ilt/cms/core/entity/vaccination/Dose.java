package com.ilt.cms.core.entity.vaccination;

import com.ilt.cms.core.entity.UserPaymentOption;
import com.ilt.cms.core.entity.charge.Charge;
import com.lippo.commons.util.CommonUtils;

public class Dose {

    private String doseId;
    private String name;
    private String code;
    private String description;
    private Charge price;
    private UserPaymentOption priceAdjustment;
    private int nextDoseRecommendedGap;

    public Dose(String doseId, String name, String code, String description, Charge price, UserPaymentOption priceAdjustment, int nextDoseRecommendedGap) {
        this.doseId = doseId;
        this.name = name;
        this.code = code;
        this.description = description;
        this.price = price;
        this.priceAdjustment = priceAdjustment;
        this.nextDoseRecommendedGap = nextDoseRecommendedGap;
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(doseId, name, description) && price != null && priceAdjustment != null;
    }


    public Dose() {
    }

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

    public Charge getPrice() {
        return price;
    }

    public void setPrice(Charge price) {
        this.price = price;
    }

    public UserPaymentOption getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(UserPaymentOption priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

    public String getDoseId() {
        return doseId;
    }

    public int getNextDoseRecommendedGap() {
        return nextDoseRecommendedGap;
    }

    public String getCode() {
        return code;
    }

    public void setDoseId(String doseId) {
        this.doseId = doseId;
    }

    @Override
    public String toString() {
        return "Dose{" +
                "doseId='" + doseId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", priceAdjustment=" + priceAdjustment +
                ", nextDoseRecommendedGap=" + nextDoseRecommendedGap +
                '}';
    }
}
