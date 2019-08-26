package com.lippo.cms.inventory.rest;


import com.lippo.cms.exception.CMSException;
import com.lippo.cms.inventory.model.DrugDispensing;
import com.lippo.cms.inventory.service.LegacyInventoryService;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.lippo.cms.inventory.model.InventoryDetail;
import com.lippo.cms.inventory.model.InventoryUsage;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@RestController
@RequestMapping("/inventory")
public class InventorySystemRestController {
    private static final Logger logger = LoggerFactory.getLogger(InventorySystemRestController.class);
    private LegacyInventoryService inventoryService;

    public InventorySystemRestController(LegacyInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping("/get-usage/{clinicId}")
    HttpEntity<ApiResponse> getUsage(@PathVariable("clinicId") String clinicId, @RequestBody List<InventoryUsage> inventoryUsages) {
        logger.info("get inventory detail: clinic id [" + clinicId + "] inventory usages [" + inventoryUsages + "]");
        List<InventoryDetail> serviceResponse = null;
        try {
            serviceResponse = inventoryService.getInventoryDetailList(clinicId, inventoryUsages, false);
            return httpApiResponse(new HttpApiResponse(serviceResponse));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @RequestMapping("/get-usage-with-batch-no/{clinicId}")
    HttpEntity<ApiResponse> getUsageWithBatchNo(@PathVariable("clinicId") String clinicId, @RequestBody List<InventoryUsage> inventoryUsages) {
        logger.info("get inventory detail with batch no: clinic id [" + clinicId + "] inventory usages [" + inventoryUsages + "]");
        List<InventoryDetail> serviceResponse = null;
        try {
            serviceResponse = inventoryService.getInventoryDetailList(clinicId, inventoryUsages, true);
            return httpApiResponse(new HttpApiResponse(serviceResponse));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @RequestMapping("/get-usage/all/{clinicId}")
    HttpEntity<ApiResponse> getAllUsageWithBatchNo(@PathVariable("clinicId") String clinicId, @RequestBody List<InventoryUsage> inventoryUsages) {
        logger.info("get inventory detail: clinic id [" + clinicId + "] inventory usages [" + inventoryUsages + "]");
        List<InventoryDetail> serviceResponse = null;
        try {
            serviceResponse = inventoryService.getAllInventoryDetailList(clinicId, inventoryUsages, false);
            return httpApiResponse(new HttpApiResponse(serviceResponse));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @RequestMapping("/update-usage/{clinicId}")
    HttpEntity<ApiResponse> updateUsage(@PathVariable("clinicId") String clinicId, @RequestBody List<InventoryUsage> inventoryUsages) {
        logger.info("update inventory detail: clinic id [" + clinicId + "] inventory usages [" + inventoryUsages + "]");
        List<DrugDispensing> serviceResponse = null;
        try {
            serviceResponse = inventoryService.createDrugDispensingList(clinicId, inventoryUsages);
            return httpApiResponse(new HttpApiResponse(serviceResponse));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }
}
