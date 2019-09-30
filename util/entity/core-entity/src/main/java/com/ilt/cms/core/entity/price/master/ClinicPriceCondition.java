package com.ilt.cms.core.entity.price.master;

public class ClinicPriceCondition implements Condition {

    private String clinicId;

    public ClinicPriceCondition() {
    }

    public ClinicPriceCondition(String clinicId) {
        this.clinicId = clinicId;
    }

    @Override
    public boolean match(Object object) {
        return this.clinicId.equals(object);
    }


    @Override
    public String toString() {
        return "ClinicPriceCondition{" +
                "clinicId='" + clinicId + '\'' +
                '}';
    }
}
