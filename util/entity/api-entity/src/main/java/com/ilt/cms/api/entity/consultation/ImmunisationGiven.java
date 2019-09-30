package com.ilt.cms.api.entity.consultation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.api.entity.common.UserPaymentOption;
import com.lippo.cms.util.CMSConstant;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ImmunisationGiven {
    private String id;
    private List<Immunisation> immunisation = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Immunisation {
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= CMSConstant.JSON_DATE_FORMAT)
        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
        private LocalDate immunisationDate;
        private String batchNumber;
        private String branch;
        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=CMSConstant.JSON_DATE_FORMAT)
        @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT)
        private LocalDate nextDose;

        private UserPaymentOption priceAdjustment;
        private String vaccinationId;
        private String doseId;
        private ChargeEntity chargeAmount;
        private UserPaymentOption availablePriceAdjustment;
    }
}
