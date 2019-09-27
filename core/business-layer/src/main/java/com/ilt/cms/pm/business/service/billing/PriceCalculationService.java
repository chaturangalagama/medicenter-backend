package com.ilt.cms.pm.business.service.billing;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.inventory.InventoryUsage;
import com.ilt.cms.core.entity.item.*;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeDetailResponse;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeRequest;
import com.ilt.cms.pm.business.service.inventory.LegacyInventoryService;
import com.ilt.cms.repository.spring.*;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import com.lippo.cms.container.CmsServiceResponse;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ilt.cms.core.entity.coverage.MedicalCoverage.CoverageType.*;

@Service
public class PriceCalculationService {

    private static final Logger logger = LogManager.getLogger(PriceCalculationService.class);

    private MedicalCoverageRepository medicalCoverageRepository;
    private MedicalCoverageItemRepository medicalCoverageItemRepository;
    private ItemRepository itemRepository;
    private ClinicGroupItemMasterRepository clinicGroupItemMasterRepository;
    private ClinicItemMasterRepository clinicItemMasterRepository;
    private ClinicRepository clinicRepository;
    private CaseRepository caseRepository;
    private LegacyInventoryService legacyInventoryService;

    public PriceCalculationService(MedicalCoverageRepository medicalCoverageRepository,
                                   MedicalCoverageItemRepository medicalCoverageItemRepository,
                                   ItemRepository itemRepository, ClinicGroupItemMasterRepository clinicGroupItemMasterRepository,
                                   ClinicItemMasterRepository clinicItemMasterRepository, ClinicRepository clinicRepository, CaseRepository caseRepository,
                                   LegacyInventoryService legacyInventoryService) {

        this.medicalCoverageRepository = medicalCoverageRepository;
        this.medicalCoverageItemRepository = medicalCoverageItemRepository;
        this.itemRepository = itemRepository;
        this.clinicGroupItemMasterRepository = clinicGroupItemMasterRepository;
        this.clinicItemMasterRepository = clinicItemMasterRepository;
        this.clinicRepository = clinicRepository;
        this.caseRepository = caseRepository;
        this.legacyInventoryService = legacyInventoryService;
    }

    public ItemChargeDetailResponse calculateSalesPrice(String caseId, ItemChargeRequest chargeRequests) throws CMSException {
        Case aCase = caseRepository.findById(caseId).orElseThrow(() -> {
            logger.error("Case id is invalid [" + caseId + "]");
            return new CMSException(StatusCode.E2000, "Case id is invalid");
        });
        ItemChargeDetailResponse itemChargeDetailResponse = calculateSalesPrice(aCase.getAttachedMedicalCoverages(), chargeRequests, aCase.getClinicId());
        List<ItemChargeDetail> chargeDetails = itemChargeDetailResponse.getChargeDetails();
        List<InventoryUsage> inventoryUsages = chargeDetails.stream()
                .map(itemChargeDetail -> new InventoryUsage(InventoryUsage.InventoryType.DRUG, itemChargeDetail.getItemId(), itemChargeDetail.getQuantity()))
                .collect(Collectors.toList());
        List<ItemChargeDetailResponse.InventoryData> inventoryDatas = legacyInventoryService.getOldUsage(aCase.getClinicId(), inventoryUsages);
        itemChargeDetailResponse.setInventoryData(inventoryDatas);
        return itemChargeDetailResponse;

    }

    public ItemChargeDetailResponse calculateSalesPrice(List<AttachedMedicalCoverage> attachedCoverages,
                                                              ItemChargeRequest chargeRequests, String clinicId) throws CMSException {

        logger.info("Calculating prices for Sales Items [" + chargeRequests
                + "] and attached medical coverages [" + attachedCoverages + "] and Clinic Id [" + clinicId + "]");

        CmsServiceResponse validationResult = validateRequest(chargeRequests);
        if (validationResult.getStatusCode() != StatusCode.S0000) {
            logger.error("Error occurred while validating the charging metadata creation request [" + validationResult + "]");
            throw new CMSException(validationResult.getStatusCode(), validationResult.getMessage());
        }
        return calculateCoverageAndPriceForItems(attachedCoverages, chargeRequests, clinicId);
    }


