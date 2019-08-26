package com.ilt.cms.inventory.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class DeliveryNote {
    private String requestClinicId;

    private String senderClinicId;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate createDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate deliveryDate;

    private String deliveryNoteNo;

    private List<TransferSendItem> transferSendItems;

    public boolean checkValidate() {
        return isStringValid(requestClinicId, senderClinicId)
                && deliveryDate != null && transferSendItems != null && transferSendItems.size() > 0;
    }

    public DeliveryNote() {
        this.transferSendItems = new ArrayList<>();
    }

    public DeliveryNote(String requestClinicId, String senderClinicId, LocalDate createDate,
                        LocalDate deliveryDate, String deliveryNoteNo) {
        this.requestClinicId = requestClinicId;
        this.senderClinicId = senderClinicId;
        this.createDate = createDate;
        this.deliveryDate = deliveryDate;
        this.deliveryNoteNo = deliveryNoteNo;
        this.transferSendItems = new ArrayList<>();
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

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryNoteNo() {
        return deliveryNoteNo;
    }

    public void setDeliveryNoteNo(String deliveryNoteNo) {
        this.deliveryNoteNo = deliveryNoteNo;
    }

    public List<TransferSendItem> getTransferSendItems() {
        return transferSendItems;
    }

    public void setTransferSendItems(List<TransferSendItem> transferSendItems) {
        this.transferSendItems = transferSendItems;
    }

    @Override
    public String toString() {
        return "DeliveryNote{" +
                "requestClinicId='" + requestClinicId + '\'' +
                ", senderClinicId='" + senderClinicId + '\'' +
                ", createDate=" + createDate +
                ", deliveryDate=" + deliveryDate +
                ", deliveryNoteNo='" + deliveryNoteNo + '\'' +
                ", transferSendItems=" + transferSendItems +
                '}';
    }
}
