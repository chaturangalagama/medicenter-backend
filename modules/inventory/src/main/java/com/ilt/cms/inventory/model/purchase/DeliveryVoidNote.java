package com.ilt.cms.inventory.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class DeliveryVoidNote {
    private String requestClinicId;

    private String senderClinicId;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate createDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate receivedDate;

    private String dvnNo;

    private List<TransferSendVoidItem> transferSendVoidItems;

    public boolean checkValidate(){
        return isStringValid(requestClinicId, senderClinicId) && receivedDate != null
                && transferSendVoidItems != null && transferSendVoidItems.size() > 0;
    }

    public DeliveryVoidNote() {
        this.transferSendVoidItems = new ArrayList<>();
    }

    public DeliveryVoidNote(String requestClinicId, String senderClinicId, LocalDate createDate, LocalDate receivedDate, String dvnNo) {
        this.requestClinicId = requestClinicId;
        this.senderClinicId = senderClinicId;
        this.createDate = createDate;
        this.receivedDate = receivedDate;
        this.dvnNo = dvnNo;
        this.transferSendVoidItems = new ArrayList<>();
    }

    public String getRequestClinicId() {
        return requestClinicId;
    }

    public void setRequestClinicId(String requestClinicId) {
        this.requestClinicId = requestClinicId;
    }

    public String getSenderClinicId() {
        return senderClinicId;
    }

    public void setSenderClinicId(String senderClinicId) {
        this.senderClinicId = senderClinicId;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getDvnNo() {
        return dvnNo;
    }

    public void setDvnNo(String dvnNo) {
        this.dvnNo = dvnNo;
    }

    public List<TransferSendVoidItem> getTransferSendVoidItems() {
        return transferSendVoidItems;
    }

    public void setTransferSendVoidItems(List<TransferSendVoidItem> transferSendVoidItems) {
        this.transferSendVoidItems = transferSendVoidItems;
    }
}
