package com.lippo.cms.inventory.service;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.pm.business.service.ItemService;
import com.ilt.cms.repository.spring.ClinicRepository;
import com.ilt.cms.repository.spring.ItemRepository;

import com.lippo.cms.exception.CMSException;
import com.lippo.cms.inventory.model.DrugDispensing;
import com.lippo.cms.inventory.model.InventoryDetail;
import com.lippo.cms.inventory.model.InventoryUsage;
import com.lippo.cms.inventory.repository.LegacyInventoryRepository;

import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class LegacyInventoryService {
    private static final Logger logger = LoggerFactory.getLogger(LegacyInventoryService.class);

    private LegacyInventoryRepository legacyInventoryRepository;
    private ClinicRepository clinicRepository;
    private ItemRepository itemRepository;

    public LegacyInventoryService(LegacyInventoryRepository legacyInventoryRepository, ClinicRepository clinicRepository,
                                  ItemRepository itemRepository) {
        this.legacyInventoryRepository = legacyInventoryRepository;
        this.clinicRepository = clinicRepository;
        this.itemRepository = itemRepository;
    }

    private List<InventoryUsage> transformInventoryUsagesItemCode(List<InventoryUsage> inventoryUsages) {
        List<InventoryUsage> validInventoryUsages = new ArrayList<>();
        int index = 0;
        for (InventoryUsage inventoryUsage: inventoryUsages) {
            Optional<Item> itemOpt = itemRepository.findById(inventoryUsage.getItemId());
            if (itemOpt.isPresent()) {
                Item item = itemOpt.get();
                inventoryUsage.setItemCode(item.getCode());
                inventoryUsage.setIndex(index);
                index++;
                validInventoryUsages.add(inventoryUsage);
            }
        }
        return validInventoryUsages;
    }

    public List<InventoryDetail> getInventoryDetailList(String clinicId, List<InventoryUsage> inventoryUsages, boolean isWithBatchNo) throws CMSException {
        Optional<Clinic> clinicOpt = clinicRepository.findById(clinicId);
        if(!clinicOpt.isPresent()){
            logger.error("clinic does not exist for: [" + clinicId + "]");
            throw new CMSException(StatusCode.E2000);
        }
        Clinic clinic = clinicOpt.get();
        List<InventoryUsage> validInventoryUsages = transformInventoryUsagesItemCode(inventoryUsages);
        logger.debug("transformed inventory usages: " + validInventoryUsages);
        //search condition
        List<InventoryDetail> inventoryDetailList = isWithBatchNo ? legacyInventoryRepository.getInventoryDetailListWithBatchNo(clinic.getClinicCode(), validInventoryUsages) : legacyInventoryRepository.getInventoryDetailList(clinic.getClinicCode(), validInventoryUsages);
        if (inventoryDetailList.size() == 0) {
            logger.error("inventory detail does not exist for clinic: [" + clinicId + "] inventory usages: [" + inventoryUsages + "]");
            throw new CMSException(StatusCode.E2000);
        }
        Map<Integer, InventoryUsage> inventoryUsageMap = validInventoryUsages.stream()
                .collect(Collectors.toMap(InventoryUsage::getIndex, Function.identity()));
        for (InventoryDetail inventoryDetail: inventoryDetailList) {
            InventoryUsage inventoryUsage = inventoryUsageMap.get(inventoryDetail.getInventoryUsageIndex());
            inventoryDetail.setItemId(inventoryUsage.getItemId());
        }
        return inventoryDetailList;
    }

    public List<InventoryDetail> getAllInventoryDetailList(String clinicId, List<InventoryUsage> inventoryUsages, boolean isWithBatchNo) throws CMSException {
        Optional<Clinic> clinicOpt = clinicRepository.findById(clinicId);
        if(!clinicOpt.isPresent()){
            logger.error("clinic does not exist for: [" + clinicId + "]");
            throw new CMSException(StatusCode.E2000);
        }
        Clinic clinic = clinicOpt.get();
        List<InventoryUsage> validInventoryUsages = transformInventoryUsagesItemCode(inventoryUsages);
        logger.debug("transformed inventory usages: " + validInventoryUsages);
        //search condition
        List<InventoryDetail> inventoryDetailList = isWithBatchNo ?
                legacyInventoryRepository.getAllInventoryDetailListWithBatchNo(clinic.getClinicCode(), validInventoryUsages) :
                legacyInventoryRepository.getAllInventoryDetailList(clinic.getClinicCode(), validInventoryUsages);
        if (inventoryDetailList.size() == 0) {
            logger.error("inventory detail does not exist for clinic: [" + clinicId + "] inventory usages: [" + inventoryUsages + "]");
            throw new CMSException(StatusCode.E2000);
        }
        Map<Integer, InventoryUsage> inventoryUsageMap = validInventoryUsages.stream()
                .collect(Collectors.toMap(InventoryUsage::getIndex, Function.identity()));
        for (InventoryDetail inventoryDetail: inventoryDetailList) {
            InventoryUsage inventoryUsage = inventoryUsageMap.get(inventoryDetail.getInventoryUsageIndex());
            inventoryDetail.setItemId(inventoryUsage.getItemId());
        }
        return inventoryDetailList;
    }

    public List<DrugDispensing> createDrugDispensingList(String clinicId, List<InventoryUsage> inventoryUsages) throws CMSException {
        Optional<Clinic> clinicOpt = clinicRepository.findById(clinicId);
        if(!clinicOpt.isPresent()){
            logger.error("clinic does not exist for: [" + clinicId + "]");
            throw new CMSException(StatusCode.E2000);
        }
        Clinic clinic = clinicOpt.get();
        List<InventoryUsage> validInventoryUsages = transformInventoryUsagesItemCode(inventoryUsages);
        logger.debug("transformed inventory usages: " + validInventoryUsages);
        List<DrugDispensing> drugDispensingList = legacyInventoryRepository.createDrugDispensingList(clinic.getClinicCode(), validInventoryUsages);
        if (drugDispensingList.size() == 0) {
            logger.error("drug dispensing does not create for clinic: [" + clinicId + "] inventory usages: [" + inventoryUsages + "]");
            throw new CMSException(StatusCode.E2000);
        }
        logger.debug("drug dispensing list: " + drugDispensingList);

        return drugDispensingList;
    }
}
