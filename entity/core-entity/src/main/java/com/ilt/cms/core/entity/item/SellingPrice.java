package com.ilt.cms.core.entity.item;

import com.lippo.cms.util.Calculations;

public class SellingPrice {

    private int price;
    private boolean taxIncluded;

    public SellingPrice() {
    }

    public SellingPrice(int price, boolean taxIncluded) {
        this.price = price;
        this.taxIncluded = taxIncluded;
    }

    public int getPrice() {
        return price;
    }

    public boolean isTaxIncluded() {
        return taxIncluded;
    }

    public int getPriceWithTax(int taxPercentage) {
        if (!taxIncluded) {
            return price + Calculations.calculatePercentage(price, taxPercentage);
        }
        return price;
    }
    
    @Override
    public String toString() {
        return "Cost{" +
                "price=" + price +
                ", taxIncluded=" + taxIncluded +
                '}';
    }
}
