package com.ilt.cms.core.entity.allergy;

import com.ilt.cms.core.entity.PersistedObject;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

public class AllergyGroup extends PersistedObject {

    @Indexed(unique = true)
    private String groupCode;
    private String description;
    private List<String> drugIds = new ArrayList<>();


    public void copy(AllergyGroup allergyGroup) {
        setGroupCode(allergyGroup.groupCode);
        setDescription(allergyGroup.description);
        setDrugIds(allergyGroup.drugIds);

    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(groupCode);
    }

    public void addDrug(String drugCode) {
        drugIds.add(drugCode);
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDrugIds() {
        return drugIds;
    }

    public void setDrugIds(List<String> drugIds) {
        this.drugIds = drugIds;
    }

    @Override
    public String toString() {
        return "AllergyGroup{" +
                "groupCode='" + groupCode + '\'' +
                ", description='" + description + '\'' +
                ", drugIds=" + drugIds +
                '}';
    }





}
