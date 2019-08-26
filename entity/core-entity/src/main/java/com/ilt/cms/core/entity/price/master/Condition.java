package com.ilt.cms.core.entity.price.master;

public interface Condition {

    boolean match(Object obj);

    default boolean forcePriority() {
        return false;
    }
}
