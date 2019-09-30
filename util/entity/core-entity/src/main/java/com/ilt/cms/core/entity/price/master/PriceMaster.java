package com.ilt.cms.core.entity.price.master;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.item.SellingPrice;

import java.util.List;
import java.util.Optional;

public class PriceMaster extends PersistedObject {

    private String itemIdRef;
    private int price;
    private List<OverrideDefaultCharge> overRideList;


    public PriceMaster() {
    }

    public PriceMaster(int price) {
        this.price = price;
    }

    public Optional<SellingPrice> anyOverRiddenCharge(Class<? extends Condition> type, Object obj) {
        if (overRideList != null && overRideList.size() > 0) {
            SellingPrice matchedCharge = null;
            for (OverrideDefaultCharge charge : overRideList) {
                if (charge.doesConstraintMatch(type, obj)) {
                    if (charge.forcePriority()) {
                        return Optional.ofNullable(matchedCharge);
                    } else {
                        matchedCharge = charge.getSellingPrice();
                    }
                }
            }
            return Optional.ofNullable(matchedCharge);
        }
        return Optional.empty();

    }

    public String getItemIdRef() {
        return itemIdRef;
    }

    public void setItemIdRef(String itemIdRef) {
        this.itemIdRef = itemIdRef;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public List<OverrideDefaultCharge> getOverRideList() {
        return overRideList;
    }

    public void setOverRideList(List<OverrideDefaultCharge> overRideList) {
        this.overRideList = overRideList;
    }


    @Override
    public String toString() {
        return "PriceMaster{" +
                "price=" + price +
                ", overRideList=" + overRideList +
                '}';
    }
}
