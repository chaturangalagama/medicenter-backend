package com.ilt.cms.core.entity.sales;

import com.lippo.commons.util.CommonUtils;

public class PaymentInfo {
    private String billTransactionId;
    private Invoice.PaymentMode billMode;
    private int amount;
    private String externalTransactionId;
    private int cashAdjustment;
    private String invoiceNumber;


    public PaymentInfo() {
    }

    public PaymentInfo(String billTransactionId, Invoice.PaymentMode billMode, int amount, String externalTransactionId, String invoiceNumber) {
        this.billTransactionId = billTransactionId;
        this.billMode = billMode;
        this.amount = amount;
        this.externalTransactionId = externalTransactionId;
        this.invoiceNumber = invoiceNumber;
    }

    public boolean areParametersValid() {
        return billMode != null && (billMode != Invoice.PaymentMode.CASH) ? CommonUtils.isStringValid(externalTransactionId) : true;
    }

    public String getBillTransactionId() {
        return billTransactionId;
    }

    public void setBillTransactionId(String billTransactionId) {
        this.billTransactionId = billTransactionId;
    }

    public Invoice.PaymentMode getBillMode() {
        return billMode;
    }

    public void setBillMode(Invoice.PaymentMode billMode) {
        this.billMode = billMode;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getExternalTransactionId() {
        return externalTransactionId;
    }

    public void setExternalTransactionId(String externalTransactionId) {
        this.externalTransactionId = externalTransactionId;
    }

    public int getCashAdjustment() {
        return cashAdjustment;
    }

    public void setCashAdjustment(int cashAdjustment) {
        this.cashAdjustment = cashAdjustment;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "billTransactionId='" + billTransactionId + '\'' +
                ", billMode=" + billMode +
                ", amount=" + amount +
                ", externalTransactionId='" + externalTransactionId + '\'' +
                ", cashAdjustment=" + cashAdjustment +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                '}';
    }
}
