package com.ilt.cms.pm.integration.mapper.patient;

import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.api.entity.vaccination.VaccinationEntity;
import com.ilt.cms.api.entity.vaccination.VaccinationSchemeEntity;
import com.ilt.cms.core.entity.UserPaymentOption;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.vaccination.Dose;
import com.ilt.cms.core.entity.vaccination.Vaccination;
import com.ilt.cms.core.entity.vaccination.VaccinationScheme;
import com.ilt.cms.pm.integration.mapper.Mapper;

import java.util.function.Function;
import java.util.stream.Collectors;

public class VaccinationMapper extends Mapper {

    public static Vaccination mapToCore(VaccinationEntity vaccinationEntity){
        if(vaccinationEntity == null){
            return null;
        }
        Vaccination vaccination = new Vaccination();
        vaccination.setName(vaccinationEntity.getName());
        vaccination.setCode(vaccinationEntity.getCode());
        vaccination.setAgeInMonths(vaccinationEntity.getAgeInMonths());
        if(vaccinationEntity.getDoses() != null) {
            vaccination.setDoses(vaccinationEntity.getDoses().stream().map(VaccinationMapper::mapToDoseCore).collect(Collectors.toList()));
        }

        return vaccination;
    }

    public static VaccinationEntity mapToEntity(Vaccination vaccination){
        if(vaccination == null){
            return null;
        }
        VaccinationEntity vaccinationEntity = new VaccinationEntity();
        vaccinationEntity.setId(vaccination.getId());
        vaccinationEntity.setName(vaccination.getName());
        vaccinationEntity.setCode(vaccination.getCode());
        vaccinationEntity.setAgeInMonths(vaccination.getAgeInMonths());
        if(vaccination.getDoses() != null) {
            vaccinationEntity.setDoses(vaccination.getDoses().stream().map(VaccinationMapper::mapToDoseEntity).collect(Collectors.toList()));
        }
        return vaccinationEntity;
    }

    public static Dose mapToDoseCore(com.ilt.cms.api.entity.vaccination.Dose doseEntity){
        if(doseEntity == null){
            return null;
        }
        Charge price = null;
        if(doseEntity.getPrice() != null) {
            price = mapToChargeCore(doseEntity.getPrice());
        }
        UserPaymentOption priceAdjustment = null;
        if(doseEntity.getPriceAdjustment() != null) {
            priceAdjustment = mapToUserPaymentOtpionCore(doseEntity.getPriceAdjustment());
        }
        Dose dose = new Dose(doseEntity.getDoseId(), doseEntity.getName(), doseEntity.getCode(), doseEntity.getDescription(),
                price, priceAdjustment, doseEntity.getNextDoseRecommendedGap());
        return dose;
    }

    public static com.ilt.cms.api.entity.vaccination.Dose mapToDoseEntity(Dose dose){
        if(dose == null){
            return null;
        }
        ChargeEntity priceEntity = null;
        if(dose.getPrice() != null) {
            priceEntity = mapToChargeEntity(dose.getPrice());
        }
        com.ilt.cms.api.entity.common.UserPaymentOption priceAdjustmentEntity = null;
        if(dose.getPriceAdjustment() != null) {
            priceAdjustmentEntity = mapToUserPaymentOtpionEntity(dose.getPriceAdjustment());
        }
        com.ilt.cms.api.entity.vaccination.Dose doseEntity = new com.ilt.cms.api.entity.vaccination.Dose(dose.getDoseId(), dose.getName(), dose.getCode(),
                dose.getDescription(), priceEntity, priceAdjustmentEntity, dose.getNextDoseRecommendedGap());
        return doseEntity;
    }

    public static Function<VaccinationSchemeEntity, VaccinationScheme> mapToVaccinationSchemeCore(){
        return vaccinationScheme -> new VaccinationScheme(vaccinationScheme.getDoseId(), mapToChargeCore(vaccinationScheme.getPrice()));
    }

    public static Function<VaccinationScheme, VaccinationSchemeEntity> mapToVaccinationSchemeEntity(){
        return vaccinationScheme -> new VaccinationSchemeEntity(vaccinationScheme.getDoseId(), mapToChargeEntity(vaccinationScheme.getPrice()));
    }
}
