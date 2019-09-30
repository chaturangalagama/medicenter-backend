package com.ilt.cms.core.entity.patient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.core.entity.CmsConstant;

import java.time.LocalDateTime;

public class PatientPayable {

    private String invoiceId;
    private String billNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CmsConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime billDateTime;
    private int amount;
    private String rejectionReason;
    private boolean paidByUser;

    public PatientPayable() {
    }

    public PatientPayable(String invoiceId, String billNumber, LocalDateTime billDateTime, int amount, String rejectionReason, boolean paidByUser) {
        this.invoiceId = invoiceId;
        this.billNumber = billNumber;
        this.billDateTime = billDateTime;
        this.amount = amount;
        this.rejectionReason = rejectionReason;
        this.paidByUser = paidByUser;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public LocalDateTime getBillDateTime() {
        return billDateTime;
    }

    public int getAmount() {
        return amount;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public boolean isPaidByUser() {
        return paidByUser;
    }

    @Override
    public String toString() {
        return "PatientPayable{" +
                "invoiceId='" + invoiceId + '\'' +
                ", billNumber='" + billNumber + '\'' +
                ", billDateTime=" + billDateTime +
                ", amount=" + amount +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", paidByUser=" + paidByUser +
                '}';
    }
}
