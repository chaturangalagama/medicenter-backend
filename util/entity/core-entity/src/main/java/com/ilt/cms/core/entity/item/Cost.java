package com.ilt.cms.core.entity.item;

public class Cost {

    private int price;
    private boolean taxIncluded;

    public Cost() {
    }

    public Cost(int price, boolean taxIncluded) {
        this.price = price;
        this.taxIncluded = taxIncluded;
    }

    public int getPrice() {
        return price;
    }

    public boolean isTaxIncluded() {
        return taxIncluded;
    }
    
    @Override
    public String toString() {
        return "Cost{" +
                "price=" + price +
                ", taxIncluded=" + taxIncluded +
                '}';
    }
}
