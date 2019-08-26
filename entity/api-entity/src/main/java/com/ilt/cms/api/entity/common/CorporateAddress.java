package com.ilt.cms.api.entity.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CorporateAddress {
    private String attentionTo;
    private String address;
    private String postalCode;
}
