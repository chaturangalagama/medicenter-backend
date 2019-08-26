package com.ilt.cms.api.entity.casem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ClaimEntity {

    public enum ClaimStatus {
        SUBMITTED, PENDING, REJECTED_PERMANENT, REJECTED, APPEALED, APPROVED, FAILED, PAID
    }

    private String claimId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
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
    private int gstAmount;
    private String remark;
    private ClaimStatus claimStatus;
    private ClaimResult claimResult;
    private ClaimResult paidResult;
    private SubmissionResult submissionResult = new SubmissionResult();
    private List<AppealRejection> appealRejections = new ArrayList<>();

    @Getter
    @Setter
    @ToString
    public static class ClaimResult {

        private String referenceNumber;
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
        private LocalDateTime resultDateTime;
        private int amount;
        private String statusCode;
        private String remark;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubmissionResult {

        private String claimNo;
        private String statusCode;
        private String statusDescription;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AppealRejection {

        private String reason;
    }
}
