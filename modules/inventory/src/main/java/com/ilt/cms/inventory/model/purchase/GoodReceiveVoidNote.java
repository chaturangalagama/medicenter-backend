package com.ilt.cms.inventory.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class GoodReceiveVoidNote {
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate grnVoidDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
    private LocalDate createDate;

    private String delivererId;

    private String grnVoidNo;

    private List<GoodReturnItem> returnItems;

    private String additionalRemark;

    public boolean checkValidate() {
        return isStringValid(delivererId) && grnVoidDate != null && returnItems != null && returnItems.size() > 0;
    }

    public GoodReceiveVoidNote() {
        returnItems = new ArrayList<>();
    }

    public GoodReceiveVoidNote(LocalDate createDate, LocalDate grnVoidDate, String delivererId, String grnVoidNo, String additionalRemark) {
        this.createDate = createDate;
        this.grnVoidDate = grnVoidDate;
        this.delivererId = delivererId;
        this.grnVoidNo = grnVoidNo;
        this.additionalRemark = additionalRemark;
        this.returnItems = new ArrayList<>();
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getGrnVoidDate() {
        return grnVoidDate;
    }

    public void setGrnVoidDate(LocalDate grnVoidDate) {
        this.grnVoidDate = grnVoidDate;
    }

    public String getDelivererId() {
        return delivererId;
    }

    public void setDelivererId(String delivererId) {
        this.delivererId = delivererId;
    }

    public String getGrnVoidNo() {
        return grnVoidNo;
    }

    public void setGrnVoidNo(String grnNo) {
        this.grnVoidNo = grnNo;
    }

    public List<GoodReturnItem> getReturnItems() {
        return returnItems;
    }

    public void setReturnItems(List<GoodReturnItem> returnItems) {
        this.returnItems = returnItems;
    }

    public String getAdditionalRemark() {
        return additionalRemark;
    }

    public void setAdditionalRemark(String additionalRemark) {
        this.additionalRemark = additionalRemark;
    }

    @Override
    public String toString() {
        return "GoodReceiveVoidNote{" +
                "grnVoidDate=" + grnVoidDate +
                ", createDate=" + createDate +
                ", delivererId='" + delivererId + '\'' +
                ", grnVoidNo='" + grnVoidNo + '\'' +
                ", returnItems=" + returnItems +
                ", additionalRemark='" + additionalRemark + '\'' +
                '}';
    }

}
