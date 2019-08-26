package com.ilt.cms.core.entity.coverage;

import com.ilt.cms.core.entity.charge.Charge;

import java.util.Objects;

/**
 * This will be the allowed schemes under a given plan. Schemes will include Medical Services
 */
public class MedicalServiceScheme {

    private String medicalServiceItemID;
    private Charge modifiedCharge;

    public MedicalServiceScheme() {
    }

    public MedicalServiceScheme(String medicalServiceItemID) {
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
        MedicalServiceScheme that = (MedicalServiceScheme) o;
        return Objects.equals(medicalServiceItemID, that.medicalServiceItemID) &&
                Objects.equals(modifiedCharge, that.modifiedCharge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicalServiceItemID, modifiedCharge);
    }

    public Charge getModifiedCharge() {
        return modifiedCharge;
    }

    public void setModifiedCharge(Charge modifiedCharge) {
        this.modifiedCharge = modifiedCharge;
    }

    @Override
    public String toString() {
        return "MedicalServiceScheme{" +
                "medicalServiceItemID='" + medicalServiceItemID + '\'' +
                ", modifiedCharge=" + modifiedCharge +
                '}';
    }
}
