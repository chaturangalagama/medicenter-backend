package com.ilt.cms.report.ui.models.dataset;

public class PatientTreatmentItem {

    private int id;
    private String itemName;
    private Float totalAmount;

    public PatientTreatmentItem() {
    }

    public PatientTreatmentItem(int id, String itemName, Float totalAmount) {
        this.itemName = itemName;
        this.totalAmount = totalAmount;
        this.id = id;
    }

    public PatientTreatmentItem(String itemName, Float totalAmount) {
        this.itemName = itemName;
        this.totalAmount = totalAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
