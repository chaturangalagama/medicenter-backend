package com.ilt.cms.inventory.model.purchase;

import java.time.LocalDate;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class GoodReturnItem extends DeliveryItem{

    private String comment;


    public boolean checkValidate() {
        return isStringValid(itemRefId, batchNumber, uom);
    }

    public GoodReturnItem() {
    }

    public GoodReturnItem(String id, String itemRefId, String uom, String batchNumber, int quantity,
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

    @Override
    public String toString() {
        return "GoodReturnItem{" +
                "id='" + id + '\'' +
                ", itemRefId='" + itemRefId + '\'' +
                ", uom='" + uom + '\'' +
                ", quantity=" + quantity +
                ", batchNumber='" + batchNumber + '\'' +
                ", expiryDate=" + expiryDate +
                ", comment='" + comment + '\'' +
                ", isCountInStock=" + isCountInStock +
                '}';
    }
}
