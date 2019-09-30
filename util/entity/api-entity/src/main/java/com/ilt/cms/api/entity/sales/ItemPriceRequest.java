package com.ilt.cms.api.entity.sales;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemPriceRequest {

    private String itemId;
    private String purchasedId;
    private Set<String> excludePlanIds = new HashSet<>();
}