    private ItemChargeDetailResponse calculateCoverageAndPriceForItems(List<AttachedMedicalCoverage> attachedCoverages,
                                                                             ItemChargeRequest chargeRequests, String clinicId) throws CMSException {
        List<ItemChargeDetail> results = new ArrayList<>();
        List<MedicalCoverage> medicalCoverages = Collections.unmodifiableList(prioritizeMedicalCoverages(attachedCoverages));
        Clinic clinic = clinicRepository.findById(clinicId).orElseThrow(() -> {
            logger.error("invalid clinic id [" + clinicId + "]");
            return new CMSException(StatusCode.E2000, "Invalid clinic id");
        });

        for (ItemChargeDetail chargingRequest : chargeRequests.getChargeDetails()) {

            ItemChargeDetail chargingMetadata = calculateMedicalCoveragePrice(chargingRequest, medicalCoverages)
//                    .or(() -> calculatePriceByClinic(chargingRequest, clinic))
//                    .or(() -> calculatePriceByClinicGroup(chargingRequest, clinic))
                    .orElse(calculatePriceBasedOnItem(chargingRequest));

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
                            chargingRequest.getItemPriceAdjustment(), chargingRequest.getExcludedPlans()))
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
                            new Charge(clinicPrice.getPrice(), false), chargingRequest.getItemPriceAdjustment(),
                            chargingRequest.getExcludedPlans()))
                    .findFirst();
        }
    }

    private Optional<ItemChargeDetail> calculateMedicalCoveragePrice(ItemChargeDetail chargeDetail, List<MedicalCoverage> medicalCoverages) {

        logger.debug("Calculating price for the Item [{}] by Attached Coverages", chargeDetail);
        for (MedicalCoverage medicalCoverage : medicalCoverages) {
            for (CoveragePlan plan : medicalCoverage.getCoveragePlans()) {
                MedicalCoverageItem medicalCoverageItem = medicalCoverageItemRepository.findByPlanAndItemId(plan.getId(), chargeDetail.getItemId());
                if (medicalCoverageItem != null && !chargeDetail.getExcludedPlans().contains(plan.getId())) {

                    logger.info("Plan pricing found for item [" + chargeDetail.getItemId() + "] planId[" + plan.getId() + "]");
                    return Optional.of(new ItemChargeDetail(chargeDetail.getItemId(),
                            chargeDetail.getQuantity(),
                            medicalCoverageItem.getItemCoverageSchemes().stream()
                                    .filter(itemCoverageScheme -> itemCoverageScheme.getItemId().equals(chargeDetail.getItemId()))
                                    .map(ItemCoverageScheme::getPrice)
                                    .findFirst()
                                    .get(), chargeDetail.getItemPriceAdjustment(), chargeDetail.getExcludedPlans()));
                }
            }
        }
        return Optional.empty();
    }

    private ItemChargeDetail calculatePriceBasedOnItem(ItemChargeDetail chargeDetail) throws CMSException {

        Item item = itemRepository.findById(chargeDetail.getItemId())
                .orElseThrow(() -> {
                    logger.error("Item not found [" + chargeDetail + "]");
                    return new CMSException(StatusCode.E2000, "Sales item invalid");
                });
        return new ItemChargeDetail(chargeDetail.getItemId(), chargeDetail.getQuantity(), new Charge(item.getSellingPrice().getPrice(),
                item.getSellingPrice().isTaxIncluded()), chargeDetail.getItemPriceAdjustment(), chargeDetail.getExcludedPlans());
    }

    private List<MedicalCoverage> prioritizeMedicalCoverages(List<AttachedMedicalCoverage> attachedMedicalCoverages) {
        logger.info(
                "Loading Medical Coverage from attached coverage plan ids [" + attachedMedicalCoverages + "]and ordering them");
        return attachedMedicalCoverages
                .stream()
                .map(attachedCoverage -> Optional.ofNullable(medicalCoverageRepository
                        .findMedicalCoverageByPlanId(attachedCoverage.getPlanId(), Status.ACTIVE)))
                .map(optListOfCoverages -> optListOfCoverages.orElse(new ArrayList<>()))
                .flatMap(Collection::stream)
                .sorted(sortByCoverageTypePriority())
                .collect(Collectors.toList());
    }

    private Comparator<MedicalCoverage> sortByCoverageTypePriority() {
        return (o1, o2) -> {
            if (o1 != null && o1.getType() == CORPORATE && (o2.getType() == CHAS || o2.getType() == MEDISAVE)) {
                return -1;
            } else if (o1 != null && o1.getType() == INSURANCE && (o2.getType() == CHAS || o2.getType() == MEDISAVE)) {
                return -1;
            }

            if (o1 != null && (o1.getType() == CHAS || o1.getType() == MEDISAVE) && o2.getType() == CORPORATE) {
                return 1;
            } else if (o1 != null && (o1.getType() == CHAS || o1.getType() == MEDISAVE) && o2.getType() == INSURANCE) {
                return 1;
            }
            return 0;
        };
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
