package com.ilt.cms.core.entity.casem;

import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Claim {

    public Claim() {
    }

    public enum ClaimStatus {
        // isUpdatable, isFinalState
        SUBMITTED(false, false),
        PENDING(true, false),
        REJECTED_PERMANENT(false, true),
        REJECTED(false, false),
        APPEALED(false, false),
        APPROVED(false, false),
        FAILED(false, true),
        PAID(false, true);

        private boolean isUpdatable;

        private boolean isFinalState;

        ClaimStatus(boolean isUpdatable, boolean isFinalState) {
            this.isUpdatable = isUpdatable;
            this.isFinalState = isFinalState;
        }

        public boolean isUpdatable() {
            return this.isUpdatable;
        }

        public boolean isFinalState() {
            return isFinalState;
        }
    }

    private String claimId;
    private LocalDateTime submissionDateTime;
    private String attendingDoctorId;
    private String claimDoctorId;
    private String payersNric;
    private String payersName;
    private List<String> diagnosisCodes = new ArrayList<>();

    private int consultationAmt;
    private int medicationAmt;
    private int medicalTestAmt;
    private int otherAmt;
    private int claimExpectedAmt;
    private int claimedAmount; // claim approved amount
    private int gstAmount;

    private String claimRefNo;
    private String remark;

    private ClaimStatus claimStatus;
    private ClaimResult claimResult;
    private ClaimResult paidResult;

    private SubmissionResult submissionResult;

    private boolean manuallyUpdated;

    private List<AppealRejection> appealRejections = new ArrayList<>();

    public static class ClaimResult {
        private String referenceNumber;
        private LocalDateTime resultDateTime;
        private int amount;
        private String statusCode;
        private String remark;

        public ClaimResult() {
        }

        public ClaimResult(String referenceNumber, LocalDateTime resultDateTime, int amount, String statusCode,
                           String remark) {
            this.referenceNumber = referenceNumber;
            this.resultDateTime = resultDateTime;
            this.amount = amount;
            this.statusCode = statusCode;
            this.remark = remark;
        }

        public String getReferenceNumber() {
            return referenceNumber;
        }

        public void setReferenceNumber(String referenceNumber) {
            this.referenceNumber = referenceNumber;
        }

        public LocalDateTime getResultDateTime() {
            return resultDateTime;
        }

        public void setResultDateTime(LocalDateTime resultDateTime) {
            this.resultDateTime = resultDateTime;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Override
        public String toString() {
            return "ClaimResult{" +
                    "referenceNumber='" + referenceNumber + '\'' +
                    ", resultDateTime=" + resultDateTime +
                    ", amount=" + amount +
                    ", statusCode='" + statusCode + '\'' +
                    ", remark='" + remark + '\'' +
                    '}';
        }
    }

    @Setter
    public static class SubmissionResult {
        private String claimNo;
        private String statusCode;
        private String statusDescription;

        public SubmissionResult() {
        }

        public SubmissionResult(String claimNo, String statusCode, String statusDescription) {
            this.claimNo = claimNo;
            this.statusCode = statusCode;
            this.statusDescription = statusDescription;
        }

        public String getClaimNo() {
            return claimNo;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public String getStatusDescription() {
            return statusDescription;
        }
    }

    public static class AppealRejection {

        public AppealRejection() {
        }

        public AppealRejection(String reason) {
            this.reason = reason;
        }

        private String reason;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }


    public LocalDateTime getSubmissionDateTime() {
        return submissionDateTime;
    }

    public void setSubmissionDateTime(LocalDateTime submissionDateTime) {
        this.submissionDateTime = submissionDateTime;
    }

    public String getAttendingDoctorId() {
        return attendingDoctorId;
    }

    public void setAttendingDoctorId(String attendingDoctorId) {
        this.attendingDoctorId = attendingDoctorId;
    }

    public String getClaimDoctorId() {
        return claimDoctorId;
    }

    public void setClaimDoctorId(String claimDoctorId) {
        this.claimDoctorId = claimDoctorId;
    }

    public int getConsultationAmt() {
        return consultationAmt;
    }

    public void setConsultationAmt(int consultationAmt) {
        this.consultationAmt = consultationAmt;
    }

    public int getMedicationAmt() {
        return medicationAmt;
    }

    public void setMedicationAmt(int medicationAmt) {
        this.medicationAmt = medicationAmt;
    }

    public int getMedicalTestAmt() {
        return medicalTestAmt;
    }

    public void setMedicalTestAmt(int medicalTestAmt) {
        this.medicalTestAmt = medicalTestAmt;
    }

    public int getOtherAmt() {
        return otherAmt;
    }

    public void setOtherAmt(int otherAmt) {
        this.otherAmt = otherAmt;
    }

    public int getClaimExpectedAmt() {
        return claimExpectedAmt;
    }

    public void setClaimExpectedAmt(int claimExpectedAmt) {
        this.claimExpectedAmt = claimExpectedAmt;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<AppealRejection> getAppealRejections() {
        return appealRejections;
    }

    public void setAppealRejections(List<AppealRejection> appealRejections) {
        this.appealRejections = appealRejections;
    }

    public ClaimResult getClaimResult() {
        return claimResult;
    }

    public void setClaimResult(ClaimResult claimResult) {
        this.claimResult = claimResult;
    }


    public void setPayersNric(String payersNric) {
        this.payersNric = payersNric;
    }

    public String getPayersNric() {
        return payersNric;
    }


    public String getPayersName() {
        return payersName;
    }

    public void setPayersName(String payersName) {
        this.payersName = payersName;
    }


    public List<String> getDiagnosisCodes() {
        return diagnosisCodes;
    }

    public void setDiagnosisCodes(List<String> diagnosisCodes) {
        this.diagnosisCodes = diagnosisCodes;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public SubmissionResult getSubmissionResult() {
        return submissionResult;
    }

    public void setSubmissionResult(SubmissionResult submissionResult) {
        this.submissionResult = submissionResult;
    }

    public ClaimResult getPaidResult() {
        return paidResult;
    }

    public void setPaidResult(ClaimResult paidResult) {
        this.paidResult = paidResult;
    }

    public int getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(int gstAmount) {
        this.gstAmount = gstAmount;
    }

    public boolean isManuallyUpdated() {
        return manuallyUpdated;
    }

    public void setManuallyUpdated(boolean manuallyUpdated) {
        this.manuallyUpdated = manuallyUpdated;
    }

    public String getClaimRefNo() {
        return claimRefNo;
    }

    public void setClaimRefNo(String claimRefNo) {
        this.claimRefNo = claimRefNo;
    }

    public int getClaimedAmount() {
        return claimedAmount;
    }

    public void setClaimedAmount(int claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "claimId='" + claimId + '\'' +
                ", submissionDateTime=" + submissionDateTime +
                ", attendingDoctorId='" + attendingDoctorId + '\'' +
                ", claimDoctorId='" + claimDoctorId + '\'' +
                ", payersNric='" + payersNric + '\'' +
                ", payersName='" + payersName + '\'' +
                ", diagnosisCodes=" + diagnosisCodes +
                ", consultationAmt=" + consultationAmt +
                ", medicationAmt=" + medicationAmt +
                ", medicalTestAmt=" + medicalTestAmt +
                ", otherAmt=" + otherAmt +
                ", claimExpectedAmt=" + claimExpectedAmt +
                ", gstAmount=" + gstAmount +
                ", remark='" + remark + '\'' +
                ", claimStatus=" + claimStatus +
                ", claimResult=" + claimResult +
                ", paidResult=" + paidResult +
                ", submissionResult=" + submissionResult +
                ", appealRejectionViews=" + appealRejections +
                ", manuallyUpdated=" + manuallyUpdated +
                '}';
    }
}
