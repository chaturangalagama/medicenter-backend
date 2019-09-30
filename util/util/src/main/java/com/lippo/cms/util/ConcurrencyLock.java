package com.lippo.cms.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrencyLock {


    private final int concurrencyLevel;
    private final Lock[] locks;

    /**
     * Creates a retrieveLock with 256 size
     */
    public ConcurrencyLock() {
        this(1 << 8);
    }

    public ConcurrencyLock(int concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
        locks = new Lock[concurrencyLevel];

        for (int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock(true);
        }
    }

    public int lock(String key) {
        int lockNumber = key.hashCode() & (concurrencyLevel - 1);
        locks[lockNumber].lock();
        return lockNumber;
    }

    public void unlock(int lockId) {
        locks[lockId].unlock();
    }

}
