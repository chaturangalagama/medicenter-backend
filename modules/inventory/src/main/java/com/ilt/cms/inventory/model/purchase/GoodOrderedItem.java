package com.ilt.cms.inventory.model.purchase;

import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.common.UnitPrice;

import static com.lippo.commons.util.CommonUtils.isStringValid;


public class GoodOrderedItem{

    private String itemRefId;
    private String uom;
    private int quantity;
    private UnitPrice unitPrice;

    public boolean checkValidate(){
        return isStringValid(itemRefId, uom) && quantity > 0 && unitPrice != null;
    }

    public GoodOrderedItem() {
    }

    public GoodOrderedItem(String itemRefId, String uom, int quantity, UnitPrice unitPrice) {
        this.itemRefId = itemRefId;
        this.uom = uom;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
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

    public UnitPrice getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(UnitPrice unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "GoodOrderedItem{" +
                "itemRefId='" + itemRefId + '\'' +
                ", purchaseUom=" + uom +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
