package com.ilt.cms.inventory.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class GoodReceiveNote {
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate grnDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate createDate;

    private String delivererId;

    private String grnNo;

    private List<GoodReceivedItem> receivedItems;

    private String additionalRemark;

    public boolean checkValidate(){
        return isStringValid(delivererId) && receivedItems != null && receivedItems.size() > 0;
    }

    public GoodReceiveNote() {
        this.receivedItems = new ArrayList<>();
    }

    public GoodReceiveNote(LocalDate createDate, LocalDate grnDate, String delivererId, String grnNo, String additionalRemark) {
        this.createDate = createDate;
        this.grnDate = grnDate;
        this.delivererId = delivererId;
        this.grnNo = grnNo;
        this.additionalRemark = additionalRemark;
        this.receivedItems = new ArrayList<>();
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getGrnDate() {
        return grnDate;
    }

    public void setGrnDate(LocalDate grnDate) {
        this.grnDate = grnDate;
    }

    public String getDelivererId() {
        return delivererId;
    }

    public void setDelivererId(String delivererId) {
        this.delivererId = delivererId;
    }

    public String getGrnNo() {
        return grnNo;
    }

    public void setGrnNo(String grnNo) {
        this.grnNo = grnNo;
    }

    public List<GoodReceivedItem> getReceivedItems() {
        return receivedItems;
    }

    public void setReceivedItems(List<GoodReceivedItem> receivedItems) {
        this.receivedItems = receivedItems;
    }

    public String getAdditionalRemark() {
        return additionalRemark;
    }

    public void setAdditionalRemark(String additionalRemark) {
        this.additionalRemark = additionalRemark;
    }

    @Override
    public String toString() {
        return "GoodReceiveNote{" +
                "grnDate=" + grnDate +
                ", delivererId='" + delivererId + '\'' +
                ", grnNo='" + grnNo + '\'' +
                ", receivedItems=" + receivedItems +
                ", additionalRemark='" + additionalRemark + '\'' +
                '}';
    }
}
