package com.ilt.cms.core.entity.sales;

import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    public enum InvoiceState {
        INITIAL, PAID, DELETED;
    }

    public enum PaymentMode {
        CASH, NETS, VISA, MASTER_CARD, AMERICAN_EXPRESS, JCB, OTHER_CREDIT_CARD, CHEQUE, GIRO, CREDIT;
    }

    public enum InvoiceType {
        CREDIT, DIRECT;
    }

    private String visitId;
    private String invoiceNumber;
    private InvoiceType invoiceType;
    private List<PaymentInfo> paymentInfos = new ArrayList<>();
    private LocalDateTime invoiceTime;
    private LocalDateTime paidTime;
    private int payableAmount;
    private int paidAmount;
    private int taxAmount;
    private int cashAdjustmentRounding;
    private String planId;
    @Transient
    private String planName;
    private InvoiceState invoiceState;
    private String reasonForDelete;


    public Invoice(String invoiceNumber, InvoiceType invoiceType, String planId) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceType = invoiceType;
        this.planId = planId;
        invoiceTime = LocalDateTime.now();
        invoiceState = InvoiceState.INITIAL;
    }

    public Invoice() {
    }

    public void priceAdjustmentForCash() {
        cashAdjustmentRounding = payableAmount % 5;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
    }

    public List<PaymentInfo> getPaymentInfos() {
        return paymentInfos;
    }

    public void setPaymentInfos(List<PaymentInfo> paymentInfos) {
        this.paymentInfos = paymentInfos;
    }

    public LocalDateTime getInvoiceTime() {
        return invoiceTime;
    }

    public void setInvoiceTime(LocalDateTime invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    public LocalDateTime getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(LocalDateTime paidTime) {
        this.paidTime = paidTime;
    }

    public int getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(int payableAmount) {
        this.payableAmount = payableAmount;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(int taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public InvoiceState getInvoiceState() {
        return invoiceState;
    }

    public void setInvoiceState(InvoiceState invoiceState) {
        this.invoiceState = invoiceState;
    }

    public String getReasonForDelete() {
        return reasonForDelete;
    }

    public void setReasonForDelete(String reasonForDelete) {
        this.reasonForDelete = reasonForDelete;
    }

    public int getCashAdjustmentRounding() {
        return cashAdjustmentRounding;
    }

    public void setCashAdjustmentRounding(int cashAdjustmentRounding) {
        this.cashAdjustmentRounding = cashAdjustmentRounding;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceNumber='" + invoiceNumber + '\'' +
                ", visitId=" + visitId +
                ", invoiceType=" + invoiceType +
                ", paymentInfos=" + paymentInfos +
                ", invoiceTime=" + invoiceTime +
                ", paidTime=" + paidTime +
                ", payableAmount=" + payableAmount +
                ", paidAmount=" + paidAmount +
                ", taxAmount=" + taxAmount +
                ", cashAdjustmentRounding=" + cashAdjustmentRounding +
                ", planId='" + planId + '\'' +
                ", planName='" + planName + '\'' +
                ", invoiceState=" + invoiceState +
                ", reasonForDelete='" + reasonForDelete + '\'' +
                '}';
    }
}
