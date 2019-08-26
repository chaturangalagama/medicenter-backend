package com.ilt.cms.report.ui.models.dataset;

public class DailyQueueListItem {

    private Float totalPatientPaid;
    private String billMode;

    public DailyQueueListItem() {
    }

    public DailyQueueListItem(Float amount, String billMode) {
        this.totalPatientPaid = amount;
        this.billMode = billMode;
    }

    public Float getTotalPatientPaid() {
        return totalPatientPaid;
    }

    public void setTotalPatientPaid(Float totalPatientPaid) {
        this.totalPatientPaid = totalPatientPaid;
    }

    public String getBillMode() {
        return billMode;
    }

    public void setBillMode(String billMode) {
        this.billMode = billMode;
    }
}
