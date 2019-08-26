package com.ilt.cms.inventory.model.common;

import java.util.Objects;

public class UnitPrice {
    private int price;
    private boolean taxIncluded;
    public UnitPrice() {
    }

    public UnitPrice(int price, boolean taxIncluded) {
        this.price = price;
        this.taxIncluded = taxIncluded;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isTaxIncluded() {
        return taxIncluded;
    }

    public void setTaxIncluded(boolean taxIncluded) {
        this.taxIncluded = taxIncluded;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitPrice unitPrice = (UnitPrice) o;
        return Double.compare(unitPrice.price, price) == 0 &&
                taxIncluded == unitPrice.taxIncluded;
    }


    @Override
    public String toString() {
        return "UnitPrice{" +
                "price=" + price +
                ", taxIncluded=" + taxIncluded +
                '}';
    }
}
