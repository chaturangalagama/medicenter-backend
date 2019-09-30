package com.ilt.cms.api.entity.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Address {
    private String address;
    private String country;
    private String postalCode;
}
