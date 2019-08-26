package com.ilt.cms.core.entity.item;

import java.util.ArrayList;
import java.util.List;

public class ItemFilter {
    public ItemFilter() {
        this.clinicIds = new ArrayList<>();
        this.clinicGroupNames = new ArrayList<>();
    }

    public ItemFilter(List<String> clinicIds, List<String> clinicGroupNames) {
        this.clinicIds = clinicIds;
        this.clinicGroupNames = clinicGroupNames;
    }

    private List<String> clinicIds;
    private List<String> clinicGroupNames;

    public void addClinicIds(List<String> clinicIds){
        this.clinicIds.addAll(clinicIds);
    }

    public void addGroupNames(List<String> groupNames){
        this.clinicGroupNames.addAll(groupNames);
    }

    public List<String> getClinicIds() {
        return clinicIds;
    }

    public void setClinicIds(List<String> clinicIds) {
        this.clinicIds = clinicIds;
    }

    public List<String> getClinicGroupNames() {
        return clinicGroupNames;
    }

    public void setClinicGroupNames(List<String> clinicGroupNames) {
        this.clinicGroupNames = clinicGroupNames;
    }

    @Override
    public String toString() {
        return "ItemFilter{" +
                "clinicIds=" + clinicIds +
                ", clinicGroupNames=" + clinicGroupNames +
                '}';
    }
}
