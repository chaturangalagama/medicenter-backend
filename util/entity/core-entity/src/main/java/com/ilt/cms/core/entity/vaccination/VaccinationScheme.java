package com.ilt.cms.core.entity.vaccination;

import com.ilt.cms.core.entity.charge.Charge;

public class VaccinationScheme {

    private String doseId;
    private Charge price;

    public VaccinationScheme() {
    }

    public VaccinationScheme(String doseId, Charge price) {
        this.doseId = doseId;
        this.price = price;
    }

    public String getDoseId() {
        return doseId;
    }

    public void setDoseId(String doseId) {
        this.doseId = doseId;
    }

    public Charge getPrice() {
        return price;
    }

    public void setPrice(Charge price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "VaccinationScheme{" +
                "doseId='" + doseId + '\'' +
                ", price=" + price +
                '}';
    }
}
