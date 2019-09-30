package com.ilt.cms.api.entity.medicalTest;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.api.entity.common.CorporateAddress;
import com.ilt.cms.api.entity.common.Status;
import com.ilt.cms.api.entity.common.UserPaymentOption;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MedicalTestEntity {
    private String id;
    private String category;
    private String code;
    private String name;
    private ChargeEntity charge;
    private UserPaymentOption priceAdjustment;
    private Status status;
    private List<Laboratory> laboratories;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Laboratory {
        private String name;
        private CorporateAddress address;
    }


    public MedicalTestEntity(String category, String code, String name, ChargeEntity charge, UserPaymentOption priceAdjustment,
                       List<Laboratory> laboratories) {
        this.category = category;
        this.code = code;
        this.name = name;
        this.charge = charge;
        this.priceAdjustment = priceAdjustment;
        this.laboratories = laboratories;
    }
}
