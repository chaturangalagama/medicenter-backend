package com.ilt.cms.api.entity.casem;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CostEntity {

    private int price;
    private boolean taxIncluded;

}
