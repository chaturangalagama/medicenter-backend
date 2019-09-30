package com.ilt.cms.core.entity;

import org.springframework.data.annotation.Transient;

public class RunningNumber extends PersistedObject {

    /**
     * This only supports a number running up to a 10 million records
     */
    @Transient
    public static final long MIN_NUMBER = 1_000_000;

    @Transient
    public static final long MAX_NUMBER = 9_900_000;

    private long runner;


    public long getRunner() {
        return runner;
    }

    public void setRunner(long runner) {
        this.runner = runner;
    }

    @Override
    public String toString() {
        return "RunningNumber{" +
                "runner=" + runner +
                ", id='" + id + '\'' +
                '}';
    }
}
