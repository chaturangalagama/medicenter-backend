package com.ilt.cms.api.entity.sales;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SellingPriceEntity {

    private int price;
    private boolean taxIncluded;

}
