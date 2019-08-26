package com.ilt.cms.core.entity.item;

import com.ilt.cms.core.entity.PersistedObject;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CompoundIndexes({
        @CompoundIndex(name = "index_unique", unique = true, def = "{'planId' : 1, 'itemCoverageSchemes.itemId' : 1}")
})
public class MedicalCoverageItem extends PersistedObject {

    private String planId;
    private List<ItemCoverageScheme> itemCoverageSchemes;
    private List<String> excludedItemsForCoverage;

    public MedicalCoverageItem() {
        itemCoverageSchemes = new ArrayList<>();
        excludedItemsForCoverage = new ArrayList<>();
    }

    public MedicalCoverageItem(String planId, List<ItemCoverageScheme> itemCoverageSchemes, List<String> excludedItemsForCoverage) {
        this.planId = planId;
        this.itemCoverageSchemes = itemCoverageSchemes;
        this.excludedItemsForCoverage = excludedItemsForCoverage;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public List<ItemCoverageScheme> getItemCoverageSchemes() {
        return itemCoverageSchemes;
    }

    public void setItemCoverageSchemes(List<ItemCoverageScheme> itemCoverageSchemes) {
        this.itemCoverageSchemes = itemCoverageSchemes;
    }

    public List<String> getExcludedItemsForCoverage() {
        return excludedItemsForCoverage;
    }

    public void setExcludedItemsForCoverage(List<String> excludedItemsForCoverage) {
        this.excludedItemsForCoverage = excludedItemsForCoverage;
    }

    @Override
    public String toString() {
        return "MedicalCoverageItem{" +
                "planId='" + planId + '\'' +
                ", itemCoverageSchemes size=" + itemCoverageSchemes.size() +
                ", excludedItemsForCoverage size=" + excludedItemsForCoverage.size() +
                '}';
    }
}
