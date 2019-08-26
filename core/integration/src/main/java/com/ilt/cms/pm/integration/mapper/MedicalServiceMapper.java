package com.ilt.cms.pm.integration.mapper;

import com.ilt.cms.api.entity.medicalService.MedicalServiceEntity;
import com.ilt.cms.core.entity.price.master.OverrideDefaultCharge;
import com.ilt.cms.core.entity.service.MedicalService;
import com.ilt.cms.core.entity.service.MedicalServiceItem;

import java.util.List;
import java.util.stream.Collectors;

public class MedicalServiceMapper extends Mapper{

    public static MedicalService mapToCore(MedicalServiceEntity medicalServiceEntity){
        if(medicalServiceEntity == null){
            return null;
        }
        MedicalService medicalService = new MedicalService();
        medicalService.setName(medicalServiceEntity.getName());
        medicalService.setDescription(medicalServiceEntity.getDescription());
        medicalService.setMainCategoryCode(medicalServiceEntity.getMainCategoryCode());

        List<com.ilt.cms.api.entity.medicalService.MedicalServiceItem> medicalServiceItemEntities = medicalServiceEntity.getMedicalServiceItemList();

        List<MedicalServiceItem> medicalServiceItems =
        medicalServiceItemEntities.stream().map(MedicalServiceMapper::mapToMedicalServiceItemCore).collect(Collectors.toList());
        medicalService.setMedicalServiceItemList(medicalServiceItems);

        return medicalService;
    }

    public static MedicalServiceEntity mapToEntity(MedicalService medicalService){
        if(medicalService == null){
            return null;
        }
        MedicalServiceEntity medicalServiceEntity = new MedicalServiceEntity();
        medicalServiceEntity.setName(medicalServiceEntity.getName());
        medicalServiceEntity.setDescription(medicalServiceEntity.getDescription());
        medicalServiceEntity.setMainCategoryCode(medicalServiceEntity.getMainCategoryCode());

        List<MedicalServiceItem> medicalServiceItems = medicalService.getMedicalServiceItemList();

        List<com.ilt.cms.api.entity.medicalService.MedicalServiceItem> medicalServiceItemEntities =
        medicalServiceItems.stream().map(MedicalServiceMapper::mapToMedicalServiceItemEntity).collect(Collectors.toList());
        medicalServiceEntity.setMedicalServiceItemList(medicalServiceItemEntities);
        return medicalServiceEntity;
    }

    public static MedicalServiceItem mapToMedicalServiceItemCore(com.ilt.cms.api.entity.medicalService.MedicalServiceItem medicalServiceItemEntity){
        if(medicalServiceItemEntity == null){
            return null;
        }
        MedicalServiceItem medicalServiceItem = new MedicalServiceItem();

        medicalServiceItem.setId(medicalServiceItemEntity.getId());
        medicalServiceItem.setName(medicalServiceItemEntity.getName());
        medicalServiceItem.setCode(medicalServiceItemEntity.getCode());
        medicalServiceItem.setDescription(medicalServiceItemEntity.getDescription());
        medicalServiceItem.setChargeAmount(mapToChargeCore(medicalServiceItemEntity.getChargeAmount()));
        medicalServiceItem.setPriceAdjustment(mapToUserPaymentOtpionCore(medicalServiceItemEntity.getPriceAdjustment()));

        return medicalServiceItem;
    }

    public static com.ilt.cms.api.entity.medicalService.MedicalServiceItem mapToMedicalServiceItemEntity(MedicalServiceItem medicalServiceItem){
        if(medicalServiceItem == null){
            return null;
        }
        com.ilt.cms.api.entity.medicalService.MedicalServiceItem medicalServiceItemEntity = new com.ilt.cms.api.entity.medicalService.MedicalServiceItem();
        medicalServiceItemEntity.setId(medicalServiceItem.getId());
        medicalServiceItemEntity.setName(medicalServiceItem.getName());
        medicalServiceItemEntity.setCode(medicalServiceItem.getCode());
        medicalServiceItemEntity.setDescription(medicalServiceItem.getDescription());
        medicalServiceItemEntity.setChargeAmount(mapToChargeEntity(medicalServiceItem.getChargeAmount()));
        medicalServiceItemEntity.setPriceAdjustment(mapToUserPaymentOtpionEntity(medicalServiceItem.getPriceAdjustment()));
        return medicalServiceItemEntity;

    }
}
