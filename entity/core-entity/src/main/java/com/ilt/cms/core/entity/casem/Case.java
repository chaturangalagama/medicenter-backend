package com.ilt.cms.core.entity.casem;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Case extends PersistedObject {

    public enum CaseStatus {
        OPEN, CLOSED,
    }

    @Indexed
    private String caseNumber;
    @Indexed
    private String patientId;
    @Indexed
    private String clinicId;
    @DBRef
    private SalesOrder salesOrder;
    private Package purchasedPackage;
    private boolean isSingleVisit;
    private LocalDateTime caseStartDateTime;
    private LocalDateTime caseEndDateTime;
//    TODO got some issue on indexing this for existing mongo data in case even the dropping indexed + removing the visitIds
//    Just remove the unique indexed and will check later
//    @Indexed(unique = true)
    private List<String> visitIds = new ArrayList<>();
    private List<AttachedMedicalCoverage> attachedMedicalCoverages;
    private CaseStatus status = CaseStatus.OPEN;


    public boolean areParametersValid() {
        return CommonUtils.isStringValid(patientId, clinicId);
    }


    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public boolean isSingleVisit() {
        return isSingleVisit;
    }

    public void setSingleVisit(boolean singleVisit) {
        isSingleVisit = singleVisit;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public List<String> getVisitIds() {
        return visitIds;
    }

    public void setVisitIds(List<String> visitIds) {
        this.visitIds = visitIds;
    }

    public SalesOrder getSalesOrder() {
        if (salesOrder == null) salesOrder = new SalesOrder();
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    public Package getPurchasedPackage() {
        if (purchasedPackage == null) purchasedPackage = new Package();
        return purchasedPackage;
    }

    public void setPurchasedPackage(Package purchasedPackage) {
        this.purchasedPackage = purchasedPackage;
    }

    public List<AttachedMedicalCoverage> getAttachedMedicalCoverages() {
        if (attachedMedicalCoverages == null)
            attachedMedicalCoverages = new ArrayList<>();
        return attachedMedicalCoverages;
    }

    public void setAttachedMedicalCoverages(List<AttachedMedicalCoverage> attachedMedicalCoverages) {
        this.attachedMedicalCoverages = attachedMedicalCoverages;
    }

    public CaseStatus getStatus() {
        return status;
    }

    public void setStatus(CaseStatus status) {
        this.status = status;
    }


    public LocalDateTime getCaseStartDateTime() {
        return caseStartDateTime;
    }

    public void setCaseStartDateTime(LocalDateTime caseStarteDateTime) {
        this.caseStartDateTime = caseStarteDateTime;
    }

    public LocalDateTime getCaseEndDateTime() {
        return caseEndDateTime;
    }

    public void setCaseEndeDateTime(LocalDateTime caseEndeDateTime) {
        this.caseEndDateTime = caseEndeDateTime;
    }

    @Override
    public String toString() {
        return "Case{" +
                "caseNumber='" + caseNumber + '\'' +
                ", patientId='" + patientId + '\'' +
                ", clinicId='" + clinicId + '\'' +
                ", salesOrder=" + salesOrder +
                ", purchasedPackage=" + purchasedPackage +
                ", isSingleVisit=" + isSingleVisit +
                ", caseStartDateTime=" + caseStartDateTime +
                ", caseEndDateTime=" + caseEndDateTime +
                ", visitIds=" + visitIds +
                ", attachedMedicalCoverages=" + attachedMedicalCoverages +
                ", status=" + status +
                '}';
    }
}
