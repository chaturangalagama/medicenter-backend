package com.ilt.cms.core.entity.item;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

public class Item extends PersistedObject {

    public enum ItemType {
        LABORATORY, IMPLANTS, CONSULTATION, SERVICE, DRUG, CONSUMABLE, VACCINATION
    }

    @Indexed(unique = true, sparse = true)
    private String name;
    @Indexed(unique = true, sparse = true)
    private String code;
    private String description;
    private String baseUom;
    private String salesUom;
    private String purchaseUom;
    private String category;
    private Cost cost;
    private SellingPrice sellingPrice;
    private int reorderPoint;
    private int reorderQty;
    private int minimumOrderQty;
    private int maximumOrderQty;
    private int safetyStockQty;
    private boolean inventoried;
    private boolean allowNegativeInventory;
    private List<SubItem> subItems = new ArrayList<>();
    //private List<String> clinicIds = new ArrayList<>();
    private ItemFilter itemFilter;
    private String supplierId;
    private ItemType itemType;
    private Status status;


    public Item() {
        this.itemFilter = new ItemFilter();
    }

    public Item(boolean inventoried, ItemType itemType) {
        this.inventoried = inventoried;
        this.itemType = itemType;
    }

    public class SubItem {
        private Item item;

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        @Override
        public String toString() {
            return "SubItem{" +
                    "item=" + item +
                    '}';
        }
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(code, name, description) && cost != null && itemType != null;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaseUom() {
        return baseUom;
    }

    public void setBaseUom(String baseUom) {
        this.baseUom = baseUom;
    }

    public String getSalesUom() {
        return salesUom;
    }

    public void setSalesUom(String salesUom) {
        this.salesUom = salesUom;
    }

    public String getPurchaseUom() {
        return purchaseUom;
    }

    public void setPurchaseUom(String purchaseUom) {
        this.purchaseUom = purchaseUom;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Cost getCost() {
        return cost;
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public SellingPrice getSellingPrice() {
        if (sellingPrice == null) return new SellingPrice();
        return sellingPrice;
    }

    public void setSellingPrice(SellingPrice sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(int reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public int getReorderQty() {
        return reorderQty;
    }

    public void setReorderQty(int reorderQty) {
        this.reorderQty = reorderQty;
    }

    public int getMinimumOrderQty() {
        return minimumOrderQty;
    }

    public void setMinimumOrderQty(int minimumOrderQty) {
        this.minimumOrderQty = minimumOrderQty;
    }

    public int getMaximumOrderQty() {
        return maximumOrderQty;
    }

    public void setMaximumOrderQty(int maximumOrderQty) {
        this.maximumOrderQty = maximumOrderQty;
    }

    public int getSafetyStockQty() {
        return safetyStockQty;
    }

    public void setSafetyStockQty(int safetyStockQty) {
        this.safetyStockQty = safetyStockQty;
    }

    public boolean isInventoried() {
        return inventoried;
    }

    public void setInventoried(boolean inventoried) {
        this.inventoried = inventoried;
    }

    public boolean isAllowNegativeInventory() {
        return allowNegativeInventory;
    }

    public void setAllowNegativeInventory(boolean allowNegativeInventory) {
        this.allowNegativeInventory = allowNegativeInventory;
    }

    public List<SubItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<SubItem> subItems) {
        this.subItems = subItems;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public ItemFilter getItemFilter() {
        return itemFilter;
    }

    public void setItemFilter(ItemFilter itemFilter) {
        this.itemFilter = itemFilter;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", baseUom='" + baseUom + '\'' +
                ", salesUom='" + salesUom + '\'' +
                ", purchaseUom='" + purchaseUom + '\'' +
                ", category='" + category + '\'' +
                ", cost=" + cost +
                ", sellingPrice=" + sellingPrice +
                ", reorderPoint=" + reorderPoint +
                ", reorderQty=" + reorderQty +
                ", minimumOrderQty=" + minimumOrderQty +
                ", maximumOrderQty=" + maximumOrderQty +
                ", safetyStockQty=" + safetyStockQty +
                ", inventoried=" + inventoried +
                ", allowNegativeInventory=" + allowNegativeInventory +
                ", subItems=" + subItems +
                ", itemFilter=" + itemFilter +
                ", supplierId='" + supplierId + '\'' +
                ", itemType=" + itemType +
                ", status=" + status +
                '}';
    }
}



