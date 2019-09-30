package com.ilt.cms.pm.business.service.clinic.billing;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.item.*;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeDetailResponse;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeRequest;
import com.ilt.cms.repository.clinic.*;
import com.ilt.cms.repository.clinic.inventory.ItemRepository;
import com.lippo.cms.container.CmsServiceResponse;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PriceCalculationService {

    private static final Logger logger = LogManager.getLogger(PriceCalculationService.class);

    private ItemRepository itemRepository;
    private ClinicGroupItemMasterRepository clinicGroupItemMasterRepository;
    private ClinicItemMasterRepository clinicItemMasterRepository;
    private ClinicRepository clinicRepository;
    private LegacyInventoryService legacyInventoryService;

    public PriceCalculationService(ItemRepository itemRepository, ClinicGroupItemMasterRepository clinicGroupItemMasterRepository,
                                   ClinicItemMasterRepository clinicItemMasterRepository, ClinicRepository clinicRepository,                                    LegacyInventoryService legacyInventoryService) {
        this.itemRepository = itemRepository;
        this.clinicGroupItemMasterRepository = clinicGroupItemMasterRepository;
        this.clinicItemMasterRepository = clinicItemMasterRepository;
        this.clinicRepository = clinicRepository;
        this.legacyInventoryService = legacyInventoryService;
    }

//    public ItemChargeDetailResponse calculateSalesPrice(String caseId, ItemChargeRequest chargeRequests) throws CMSException {
//        Case aCase = caseRepository.findById(caseId).orElseThrow(() -> {
//            logger.error("Case id is invalid [" + caseId + "]");
//            return new CMSException(StatusCode.E2000, "Case id is invalid");
//        });
//        ItemChargeDetailResponse itemChargeDetailResponse = calculateSalesPrice(chargeRequests, aCase.getClinicId());
//        List<ItemChargeDetail> chargeDetails = itemChargeDetailResponse.getChargeDetails();
//        List<InventoryUsage> inventoryUsages = chargeDetails.stream()
//                .map(itemChargeDetail -> new InventoryUsage(InventoryUsage.InventoryType.DRUG, itemChargeDetail.getItemId(), itemChargeDetail.getQuantity()))
//                .collect(Collectors.toList());
//        List<ItemChargeDetailResponse.InventoryData> inventoryDatas = legacyInventoryService.getOldUsage(aCase.getClinicId(), inventoryUsages);
//        itemChargeDetailResponse.setInventoryData(inventoryDatas);
//        return itemChargeDetailResponse;
//
//    }

    public ItemChargeDetailResponse calculateSalesPrice(ItemChargeRequest chargeRequests, String clinicId) throws CMSException {

        logger.info("Calculating prices for Sales Items [" + chargeRequests + "] and Clinic Id [" + clinicId + "]");

        CmsServiceResponse validationResult = validateRequest(chargeRequests);
        if (validationResult.getStatusCode() != StatusCode.S0000) {
            logger.error("Error occurred while validating the charging metadata creation request [" + validationResult + "]");
            throw new CMSException(validationResult.getStatusCode(), validationResult.getMessage());
        }
        return calculateCoverageAndPriceForItems(chargeRequests, clinicId);
    }


    private ItemChargeDetailResponse calculateCoverageAndPriceForItems(ItemChargeRequest chargeRequests, String clinicId) throws CMSException {
        List<ItemChargeDetail> results = new ArrayList<>();
        Clinic clinic = clinicRepository.findById(clinicId).orElseThrow(() -> {
            logger.error("invalid clinic id [" + clinicId + "]");
            return new CMSException(StatusCode.E2000, "Invalid clinic id");
        });

        for (ItemChargeDetail chargingRequest : chargeRequests.getChargeDetails()) {

            ItemChargeDetail chargingMetadata = //calculateMedicalCoveragePrice(chargingRequest, medicalCoverages)
//                    .or(() -> calculatePriceByClinic(chargingRequest, clinic))
//                    .or(() -> calculatePriceByClinicGroup(chargingRequest, clinic)).orElse(
                    calculatePriceBasedOnItem(chargingRequest);

            results.add(chargingMetadata);
        }
        return new ItemChargeDetailResponse(chargeRequests.getPlanMaxUsage(), results);
    }

    private Optional<ItemChargeDetail> calculatePriceByClinicGroup(ItemChargeDetail chargingRequest, Clinic clinic) {

        logger.debug("Calculating price for the Item [{}] by the Clinic Group Master. Clinic Id [{}]", chargingRequest, clinic.getId());
        ClinicGroupItemMaster itemDetail =
                clinicGroupItemMasterRepository.findGroupPrice(clinic.getGroupName(), chargingRequest.getItemId());

        if (itemDetail == null) {
            return Optional.empty();
        } else {
            return itemDetail.getClinicGroupItemPrices().stream()
                    .filter(clinicGroupItemPrice -> clinicGroupItemPrice.getItemRefId().equals(chargingRequest.getItemId()))
                    .map(clinicGroupItemPrice -> new ItemChargeDetail(clinicGroupItemPrice.getItemRefId(), chargingRequest.getQuantity(),
                            new Charge(clinicGroupItemPrice.getPrice(), false),
                            chargingRequest.getItemPriceAdjustment()))
                    .findFirst();
        }
    }

    private Optional<ItemChargeDetail> calculatePriceByClinic(ItemChargeDetail chargingRequest, Clinic clinic) {
        logger.debug("Calculating price for the Item [{}] by the Clinic Master. Clinic Id [{}]", chargingRequest, clinic.getId());

        ClinicItemMaster clinicItemPrice = clinicItemMasterRepository.findClinicItemPrice(clinic.getId(), chargingRequest.getItemId());

        if (clinicItemPrice == null) {
            return Optional.empty();
        } else {
            return clinicItemPrice.getItemsForClinic().stream()
                    .filter(clinicItem -> clinicItem.getItemRefId().equals(chargingRequest.getItemId()))
                    .map(clinicPrice -> new ItemChargeDetail(clinicPrice.getItemRefId(), chargingRequest.getQuantity(),
                            new Charge(clinicPrice.getPrice(), false), chargingRequest.getItemPriceAdjustment()))
                    .findFirst();
        }
    }

    private ItemChargeDetail calculatePriceBasedOnItem(ItemChargeDetail chargeDetail) throws CMSException {

        Item item = itemRepository.findById(chargeDetail.getItemId())
                .orElseThrow(() -> {
                    logger.error("Item not found [" + chargeDetail + "]");
                    return new CMSException(StatusCode.E2000, "Sales item invalid");
                });
        return new ItemChargeDetail(chargeDetail.getItemId(), chargeDetail.getQuantity(), new Charge(item.getSellingPrice().getPrice(),
                item.getSellingPrice().isTaxIncluded()), chargeDetail.getItemPriceAdjustment());
    }


    private CmsServiceResponse validateRequest(ItemChargeRequest chargeRequests) {

        StatusCode statusCode = StatusCode.S0000;
        String message = "Invoice validation is a success";

        if (chargeRequests == null) {
            statusCode = StatusCode.E1005;
            message = "No purchased items found";
        } else {
            for (ItemChargeDetail itemChargeDetail : chargeRequests.getChargeDetails()) {
                if (!itemRepository.existsById(itemChargeDetail.getItemId())) {
                    statusCode = StatusCode.E2000;
                    message = "The Item Id [" + itemChargeDetail + "] cannot be found in the DB";
                    break;
                }
            }
        }
        if (statusCode != StatusCode.S0000) {
            logger.error(message);
        }
        return new CmsServiceResponse<>(statusCode, message);
    }
}
