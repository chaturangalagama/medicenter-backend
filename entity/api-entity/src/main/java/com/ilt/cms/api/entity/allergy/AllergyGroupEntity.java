package com.ilt.cms.api.entity.allergy;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AllergyGroupEntity {
    private String id;
    private String groupCode;
    private String description;
    private List<String> drugIds;

}
