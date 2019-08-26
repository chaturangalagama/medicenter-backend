package com.ilt.cms.core.entity.inventory;

public class InventoryUsage {
    public enum InventoryType {
        DRUG, VACCINE
    }

    private InventoryType inventoryType;
    private String itemId;
    private double quantity;
    private String batchNo = "";

    private String itemCode = "";
    private int index;

    public InventoryUsage() {}

    public InventoryUsage(InventoryType inventoryType, String itemId, double quantity) {
        this.inventoryType = inventoryType;
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public InventoryUsage(InventoryType inventoryType, String itemId, double quantity, String batchNo) {
        this.inventoryType = inventoryType;
        this.itemId = itemId;
        this.quantity = quantity;
        this.batchNo = batchNo;
    }

    public String getItemId() {
        return itemId;
    }

    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public String getBatchNo() { return batchNo; }

    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    @Override
    public String toString() {
        return "InventoryUsage{" +
                "inventoryType=" + inventoryType +
                ", itemId='" + itemId + '\'' +
                ", quantity='" + quantity + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", index=" + index +
                '}';
    }
}
