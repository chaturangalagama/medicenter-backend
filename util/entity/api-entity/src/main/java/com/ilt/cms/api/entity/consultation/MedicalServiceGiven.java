package com.ilt.cms.api.entity.consultation;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.api.entity.common.UserPaymentOption;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MedicalServiceGiven {
    private String id;
    private List<MedicalService> medicalServices = new ArrayList<>();

    public static class MedicalService {
        private String serviceId;
        private String serviceItemId;
        private UserPaymentOption priceAdjustment;
        private String name;
        private ChargeEntity chargeAmount;

        private UserPaymentOption availablePriceAdjustment;
    }
}
