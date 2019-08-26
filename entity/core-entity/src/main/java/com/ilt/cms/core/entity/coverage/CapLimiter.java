package com.ilt.cms.core.entity.coverage;

public class CapLimiter {
    private int visits;
    private int limit;

    public CapLimiter() {
    }

    public CapLimiter(int visits, int limit) {
        this.visits = visits;
        this.limit = limit;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "CapLimiter{" +
                "visits=" + visits +
                ", limit=" + limit +
                '}';
    }
}
