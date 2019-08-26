package com.ilt.cms.api.entity.coverage;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MedicalServiceSchemeEntity {
    private String medicalServiceItemID;
    private ChargeEntity modifiedCharge;


    public MedicalServiceSchemeEntity(String medicalServiceItemID) {
        this.medicalServiceItemID = medicalServiceItemID;
    }

    public String getMedicalServiceItemID() {
        return medicalServiceItemID;
    }

    public void setMedicalServiceItemID(String medicalServiceItemID) {
        this.medicalServiceItemID = medicalServiceItemID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalServiceSchemeEntity that = (MedicalServiceSchemeEntity) o;
        return Objects.equals(medicalServiceItemID, that.medicalServiceItemID) &&
                Objects.equals(modifiedCharge, that.modifiedCharge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicalServiceItemID, modifiedCharge);
    }
}
