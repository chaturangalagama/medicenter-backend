package com.ilt.cms.api.entity.diagnosis;

import com.ilt.cms.api.entity.common.Status;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DiagnosisEntity {
    private String id;
    private String icd10Id;
    private String snomedId;

    private String icd10Code;
    private String snomedCode;

    private String icd10Term;
    private String snomedTerm;

    private Status status;

    private List<String> filterablePlanIds = new ArrayList<>();
}
