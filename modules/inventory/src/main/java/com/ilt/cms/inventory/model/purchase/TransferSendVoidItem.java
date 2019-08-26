package com.ilt.cms.inventory.model.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class TransferSendVoidItem extends DeliveryItem{

    public boolean checkValidate(){
        return isStringValid(itemRefId, uom, batchNumber) && quantity > 0;
    }

    public TransferSendVoidItem() {
    }

    public TransferSendVoidItem(String id, String itemRefId, String uom, String batchNumber, int quantity,
                                LocalDate expiryDate, boolean isCountInStock) {
        super(id, itemRefId, uom, batchNumber, quantity, expiryDate);
        this.isCountInStock = isCountInStock;
    }

    @Override
    public String toString() {
        return "TransferSendVoidItem{" +
                "id='" + id + '\'' +
                ", itemRefId='" + itemRefId + '\'' +
                ", uom='" + uom + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", expiryDate=" + expiryDate +
                ", quantity=" + quantity +
                ", isCountInStock=" + isCountInStock +
                '}';
    }
}
