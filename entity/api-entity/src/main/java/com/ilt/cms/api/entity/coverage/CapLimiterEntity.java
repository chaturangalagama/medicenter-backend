package com.ilt.cms.api.entity.coverage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CapLimiterEntity {
    private int visits;
    private int limit;
}
