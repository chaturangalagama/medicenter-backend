package com.ilt.cms.api.entity.billPayment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Adjustment {
    private String adjustmentId;
    private String refundNumber;
    private String clinicId;
    private String staffId;
    private double amount;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime time;
    private String reason;
    private List<AdjustmentDetails> adjustmentDetails;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public class AdjustmentDetails {
        private String paymentItemId;
        private double qty;
    }
}
