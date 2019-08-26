package com.ilt.cms.api.payload;

import com.ilt.cms.api.entity.common.ContactPerson;
import com.ilt.cms.api.entity.common.CorporateAddress;
import com.ilt.cms.api.entity.coverage.CoveragePlanEntity;
import com.ilt.cms.api.entity.coverage.PolicyHolderEntity;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ApiPolicyHolderResponse {

    private PolicyHolderEntity policyHolder;
    private String medicalCoverageName;
    private CorporateAddress address;
    private CoveragePlanEntity coveragePlan;
    private List<ContactPerson> contacts;


}
