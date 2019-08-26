package com.ilt.cms.inventory.model.purchase;

import com.ilt.cms.inventory.model.common.UOM;

import static com.lippo.commons.util.CommonUtils.isStringValid;

public class TransferRequestItem {
    private String itemRefId;

    private String uom;

    private int quantity;

    public boolean checkValidate(){
        return isStringValid(itemRefId, uom) && quantity > 0;
    }

    public TransferRequestItem() {
    }

    public TransferRequestItem(String itemRefId, String uom, int quantity) {
        this.itemRefId = itemRefId;
        this.uom = uom;
        this.quantity = quantity;
    }

    public String getItemRefId() {
        return itemRefId;
    }

    public void setItemRefId(String itemRefId) {
        this.itemRefId = itemRefId;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "TransferRequestItem{" +
                "itemRefId='" + itemRefId + '\'' +
                ", uom=" + uom +
                ", quantity=" + quantity +
                '}';
    }
}
