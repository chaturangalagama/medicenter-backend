package com.ilt.cms.pm.integration.mapper;

import com.ilt.cms.api.entity.billPayment.CopayAmount;
import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.api.entity.coverage.CapLimiterEntity;
import com.ilt.cms.api.entity.coverage.CoveragePlanEntity;
import com.ilt.cms.api.entity.coverage.MedicalServiceSchemeEntity;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.UserPaymentOption;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.price.master.OverrideDefaultCharge;
import com.ilt.cms.core.entity.common.ContactPerson;
import com.ilt.cms.core.entity.common.CorporateAddress;
import com.ilt.cms.core.entity.common.Relationship;
import com.ilt.cms.core.entity.coverage.CapLimiter;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalServiceScheme;


import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mapper {


    public static Charge mapToChargeCore(ChargeEntity chargeEntity){
        if(chargeEntity == null){
            return null;
        }
        Charge charge = new Charge();
        charge.setPrice(chargeEntity.getPrice());
        charge.setTaxIncluded(chargeEntity.isTaxIncluded());
        return charge;
    }

    public static ChargeEntity mapToChargeEntity(Charge charge){
        if(charge == null){
            return null;
        }
        ChargeEntity chargeEntity = new ChargeEntity();
        chargeEntity.setPrice(charge.getPrice());
        chargeEntity.setTaxIncluded(charge.isTaxIncluded());
        return chargeEntity;
    }

    public static UserPaymentOption mapToUserPaymentOtpionCore(com.ilt.cms.api.entity.common.UserPaymentOption userPaymentOptionEntity){
        if(userPaymentOptionEntity == null){
            return null;
        }
        UserPaymentOption userPaymentOption = new UserPaymentOption(userPaymentOptionEntity.getDecreaseValue(),
                userPaymentOptionEntity.getIncreaseValue(),
                UserPaymentOption.PaymentType.valueOf(userPaymentOptionEntity.getPaymentType().name()),
                userPaymentOptionEntity.getRemark());
        return userPaymentOption;

    }

    public static com.ilt.cms.api.entity.common.UserPaymentOption mapToUserPaymentOtpionEntity(UserPaymentOption userPaymentOption){
        if(userPaymentOption == null){
            return null;
        }
        com.ilt.cms.api.entity.common.UserPaymentOption  userPaymentOptionEntity = new com.ilt.cms.api.entity.common.UserPaymentOption (userPaymentOption.getDecreaseValue(),
                userPaymentOption.getIncreaseValue(),
                com.ilt.cms.api.entity.common.UserPaymentOption.PaymentType.valueOf(userPaymentOption.getPaymentType().name()),
                userPaymentOption.getRemark());
        return userPaymentOptionEntity;

    }

    public static com.ilt.cms.api.entity.common.CorporateAddress mapToCorporateAddressEntity(CorporateAddress corporateAddress){
        if(corporateAddress == null){
            return null;
        }
        com.ilt.cms.api.entity.common.CorporateAddress corporateAddress1 = new com.ilt.cms.api.entity.common.CorporateAddress();
        corporateAddress1.setAttentionTo(corporateAddress.getAttentionTo());
        corporateAddress1.setAddress(corporateAddress.getAddress());
        corporateAddress1.setPostalCode(corporateAddress.getPostalCode());
        return corporateAddress1;
    }

    public static CorporateAddress mapToCorporateAddressCore(com.ilt.cms.api.entity.common.CorporateAddress corporateAddress){
        if(corporateAddress == null){
            return null;
        }
        CorporateAddress corporateAddress1 = new CorporateAddress(corporateAddress.getAttentionTo(), corporateAddress.getAddress(), corporateAddress.getPostalCode());
        return corporateAddress1;
    }

    public static ContactPerson mapToContactPersonCore(com.ilt.cms.api.entity.common.ContactPerson contactPersonEntity){
        if(contactPersonEntity == null){
            return null;
        }
        return new ContactPerson(contactPersonEntity.getName(),
                contactPersonEntity.getTitle(),
                contactPersonEntity.getDirectNumber(),
                contactPersonEntity.getMobileNumber(),
                contactPersonEntity.getFaxNumber(),
                contactPersonEntity.getEmail());
    }

    public static com.ilt.cms.api.entity.common.ContactPerson mapToContactPersonEntity(ContactPerson contactPerson){
        if(contactPerson == null){
            return null;
        }
        return new com.ilt.cms.api.entity.common.ContactPerson(contactPerson.getName(),
                contactPerson.getTitle(),
                contactPerson.getDirectNumber(),
                contactPerson.getMobileNumber(),
                contactPerson.getFaxNumber(),
                contactPerson.getEmail());
    }

    public static CoveragePlanEntity mapToCoveragePlanEntity(CoveragePlan coveragePlan){
        if(coveragePlan == null){
            return null;
        }
        CoveragePlanEntity coveragePlanEntity = new CoveragePlanEntity();
        coveragePlanEntity.setId(coveragePlan.getId());
        coveragePlanEntity.setName(coveragePlan.getName());
        coveragePlanEntity.setCapPerVisit(mapToCapLimiterEntity(coveragePlan.getCapPerVisit()));
        coveragePlanEntity.setCapPerDay(mapToCapLimiterEntity(coveragePlan.getCapPerDay()));
        coveragePlanEntity.setCapPerWeek(mapToCapLimiterEntity(coveragePlan.getCapPerWeek()));
        coveragePlanEntity.setCapPerMonth(mapToCapLimiterEntity(coveragePlan.getCapPerMonth()));
        coveragePlanEntity.setCapPerYear(mapToCapLimiterEntity(coveragePlan.getCapPerYear()));
        coveragePlanEntity.setCapPerLifeTime(mapToCapLimiterEntity(coveragePlan.getCapPerLifeTime()));
        if(coveragePlan.getLimitResetType() != null) {
            coveragePlanEntity.setLimitResetType(CoveragePlanEntity.LimitResetType.valueOf(coveragePlan.getLimitResetType().name()));
        }
        coveragePlanEntity.setCode(coveragePlan.getCode());
        coveragePlanEntity.setRemarks(coveragePlan.getRemarks());
        coveragePlanEntity.setClinicRemarks(coveragePlan.getClinicRemarks());
        coveragePlanEntity.setRegistrationRemarks(coveragePlan.getRegistrationRemarks());
        coveragePlanEntity.setPaymentRemarks(coveragePlan.getPaymentRemarks());
        coveragePlanEntity.setExcludedClinics(coveragePlan.getExcludedClinics());
        coveragePlanEntity.setExcludeAllByDefault(coveragePlan.isExcludeAllByDefault());
//        List<MedicalServiceScheme> includedmedicalServiceSchemes = coveragePlan.getIncludedMedicalServiceSchemes();
//        List<MedicalServiceSchemeEntity> includedmedicalServiceSchemeEntities =
//                includedmedicalServiceSchemes.stream().map(PolicyHolderMapper::mapToMedicalServiceSchemeEntity).collect(Collectors.toList());
//        coveragePlanEntity.setIncludedMedicalServiceSchemes(includedmedicalServiceSchemeEntities);

//        List<MedicalServiceScheme> excludedmedicalServiceSchemes = coveragePlan.getExcludedMedicalServiceSchemes();
//        List<MedicalServiceSchemeEntity> excludedmedicalServiceSchemeEntities =
//                excludedmedicalServiceSchemes.stream().map(PolicyHolderMapper::mapToMedicalServiceSchemeEntity).collect(Collectors.toList());
//        coveragePlanEntity.setExcludedMedicalServiceSchemes(excludedmedicalServiceSchemeEntities);

        List<Relationship> relationships = coveragePlan.getAllowedRelationship();
        if(relationships != null) {
            List<com.ilt.cms.api.entity.common.Relationship> relationships1 =
                    relationships.stream().map(mapToRelationshipEntity()).collect(Collectors.toList());
            coveragePlanEntity.setAllowedRelationship(relationships1);
        }
        coveragePlanEntity.setFilterDiagnosisCode(coveragePlan.isFilterDiagnosisCode());
        coveragePlanEntity.setMinimumNumberOfDiagnosisCodes(coveragePlan.getMinimumNumberOfDiagnosisCodes());

        return coveragePlanEntity;

    }

    public static CoveragePlan mapToCoveragePlanCore(CoveragePlanEntity coveragePlanEntity){
        if(coveragePlanEntity == null){
            return null;
        }
        CoveragePlan coveragePlan = new CoveragePlan();
        coveragePlan.setId(coveragePlanEntity.getId());
        coveragePlan.setName(coveragePlanEntity.getName());
        coveragePlan.setCapPerVisit(mapToCapLimiterCore(coveragePlanEntity.getCapPerVisit()));
        coveragePlan.setCapPerDay(mapToCapLimiterCore(coveragePlanEntity.getCapPerDay()));
        coveragePlan.setCapPerWeek(mapToCapLimiterCore(coveragePlanEntity.getCapPerWeek()));
        coveragePlan.setCapPerMonth(mapToCapLimiterCore(coveragePlanEntity.getCapPerMonth()));
        coveragePlan.setCapPerYear(mapToCapLimiterCore(coveragePlanEntity.getCapPerYear()));
        coveragePlan.setCapPerLifeTime(mapToCapLimiterCore(coveragePlanEntity.getCapPerLifeTime()));
        if(coveragePlanEntity.getLimitResetType() != null) {
            coveragePlan.setLimitResetType(CoveragePlan.LimitResetType.valueOf(coveragePlanEntity.getLimitResetType().name()));
        }
        coveragePlan.setCode(coveragePlanEntity.getCode());
        coveragePlan.setRemarks(coveragePlanEntity.getRemarks());
        coveragePlan.setClinicRemarks(coveragePlanEntity.getClinicRemarks());
        coveragePlan.setRegistrationRemarks(coveragePlanEntity.getRegistrationRemarks());
        coveragePlan.setPaymentRemarks(coveragePlanEntity.getPaymentRemarks());
        coveragePlan.setExcludedClinics(coveragePlanEntity.getExcludedClinics());
        coveragePlan.setExcludeAllByDefault(coveragePlanEntity.isExcludeAllByDefault());
//        List<MedicalServiceSchemeEntity> includedmedicalServiceSchemeEntities = coveragePlanEntity.getIncludedMedicalServiceSchemes();
//        includedmedicalServiceSchemeEntities.stream().forEach(p -> coveragePlan.addSchemesToIncluded(mapToMedicalServiceSchemeCore(p)));


//        List<MedicalServiceSchemeEntity> excludedmedicalServiceSchemeEntities = coveragePlanEntity.getExcludedMedicalServiceSchemes();

//        excludedmedicalServiceSchemeEntities.stream().forEach(p -> coveragePlan.addSchemesToExcluded(mapToMedicalServiceSchemeCore(p)));

        List<com.ilt.cms.api.entity.common.Relationship> relationships = coveragePlanEntity.getAllowedRelationship();
        if(relationships != null) {
            List<Relationship> relationships1 =
                    relationships.stream().map(mapToRelationshipCore()).collect(Collectors.toList());
            coveragePlan.setAllowedRelationship(relationships1);
        }
        coveragePlan.setFilterDiagnosisCode(coveragePlanEntity.isFilterDiagnosisCode());
        coveragePlan.setMinimumNumberOfDiagnosisCodes(coveragePlanEntity.getMinimumNumberOfDiagnosisCodes());
        return coveragePlan;
    }

    public static CapLimiterEntity mapToCapLimiterEntity(CapLimiter capLimiter){
        if(capLimiter == null){
            return null;
        }
        CapLimiterEntity capLimiterEntity = new CapLimiterEntity(capLimiter.getVisits(), capLimiter.getLimit());
        return capLimiterEntity;
    }

    public static CapLimiter mapToCapLimiterCore(CapLimiterEntity capLimiterEntity){
        if(capLimiterEntity == null){
            return null;
        }
        CapLimiter capLimiter = new CapLimiter(capLimiterEntity.getVisits(), capLimiterEntity.getLimit());
        return capLimiter;
    }

    public static CopayAmount mapToCopayAmountEntity(com.ilt.cms.core.entity.CopayAmount copayAmount){
        if(copayAmount == null){
            return null;
        }
        CopayAmount copayAmountEntity = new CopayAmount(copayAmount.getValue(), CopayAmount.PaymentType.valueOf(copayAmount.getPaymentType().name()));
        return copayAmountEntity;
    }

    public static com.ilt.cms.core.entity.CopayAmount mapToCopayAmountCore(CopayAmount copayAmountEntity){
        if(copayAmountEntity == null){
            return null;
        }
        com.ilt.cms.core.entity.CopayAmount copayAmount = new com.ilt.cms.core.entity.CopayAmount(copayAmountEntity.getValue(), com.ilt.cms.core.entity.CopayAmount.PaymentType.valueOf(copayAmountEntity.getPaymentType().name()));
        return copayAmount;
    }

    public static MedicalServiceSchemeEntity mapToMedicalServiceSchemeEntity(MedicalServiceScheme medicalServiceScheme){
        if(medicalServiceScheme == null){
            return null;
        }
        MedicalServiceSchemeEntity medicalServiceSchemeEntity = new MedicalServiceSchemeEntity(medicalServiceScheme.getMedicalServiceItemID(), mapToChargeEntity(medicalServiceScheme.getModifiedCharge()));
        return medicalServiceSchemeEntity;
    }

    public static MedicalServiceScheme mapToMedicalServiceSchemeCore(MedicalServiceSchemeEntity copayAmountEntity){
        if(copayAmountEntity == null){
            return null;
        }
        MedicalServiceScheme medicalServiceScheme = new MedicalServiceScheme(copayAmountEntity.getMedicalServiceItemID());
        medicalServiceScheme.setModifiedCharge(mapToChargeCore(copayAmountEntity.getModifiedCharge()));
        return medicalServiceScheme;
    }

    public static UserId mapToUserIdCore(com.ilt.cms.api.entity.common.UserId userIdEntity){
        if(userIdEntity == null){
            return null;
        }
        if(userIdEntity.getIdType() != null) {
            return new UserId(UserId.IdType.valueOf(userIdEntity.getIdType().name()), userIdEntity.getNumber());
        }
        return null;
    }

    public static com.ilt.cms.api.entity.common.UserId mapToUserIdEntity(UserId userId){
        if(userId == null){
            return null;
        }
        if(userId.getIdType() != null) {
            return new com.ilt.cms.api.entity.common.UserId(com.ilt.cms.api.entity.common.UserId.IdType.valueOf(userId.getIdType().name()), userId.getNumber());
        }
        return null;
    }

    private static Function<com.ilt.cms.api.entity.common.Relationship, Relationship> mapToRelationshipCore(){
        return relationshipEntity -> Relationship.valueOf(relationshipEntity.name());
    }

    private static Function<Relationship, com.ilt.cms.api.entity.common.Relationship> mapToRelationshipEntity(){
        return relationship -> com.ilt.cms.api.entity.common.Relationship.valueOf(relationship.name());
    }
}
