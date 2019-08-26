package com.ilt.cms.pm.integration.mapper;


import com.ilt.cms.api.entity.billPayment.CopayAmount;
import com.ilt.cms.api.entity.charge.ChargeEntity;
import com.ilt.cms.api.entity.coverage.CapLimiterEntity;
import com.ilt.cms.api.entity.coverage.CoveragePlanEntity;
import com.ilt.cms.api.entity.coverage.MedicalServiceSchemeEntity;
import com.ilt.cms.api.entity.coverage.PolicyHolderEntity;
import com.ilt.cms.api.payload.ApiPolicyHolderResponse;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.common.ContactPerson;
import com.ilt.cms.core.entity.common.CorporateAddress;
import com.ilt.cms.core.entity.common.Relationship;
import com.ilt.cms.core.entity.coverage.*;
import com.ilt.cms.core.entity.patient.Address;
import com.ilt.cms.core.entity.patient.ContactNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PolicyHolderMapper extends Mapper{
    public static PolicyHolderEntity mapToEntity(PolicyHolder policyHolder) {
        if(policyHolder == null){
            return null;
        }
        PolicyHolderEntity policyHolderEntity = new PolicyHolderEntity();
        policyHolderEntity.setId(policyHolder.getId());
        if (policyHolder.getAddress() != null){
            com.ilt.cms.api.entity.common.Address addressEntity = new com.ilt.cms.api.entity.common.Address(policyHolder.getAddress().getAddress(),
                    policyHolder.getAddress().getCountry(),
                    policyHolder.getAddress().getPostalCode());
        policyHolderEntity.setAddress(addressEntity);
        }

        policyHolderEntity.setIdentificationNumber(mapToUserIdEntity(policyHolder.getIdentificationNumber()));
        policyHolderEntity.setName(policyHolder.getName());
        policyHolderEntity.setMedicalCoverageId(policyHolder.getMedicalCoverageId());
        policyHolderEntity.setPlanId(policyHolder.getPlanId());
        policyHolderEntity.setPatientCoverageId(policyHolder.getPatientCoverageId());
        policyHolderEntity.setSpecialRemarks(policyHolder.getSpecialRemarks());
        policyHolderEntity.setCostCenter(policyHolder.getCostCenter());
        if(policyHolder.getMobile() != null) {
            com.ilt.cms.api.entity.common.ContactNumber mobileEntity = new com.ilt.cms.api.entity.common.ContactNumber(policyHolder.getMobile().getNumber());
            policyHolderEntity.setMobile(mobileEntity);
        }
        if(policyHolder.getOffice() != null) {
            com.ilt.cms.api.entity.common.ContactNumber officeEntity = new com.ilt.cms.api.entity.common.ContactNumber(policyHolder.getOffice().getNumber());
            policyHolderEntity.setOffice(officeEntity);
        }
        if(policyHolder.getHome() != null) {
            com.ilt.cms.api.entity.common.ContactNumber homeEntity = new com.ilt.cms.api.entity.common.ContactNumber(policyHolder.getHome().getNumber());
            policyHolderEntity.setHome(homeEntity);
        }
        if(policyHolder.getRelationship() != null) {
            com.ilt.cms.api.entity.coverage.RelationshipMapping relationshipEntity = new com.ilt.cms.api.entity.coverage.RelationshipMapping(UserIdMapper.mapToEntity(policyHolder.getRelationship().getHolderId()),
                    com.ilt.cms.api.entity.common.Relationship.valueOf(policyHolder.getRelationship().getRelationship().name()),
                    policyHolder.getRelationship().getPlanId());

            policyHolderEntity.setRelationship(relationshipEntity);
        }
        if(policyHolder.getStatus() != null) {
            policyHolderEntity.setStatus(com.ilt.cms.api.entity.common.Status.valueOf(policyHolder.getStatus().name()));
        }
        policyHolderEntity.setStartDate(policyHolder.getStartDate());
        policyHolderEntity.setEndDate(policyHolder.getEndDate());


        return policyHolderEntity;
    }

    public static PolicyHolder mapToCore(PolicyHolderEntity policyHolderEntity) {
        if(policyHolderEntity == null){
            return null;
        }
        PolicyHolder policyHolder  = new PolicyHolder();
        policyHolder.setId(policyHolderEntity.getId());
        if(policyHolderEntity.getAddress() != null) {
            Address address = new Address(policyHolderEntity.getAddress().getAddress(),
                    policyHolder.getAddress().getCountry(),
                    policyHolder.getAddress().getPostalCode());

            policyHolder.setAddress(address);
        }
        policyHolder.setIdentificationNumber(mapToUserIdCore(policyHolderEntity.getIdentificationNumber()));
        policyHolder.setName(policyHolderEntity.getName());
        policyHolder.setMedicalCoverageId(policyHolderEntity.getMedicalCoverageId());
        policyHolder.setPlanId(policyHolderEntity.getPlanId());
        policyHolder.setPatientCoverageId(policyHolderEntity.getPatientCoverageId());
        policyHolder.setSpecialRemarks(policyHolderEntity.getSpecialRemarks());
        policyHolder.setCostCenter(policyHolderEntity.getCostCenter());
        if(policyHolderEntity.getMobile() != null) {
            ContactNumber mobile = new ContactNumber(policyHolderEntity.getMobile().getNumber());
            policyHolder.setMobile(mobile);
        }
        if(policyHolderEntity.getOffice() != null) {
            ContactNumber office = new ContactNumber(policyHolderEntity.getOffice().getNumber());
            policyHolder.setOffice(office);
        }
        if(policyHolderEntity.getHome() != null) {
            ContactNumber home = new ContactNumber(policyHolderEntity.getHome().getNumber());
            policyHolder.setHome(home);
        }

        if(policyHolderEntity.getRelationship() != null) {
            RelationshipMapping relationship = new RelationshipMapping(UserIdMapper.mapToCore(policyHolderEntity.getRelationship().getHolderId()),
                    Relationship.valueOf(policyHolderEntity.getRelationship().getRelationship().name()),
                    policyHolderEntity.getRelationship().getPlanId());
            policyHolder.setRelationship(relationship);
        }
        if(policyHolderEntity.getStatus() != null) {
            policyHolder.setStatus(Status.valueOf(policyHolderEntity.getStatus().name()));
        }
        policyHolder.setStartDate(policyHolderEntity.getStartDate());
        policyHolder.setEndDate(policyHolderEntity.getEndDate());

        return policyHolder;
    }


}
