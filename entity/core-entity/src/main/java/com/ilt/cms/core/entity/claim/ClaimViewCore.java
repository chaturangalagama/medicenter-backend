package com.ilt.cms.core.entity.claim;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.core.entity.casem.Claim;
import com.lippo.cms.util.CMSConstant;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClaimViewCore {

    private Claim claim;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime billDate; // invoiced date

    private String clinicId;
    private String clinicHeCode;
    private String hospitalCode;
    private String claimDoctorId;
    private List<String> diagnosisCodes = new ArrayList<>();
    private String payersName;
    private String payersNric;
    private String patientsName;
    private String patientsNric;
    private int expectedClaimAmount;
    private int claimedAmount;
    private String claimRefNo;
    private Claim.ClaimStatus claimStatus;
    private String claimRemarks;

    private String billReceiptId;
    private String documentName; //todo need explanation for this field

    private int totalAmount; // total payable invoice amount
}