package com.ilt.cms.api.entity.casem;

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
