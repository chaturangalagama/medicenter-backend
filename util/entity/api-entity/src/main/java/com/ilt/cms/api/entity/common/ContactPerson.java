package com.ilt.cms.api.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ContactPerson {
    private String name;
    private String title;
    private String directNumber;
    private String mobileNumber;
    private String faxNumber;
    private String email;
}
