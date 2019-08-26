package com.ilt.cms.api.entity.medicalService;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MedicalServiceEntity {
    private String id;
    private String name;
    private String description;
    private String mainCategoryCode;
    private String categoryCode;
    private List<MedicalServiceItem> medicalServiceItemList;
}
