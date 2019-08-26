package com.ilt.cms.report.ui.models.dataset;

public class PrivateCorporatePatientListingItem {

    private String charge;
    private Float quantity;
    private Float price;

    public PrivateCorporatePatientListingItem(String charge, Float quantity, Float price) {
        this.charge = charge;
        this.quantity = quantity;
        this.price = price;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
