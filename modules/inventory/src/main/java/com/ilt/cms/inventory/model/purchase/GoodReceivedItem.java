package com.ilt.cms.inventory.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class GoodReceivedItem extends DeliveryItem{

    private String comment;

    public boolean checkValidate() {
        return isStringValid(itemRefId, batchNumber, uom);
    }

    public GoodReceivedItem() {
    }

    public GoodReceivedItem(String id, String itemRefId, String uom, String batchNumber, int quantity,
                            LocalDate expiryDate, String comment, boolean isCountInStock) {
        super(id, itemRefId, uom, batchNumber, quantity, expiryDate);
        this.comment = comment;
        this.isCountInStock = isCountInStock;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public boolean equals(GoodReceivedItem obj) {
        return quantity == obj.getQuantity() &&
                id.equals(obj.getId()) &&
                itemRefId.equals(obj.getItemRefId()) &&
                uom.equals(obj.getUom()) &&
                batchNumber.equals(obj.getBatchNumber()) &&
                expiryDate.equals(obj.getExpiryDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemRefId, uom, quantity, batchNumber, expiryDate);
    }

    @Override
    public String toString() {
        return "GoodReceivedItem{" +
                "id='" + id + '\'' +
                ", itemRefId='" + itemRefId + '\'' +
                ", uom=" + uom +
                ", quantity=" + quantity +
                ", batchNumber='" + batchNumber + '\'' +
                ", expiryDate=" + expiryDate +
               // ", receivedItemStatus=" + receivedItemStatus +
                ", comment='" + comment + '\'' +
                ", isCountInStock=" + isCountInStock +
                '}';
    }
}
