package com.ilt.cms.core.entity.service;

import com.ilt.cms.core.entity.PersistedObject;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.util.List;

@CompoundIndexes({
        @CompoundIndex(name = "index_unique", unique = true, def = "{'name' : 1}"),
        @CompoundIndex(name = "index_medical_service_item_id", def = "{'name' : 1, 'medicalServiceItemList.id' : 1}"),
        @CompoundIndex(name = "index_medical_service_item_name", def = "{'name' : 1, 'medicalServiceItemList.name' : 1}")
})
public class MedicalService extends PersistedObject {

    private String name;
    private String description;
    private String mainCategoryCode;
    private String categoryCode;
    private List<MedicalServiceItem> medicalServiceItemList;

    public MedicalServiceItem findMedicalServiceItem(String medicalServiceItemId) {
        for (MedicalServiceItem medicalServiceItem : medicalServiceItemList) {
            if (medicalServiceItem.getId().equals(medicalServiceItemId)) {
                return medicalServiceItem;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MedicalServiceItem> getMedicalServiceItemList() {
        return medicalServiceItemList;
    }

    public void setMedicalServiceItemList(List<MedicalServiceItem> medicalServiceItemList) {
        this.medicalServiceItemList = medicalServiceItemList;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }


    public String getMainCategoryCode() {
        return mainCategoryCode;
    }

    public void setMainCategoryCode(String mainCategoryCode) {
        this.mainCategoryCode = mainCategoryCode;
    }

    @Override
    public String toString() {
        return "MedicalService{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", mainCategoryCode='" + mainCategoryCode + '\'' +
                ", categoryCode='" + categoryCode + '\'' +
                ", medicalServiceItemList=" + medicalServiceItemList +
                '}';
    }
}
