package com.ilt.cms.core.entity.charge;

import java.util.Objects;

public class Charge {

    private int price;

    private boolean taxIncluded;

    public Charge() {
    }

    public Charge(int price, boolean taxIncluded) {
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
        Charge charge = (Charge) o;
        return Double.compare(charge.price, price) == 0 &&
                taxIncluded == charge.taxIncluded;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, taxIncluded);
    }


    @Override
    public String toString() {
        return "Charge{" +
                "price=" + price +
                ", taxIncluded=" + taxIncluded +
                '}';
    }
}
