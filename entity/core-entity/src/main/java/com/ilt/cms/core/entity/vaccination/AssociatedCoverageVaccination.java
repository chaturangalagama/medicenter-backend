package com.ilt.cms.core.entity.vaccination;

import com.ilt.cms.core.entity.AssociatedCoverage;
import com.ilt.cms.core.entity.PersistedObject;

import java.util.ArrayList;
import java.util.List;

/*
@CompoundIndexes({
        @CompoundIndex(name = "index_unique", unique = true, def = "{'medicalCoverageId' : 1, 'coveragePlanId' : 1, " +
                "'modifiedVaccinationScheme.doseId' : 1}"),
})
*/
public class AssociatedCoverageVaccination extends PersistedObject implements AssociatedCoverage {

    private String medicalCoverageId;
    private String coveragePlanId;
    private List<VaccinationScheme> modifiedVaccinationScheme;

    //using the same VaccinationScheme does include pricing which is not needed for this logic, This can be handled using a
    //child class or a nested class, but to keep it simple and for consistency same class is used
    private List<VaccinationScheme> excludedVaccinationScheme;

    public AssociatedCoverageVaccination() {
        modifiedVaccinationScheme = new ArrayList<>();
        excludedVaccinationScheme = new ArrayList<>();
    }


    public AssociatedCoverageVaccination(String medicalCoverageId, String coveragePlanId) {
        this();
        this.medicalCoverageId = medicalCoverageId;
        this.coveragePlanId = coveragePlanId;
    }

    public void clearFieldsForPersistence() {
        id = null;
    }


    public void addModifiedMedicalTestScheme(VaccinationScheme medicalTestScheme) {
        modifiedVaccinationScheme.add(medicalTestScheme);
    }

    public void addExcludedMedicalTestScheme(VaccinationScheme medicalTestScheme) {
        excludedVaccinationScheme.add(medicalTestScheme);

    }

    public String getCoveragePlanId() {
        return coveragePlanId;
    }

    public List<VaccinationScheme> getModifiedVaccinationScheme() {
        return modifiedVaccinationScheme;
    }

    public List<VaccinationScheme> getExcludedVaccinationScheme() {
        return excludedVaccinationScheme;
    }

    public void setCoveragePlanId(String coveragePlanId) {
        this.coveragePlanId = coveragePlanId;
    }


    public String getId() {
        return id;
    }

    public String getMedicalCoverageId() {
        return medicalCoverageId;
    }

    public void setMedicalCoverageId(String medicalCoverageId) {
        this.medicalCoverageId = medicalCoverageId;
    }

    @Override
    public String toString() {
        return "AssociatedCoverageVaccination{" +
                "id='" + id + '\'' +
                ", medicalCoverageId='" + medicalCoverageId + '\'' +
                ", coveragePlanId='" + coveragePlanId + '\'' +
                '}';
    }
}
