package com.ilt.cms.core.entity.visit;

import com.lippo.commons.util.CommonUtils;

public class AttachedMedicalCoverage {

    private String planId;

    public AttachedMedicalCoverage() {

    }

    public AttachedMedicalCoverage(String planId) {
        this.planId = planId;
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(planId);
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    @Override
    public String toString() {
        return "AttachedMedicalCoverage{" +
                "planId='" + planId + '\'' +
                '}';
    }
}