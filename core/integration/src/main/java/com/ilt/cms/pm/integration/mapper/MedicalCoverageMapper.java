package com.ilt.cms.pm.integration.mapper;

import com.ilt.cms.api.entity.coverage.MedicalCoverageEntity;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;

import java.util.stream.Collectors;

public class MedicalCoverageMapper extends Mapper{

    public static MedicalCoverage mapToCore(MedicalCoverageEntity medicalCoverageEntity){
        if(medicalCoverageEntity == null){
            return null;
        }
        MedicalCoverage medicalCoverage = new MedicalCoverage();
        medicalCoverage.setId(medicalCoverageEntity.getId());
        medicalCoverage.setName(medicalCoverageEntity.getName());
        medicalCoverage.setCode(medicalCoverageEntity.getCode());
        medicalCoverage.setAccountManager(medicalCoverageEntity.getAccountManager());
        if(medicalCoverageEntity.getType() != null) {
            medicalCoverage.setType(MedicalCoverage.CoverageType.valueOf(medicalCoverageEntity.getType().name()));
        }
        medicalCoverage.setStartDate(medicalCoverageEntity.getStartDate());
        medicalCoverage.setEndDate(medicalCoverageEntity.getEndDate());
        medicalCoverage.setCreditTerms(medicalCoverageEntity.getCreditTerms());
        medicalCoverage.setWebsite(medicalCoverageEntity.getWebsite());
        medicalCoverage.setCostCenters(medicalCoverageEntity.getCostCenters());
        medicalCoverage.setPayAtClinic(medicalCoverageEntity.isPayAtClinic());
        medicalCoverage.setPolicyHolderCount(medicalCoverageEntity.getPolicyHolderCount());
        medicalCoverage.setTrackAttendance(medicalCoverageEntity.isTrackAttendance());
        medicalCoverage.setUsePatientAddressForBilling(medicalCoverageEntity.isUsePatientAddressForBilling());
        medicalCoverage.setMedicineRefillAllowed(medicalCoverageEntity.isMedicineRefillAllowed());
        medicalCoverage.setShowDiscount(medicalCoverageEntity.isShowDiscount());
        medicalCoverage.setShowMemberCard(medicalCoverageEntity.isShowMemberCard());
        medicalCoverage.setAddress(mapToCorporateAddressCore(medicalCoverageEntity.getAddress()));
        medicalCoverage.setContacts(medicalCoverageEntity.getContacts().stream()
                .map(MedicalCoverageMapper::mapToContactPersonCore).collect(Collectors.toList()));
        if(medicalCoverageEntity.getStatus() != null) {
            medicalCoverage.setStatus(Status.valueOf(medicalCoverageEntity.getStatus().name()));
        }
        medicalCoverage.setCoveragePlans(medicalCoverageEntity.getCoveragePlans().stream().map(Mapper::mapToCoveragePlanCore).collect(Collectors.toList()));

        return medicalCoverage;
    }

    public static MedicalCoverageEntity mapToEntity(MedicalCoverage medicalCoverage) {
        if(medicalCoverage == null){
            return null;
        }
        MedicalCoverageEntity medicalCoverageEntity = new MedicalCoverageEntity();
        medicalCoverageEntity.setId(medicalCoverage.getId());
        medicalCoverageEntity.setName(medicalCoverage.getName());
        medicalCoverageEntity.setCode(medicalCoverage.getCode());
        medicalCoverageEntity.setAccountManager(medicalCoverage.getAccountManager());
        if(medicalCoverage.getType() != null) {
            medicalCoverageEntity.setType(MedicalCoverageEntity.CoverageType.valueOf(medicalCoverage.getType().name()));
        }
        medicalCoverageEntity.setStartDate(medicalCoverage.getStartDate());
        medicalCoverageEntity.setEndDate(medicalCoverage.getEndDate());
        medicalCoverageEntity.setCreditTerms(medicalCoverage.getCreditTerms());
        medicalCoverageEntity.setWebsite(medicalCoverage.getWebsite());
        medicalCoverageEntity.setCostCenters(medicalCoverage.getCostCenters());
        medicalCoverageEntity.setPayAtClinic(medicalCoverage.isPayAtClinic());
        medicalCoverageEntity.setPolicyHolderCount(medicalCoverage.getPolicyHolderCount());
        medicalCoverageEntity.setTrackAttendance(medicalCoverage.isTrackAttendance());
        medicalCoverageEntity.setUsePatientAddressForBilling(medicalCoverage.isUsePatientAddressForBilling());
        medicalCoverageEntity.setMedicineRefillAllowed(medicalCoverage.isMedicineRefillAllowed());
        medicalCoverageEntity.setShowDiscount(medicalCoverage.isShowDiscount());
        medicalCoverageEntity.setShowMemberCard(medicalCoverage.isShowMemberCard());
        medicalCoverageEntity.setAddress(mapToCorporateAddressEntity(medicalCoverage.getAddress()));
        medicalCoverageEntity.setContacts(medicalCoverage.getContacts().stream()
                .map(MedicalCoverageMapper::mapToContactPersonEntity).collect(Collectors.toList()));
        if(medicalCoverage.getStatus() != null) {
            medicalCoverageEntity.setStatus(com.ilt.cms.api.entity.common.Status.valueOf(medicalCoverage.getStatus().name()));
        }
        medicalCoverageEntity.setCoveragePlans(medicalCoverage.getCoveragePlans().stream().map(Mapper::mapToCoveragePlanEntity).collect(Collectors.toList()));
        return medicalCoverageEntity;
    }


    /*public static Function<DrugSchemeEntity, DrugScheme> mapToDrugSchemeCore(){
        return drugSchemeEntity -> new DrugScheme(drugSchemeEntity.getDrugId(), mapToChargeCore(drugSchemeEntity.getOriTotalPrice()));
    }

    public static Function<DrugScheme, DrugSchemeEntity> mapToDrugSchemeEntity(){
        return drugScheme -> new DrugSchemeEntity(drugScheme.getDrugId(), mapToChargeEntity(drugScheme.getOriTotalPrice()));
    }*/

    /*public static  Function<MedicalTestSchemeEntity, MedicalTestScheme> mapToMedicalTestSchemeCore(){
        return medicalTestSchemeEntity -> new MedicalTestScheme(medicalTestSchemeEntity.getMedicalTestId(), mapToChargeCore(medicalTestSchemeEntity.getOriTotalPrice()));
    }

    public static Function<MedicalTestScheme, MedicalTestSchemeEntity> mapToMedicalTestSchemeEntity(){
        return medicalTestScheme -> new MedicalTestSchemeEntity(medicalTestScheme.getMedicalTestId(), mapToChargeEntity(medicalTestScheme.getOriTotalPrice()));
    }*/

    /*public static Function<VaccinationSchemeEntity, VaccinationScheme> mapToVaccinationSchemeCore(){
        return vaccinationScheme -> new VaccinationScheme(vaccinationScheme.getDoseId(), mapToChargeCore(vaccinationScheme.getOriTotalPrice()));
    }

    public static Function<VaccinationScheme, VaccinationSchemeEntity> mapToVaccinationSchemeEntity(){
        return vaccinationScheme -> new VaccinationSchemeEntity(vaccinationScheme.getDoseId(), mapToChargeEntity(vaccinationScheme.getOriTotalPrice()));
    }*/




}
