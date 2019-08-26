package com.ilt.cms.core.entity.diagnosis;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;

import java.util.ArrayList;
import java.util.List;

public class Diagnosis extends PersistedObject {

    private String icd10Id;
    private String snomedId;

    private String icd10Code;
    private String snomedCode;

    private String icd10Term;
    private String snomedTerm;

    private List<String> filterablePlanIds = new ArrayList<>();


    public boolean checkIfSearchMatches(String searchKey) {
        return (icd10Id != null && icd10Id.toLowerCase().contains(searchKey.toLowerCase()))
                || (snomedId != null && snomedId.toLowerCase().contains(searchKey.toLowerCase()))
                || (icd10Code != null && icd10Code.toLowerCase().contains(searchKey.toLowerCase()))
                || (snomedCode != null && snomedCode.toLowerCase().contains(searchKey.toLowerCase()))
                || (icd10Term != null && icd10Term.toLowerCase().contains(searchKey.toLowerCase()))
                || (snomedTerm != null && snomedTerm.toLowerCase().contains(searchKey.toLowerCase()));
    }

    private Status status;

    public String getIcd10Id() {
        return icd10Id;
    }

    public void setIcd10Id(String icd10Id) {
        this.icd10Id = icd10Id;
    }

    public String getSnomedId() {
        return snomedId;
    }

    public void setSnomedId(String snomedId) {
        this.snomedId = snomedId;
    }

    public String getIcd10Code() {
        return icd10Code;
    }

    public void setIcd10Code(String icd10Code) {
        this.icd10Code = icd10Code;
    }

    public String getSnomedCode() {
        return snomedCode;
    }

    public void setSnomedCode(String snomedCode) {
        this.snomedCode = snomedCode;
    }

    public String getIcd10Term() {
        return icd10Term;
    }

    public void setIcd10Term(String icd10Term) {
        this.icd10Term = icd10Term;
    }

    public String getSnomedTerm() {
        return snomedTerm;
    }

    public void setSnomedTerm(String snomedTerm) {
        this.snomedTerm = snomedTerm;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getFilterablePlanIds() {
        return filterablePlanIds;
    }

    public void setFilterablePlanIds(List<String> filterablePlanIds) {
        this.filterablePlanIds = filterablePlanIds;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "icd10Id='" + icd10Id + '\'' +
                ", snomedId='" + snomedId + '\'' +
                ", icd10Code='" + icd10Code + '\'' +
                ", snomedCode='" + snomedCode + '\'' +
                ", icd10Term='" + icd10Term + '\'' +
                ", snomedTerm='" + snomedTerm + '\'' +
                ", filterablePlanIds=" + filterablePlanIds +
                ", status=" + status +
                ", id='" + id + '\'' +
                '}';
    }
}
