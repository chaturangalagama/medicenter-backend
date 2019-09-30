package com.ilt.cms.pm.business.service.clinic.billing;

import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.inventory.InventoryDetail;
import com.ilt.cms.core.entity.inventory.InventoryUsage;
import com.lippo.cms.container.CmsServiceResponse;
import com.lippo.commons.web.api.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LegacyInventoryService {
    private static final Logger logger = LogManager.getLogger(LegacyInventoryService.class);

//    @Value("${legacy.api.all.usage.batchno}")
    private String legacyApiAllUsageBatch;

//    @Value("${legacy.api.all.usage}")
    private String legacyApiAllUsage;

//    @Value("${legacy.api.usage.batchno}")
    private String legacyApiUsageBatch;

//    @Value("${legacy.api.usage}")
    private String legacyApiUsage;

    private RestTemplate restTemplate;

    public LegacyInventoryService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    /*new*/
    public List<ItemChargeDetail.ItemChargeDetailResponse.InventoryData> getAllUsage(String clinicId, List<InventoryUsage> inventoryUsages){
        HttpEntity<ApiResponse> serviceResponse = restTemplate.postForObject(legacyApiAllUsage, inventoryUsages, HttpEntity.class, clinicId);
        if(serviceResponse != null && serviceResponse.hasBody()) {
            ApiResponse body = serviceResponse.getBody();
            if(body.getPayload() != null) {
                List<InventoryDetail> InventoryDetails = (List<InventoryDetail>) body.getPayload();
                return InventoryDetails.stream().map(inventoryDetail -> mapToInventoryData(inventoryDetail)).collect(Collectors.toList());
            }else{
                logger.error("Cannot get inventory, status["+body.getStatusCode()+"], message:["+body.getMessage()+"]");
            }
        }else{
            logger.error("Cannot get inventory, response["+serviceResponse+"]");
        }
        return new ArrayList<>();
    }

    /*existing*/
    public List<ItemChargeDetail.ItemChargeDetailResponse.InventoryData> getOldUsage(String clinicId, List<InventoryUsage> inventoryUsages){
        CmsServiceResponse<List<InventoryDetail>> serviceResponse = restTemplate
                .postForObject(legacyApiUsage, inventoryUsages, CmsServiceResponse.class, clinicId);
        if(serviceResponse != null) {
            if(serviceResponse.getPayload() != null) {
                List<InventoryDetail> InventoryDetails = (List<InventoryDetail>) serviceResponse.getPayload();
                return InventoryDetails.stream().map(inventoryDetail -> mapToInventoryData(inventoryDetail)).collect(Collectors.toList());
            }else{
                logger.error("Cannot get inventory, status["+serviceResponse.getStatusCode()+"], message:["+serviceResponse.getMessage()+"]");
            }
        }else{
            logger.error("Cannot get inventory, response["+serviceResponse+"]");
        }
        return new ArrayList<>();
    }

    private ItemChargeDetail.ItemChargeDetailResponse.InventoryData mapToInventoryData(InventoryDetail inventoryDetail){
        ItemChargeDetail.ItemChargeDetailResponse.InventoryData inventoryData = new ItemChargeDetail.ItemChargeDetailResponse.InventoryData();
        inventoryData.setInventoryDetailId(inventoryDetail.getInventoryDetailId());
        inventoryData.setInventoryId(inventoryDetail.getInventoryId());
        inventoryData.setClinicId(inventoryDetail.getClinicId());
        inventoryData.setChargeId(inventoryDetail.getChargeId());
        if(inventoryDetail.getExpiryDate() != null) {
            inventoryData.setExpireDate(inventoryDetail.getExpiryDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        inventoryData.setBatchNo(inventoryDetail.getBatchNo());
        inventoryData.setQuantity(inventoryDetail.getQuantity());
        inventoryData.setQuantityTotal(inventoryDetail.getQuantityTotal());
        inventoryData.setAmount(inventoryDetail.getAmount());
        inventoryData.setItemAmount(inventoryDetail.getItemAmount());
        inventoryData.setItemCost(inventoryDetail.getItemCost());
        inventoryData.setRemainingQuantity(inventoryDetail.getRemainingQuantity());
        inventoryData.setRemainingAmount(inventoryDetail.getRemainingAmount());
        if(inventoryDetail.getCreatedAt() != null){
            inventoryData.setCreatedAt(inventoryDetail.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        inventoryData.setCreatedUserId(inventoryDetail.getCreatedUserId());
        if(inventoryDetail.getUpdatedAt() != null){
            inventoryData.setUpdatedAt(inventoryDetail.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        inventoryData.setDrugCode(inventoryDetail.getDrugCode());
        inventoryData.setTotalAmount(inventoryDetail.getTotalAmount());
        inventoryData.setTotalQuantity(inventoryDetail.getTotalQuantity());
        inventoryData.setTotalAmount(inventoryDetail.getTotalAmount());
        inventoryData.setCost(inventoryDetail.getCost());
        inventoryData.setMixingFlag(inventoryDetail.isMixingFlag());
        inventoryData.setItemId(inventoryDetail.getItemId());
        inventoryData.setInventoryUsageIndex(inventoryDetail.getInventoryUsageIndex());
        inventoryData.setPatientRegisterDetailId(inventoryDetail.getPatientRegisterDetailId());

        return inventoryData;

    }
}
