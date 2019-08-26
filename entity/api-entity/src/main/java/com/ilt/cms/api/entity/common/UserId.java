package com.ilt.cms.api.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserId {

    public enum IdType {
        NRIC_PINK, NRIC_BLUE, NRIC, FIN, MIC, PASSPORT, OTHER
    }

    private IdType idType;
    private String number;

}
