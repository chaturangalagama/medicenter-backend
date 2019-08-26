package com.ilt.cms.core.entity.item;

import com.ilt.cms.core.entity.charge.Charge;

public class ItemCoverageScheme {

    private String itemId;
    private Charge price;

    public ItemCoverageScheme() {
    }

    public ItemCoverageScheme(String itemId, Charge price) {
        this.itemId = itemId;
        this.price = price;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Charge getPrice() {
        return price;
    }

    public void setPrice(Charge price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ItemCoverageScheme{" +
                "itemId='" + itemId + '\'' +
                ", price=" + price +
                '}';
    }
}
