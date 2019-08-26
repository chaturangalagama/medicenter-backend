package com.ilt.cms.api.entity.vaccination;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VaccinationEntity {
    private String id;
    private String name;
    private String code;
    private int ageInMonths;
    private List<Dose> doses;
}
