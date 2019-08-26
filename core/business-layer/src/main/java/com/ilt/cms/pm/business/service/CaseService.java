package com.ilt.cms.pm.business.service;

import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.casem.*;
import com.ilt.cms.core.entity.casem.Case.CaseStatus;
import com.ilt.cms.core.entity.casem.Package;
import com.ilt.cms.core.entity.casem.SalesOrder.SalesStatus;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.database.casem.CaseDatabaseService;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.item.ItemDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.pm.business.service.billing.SalesOrderService;
import com.lippo.cms.container.CaseItemPriceResponse;
import com.lippo.cms.container.CaseSearchParams;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * CaseService for serve the business logic operation with database layer service of {@link CaseDatabaseService}
 */
@Service
public class CaseService {

    private static final Logger logger = LoggerFactory.getLogger(CaseService.class);

    @Value("${system.gst.value:7}")
    private int systemGstValue;

    private CaseDatabaseService caseDatabaseService;
    private ClinicDatabaseService clinicDatabaseService;
    private PatientDatabaseService patientDatabaseService;
    private RunningNumberService runningNumberService;
    private ItemDatabaseService itemDatabaseService;
    private SalesOrderService salesOrderService;

    public CaseService(CaseDatabaseService caseDatabaseService, ClinicDatabaseService clinicDatabaseService,
                       PatientDatabaseService patientDatabaseService, RunningNumberService runningNumberService,
                       ItemDatabaseService itemDatabaseService, SalesOrderService salesOrderService) {
        this.caseDatabaseService = caseDatabaseService;
        this.clinicDatabaseService = clinicDatabaseService;
        this.patientDatabaseService = patientDatabaseService;
        this.runningNumberService = runningNumberService;
        this.itemDatabaseService = itemDatabaseService;
        this.salesOrderService = salesOrderService;
    }

    public List<Case> findAllCases() {
        List<Case> all = caseDatabaseService.getAll();
        if (all != null) {
            logger.info("Returning all cases, size [{}]", all.size());
            return all;
        } else {
            logger.info("No cases found in database");
            return Collections.emptyList();
        }
    }

    public List<Case> findAllCases(String clinicId) throws CMSException {
        if(!clinicDatabaseService.exists(clinicId)){
            logger.error("Clinic id[" + clinicId + "] is not found");
            throw new CMSException(StatusCode.E2000, "Clinic not found");
        }

        List<Case> all = caseDatabaseService.getAll(clinicId);
        if (all != null) {
            logger.info("Returning all cases for clinic code [" + clinicId + "], size [" + all.size() + "]");
            return all;
        } else {
            logger.info("No cases found in database");
            return Collections.emptyList();
        }
    }


    public List<Case> findAllCases(String clinicId, CaseSearchParams sp) throws CMSException {
        if(!clinicDatabaseService.exists(clinicId)){
            logger.error("Clinic id[" + clinicId + "] is not found");
            throw new CMSException(StatusCode.E2000, "Clinic not found");
        }
        List<Case> all = caseDatabaseService.getAll(clinicId, sp);
        List<Case> finalList = new ArrayList<>();
        if (sp.getName() != null) {
            for (Case aCase : all) {
                if (patientDatabaseService.likeSearchPatient(aCase.getPatientId(), sp.getName()) != null) {
                    finalList.add(aCase);
                }
            }
        } else {
            finalList.addAll(all);
        }
        logger.info("All cases for clinic [" + clinicId + "], size [" + finalList.size() + "]");
        return finalList;
    }

    public Page<Case> findAllCases(String clinicId, int page, int size) throws CMSException {
        if(!clinicDatabaseService.exists(clinicId)){
            logger.error("Clinic id[" + clinicId + "] is not found");
            throw new CMSException(StatusCode.E2000, "Clinic not found");
        }
        Page<Case> all = caseDatabaseService.getAll(clinicId, page, size, Direction.DESC, "caseId");
        if (all != null) {
            logger.info("Returning all cases of clinic :[" + clinicId + "] Size:[" + all.getContent().size() + "]");
            return all;
        } else {
            logger.info("No cases found of clinic code:" + clinicId);
            return Page.empty();
        }
    }

    public Case findByCaseId(String caseId) throws CMSException {
        Case aCase = caseDatabaseService.findByCaseId(caseId);
        if (aCase == null) {
            logger.info("Data not found for id [" + caseId + "]");
            throw new CMSException(StatusCode.E2000, String.format("Case id %s is not found", caseId));
        }
        return aCase;
    }

    public Case createCase(Case aCase) throws CMSException {
        logger.info("New case has been saving: [" + aCase + "]");
        if (aCase.getClinicId() == null || !clinicDatabaseService.exists(aCase.getClinicId())) {
            logger.info("Clinic id not found [" + aCase.getClinicId() + "]");
            throw new CMSException(StatusCode.E2000, String.format("Clinic id %s is not found", aCase.getClinicId()));
        }
        if (aCase.getPatientId() == null || !patientDatabaseService.exists(aCase.getPatientId())) {
            logger.info("Patient id not found [{" + aCase.getPatientId() + "]");
            throw new CMSException(StatusCode.E2000, String.format("Patient id %s is not found", aCase.getPatientId()));
        }
        aCase.setStatus(CaseStatus.OPEN);
        if (aCase.getPurchasedPackage() == null) {
            aCase.setPurchasedPackage(new Package());
        }
        if (aCase.getSalesOrder() == null) {
            aCase.setSalesOrder(new SalesOrder());
        }
//        aCase.getSalesOrder().setTaxValue(systemGstValue);
        aCase.setCaseNumber(runningNumberService.generateCaseNumber());
        return caseDatabaseService.save(aCase);
    }

    public Case changeCase(String caseId, Case changedCase) throws CMSException {
        logger.info("Case is updating of id [" + caseId + "], [" + changedCase + "]");
        validateInitRequest(caseId);
        Case existCase = caseDatabaseService.findByCaseId(caseId);
        validateCaseUpdate(existCase);
        prepareSalesOrder(existCase, changedCase.getSalesOrder());
//        preparePurchasedPackage(existCase, changedCase);
        return caseDatabaseService.save(existCase);
    }

    public Case updateMedicalCoverage(String caseId, Case changedCase) throws CMSException {
        logger.info("Medical coverages about to updating of case id [" + caseId + "], with [" + changedCase.getAttachedMedicalCoverages() + "]");
        validateInitRequest(caseId);
        Case existCase = caseDatabaseService.findByCaseId(caseId);
        validateCaseUpdate(existCase);
        existCase.getAttachedMedicalCoverages().clear();
        for (AttachedMedicalCoverage attachedMedicalCoverage : changedCase.getAttachedMedicalCoverages()) {
            existCase.getAttachedMedicalCoverages().add(attachedMedicalCoverage);
        }
        return caseDatabaseService.save(existCase);
    }

    private void preparePurchasedPackage(Case existCase, Case updateCase) {
/*        Package purchasedPackage = existCase.getPurchasedPackage();
        if (Optional.ofNullable(purchasedPackage).isPresent()) {
            purchasedPackage.feedUpdate(updateCase.getPurchasedPackage());
            purchasedPackage.getDispatches()
                    .forEach(dispatch -> existCase.getSalesOrder().getPurchaseItem()
                            .forEach(salesItem -> {
                                if (salesItem.getPurchasedId().equals(dispatch.getPurchasedId())) {
                                    if (dispatch.isPayable()) {
                                        salesItem.setInvoiceable(true);
                                    } else {
                                        salesItem.setInvoiceable(false);
                                    }
                                }
                            }));
        }*/
    }

    private void prepareSalesOrder(Case existCase, SalesOrder newSo) throws CMSException {
        SalesOrder salesOrder = existCase.getSalesOrder();
        if (salesOrder == null || salesOrder.getId() == null) {
            salesOrder = salesOrderService.generateNewSalesOrder(systemGstValue);
        }
        existCase.setSalesOrder(salesOrderService.updateSalesItems(salesOrder.getId(), newSo.getPurchaseItems()));
        
  /*      newSo.getPurchaseItem().forEach(salesItem -> {
            if (salesItem.getPurchasedId() == null || salesItem.getPurchasedId().isEmpty()) {
                salesItem.setPurchasedId(CommonUtils.idGenerator());
            }
            feedItemPrices(existCase, salesItem);
            prepareItemPriceAdjustment(salesItem, salesOrder.getTaxValue());
        });
        salesOrder.setPurchaseItem(newSo.getPurchaseItem());*/
    }

    private void feedItemPrices(Case existCase, SalesItem salesItem) {
/*        try {
            List<ItemChargingMetadata> itemChargingDetails = priceCalculationService
                    .calculateSalesItemPricesWithClinic(existCase.getAttachedMedicalCoverages(),
                            Collections.singletonList(salesItem), existCase.getClinicId());
            if (!itemChargingDetails.isEmpty()) {
                int price = itemChargingDetails.get(0).getCharge().getPrice();
                boolean taxIncluded = itemChargingDetails.get(0).getCharge().isTaxIncluded();
                SellingPrice unitPrice = new SellingPrice(price, taxIncluded);
                salesItem.setUnitPrice(unitPrice);
            } else {
                salesItem.setUnitPrice(new SellingPrice());
            }
        } catch (CMSException e) {
            logger.error("ERROR while getting item price", e);
        }*/
    }

    private void prepareItemPriceAdjustment(SalesItem salesItem, int taxValue) {
/*        int itemTotalPriceEntered = salesItem.getOriginalTotalPrice();
        int itemTotalPrice = salesItem.getUnitPrice().getPriceWithTax(taxValue) * salesItem.getPurchaseQty();

        int adjustedValue = itemTotalPriceEntered - itemTotalPrice;
        if (!salesItem.getUnitPrice().isTaxIncluded()) {
            adjustedValue = adjustedValue - Calculations.calculatePercentageHalfUpRound(taxValue, adjustedValue);
        }
        adjustedValue = new BigDecimal((double) adjustedValue / salesItem.getPurchaseQty()).setScale(0, RoundingMode.HALF_UP).intValue();
        if (salesItem.getUnitPrice().getPrice() + adjustedValue >= salesItem.getCost().getPrice()) {
            salesItem.setItemPriceAdjustment(new ItemPriceAdjustment(adjustedValue));
        } else {
            logger.info("Ignoring the item adjusted amount, because it's less than the cost : " + salesItem.getOriginalTotalPrice());
        }*/
    }

    public Package getCasePackage(String caseId) throws CMSException {
        logger.info("Package details generating for case [" + caseId + "]");
        validateInitRequest(caseId);
        Case aCase = caseDatabaseService.getCasePackage(caseId);
        return aCase.getPurchasedPackage();
    }

    public Case addPurchasedPackage(String caseId, String packageItemId) throws CMSException {
        logger.info("Package details generating for case id [" + caseId + "]");
        validateInitRequest(caseId);
        Case aCase = caseDatabaseService.findByCaseId(caseId);
        validateCaseUpdate(aCase);
        addPackageToCase(packageItemId, aCase);
        logger.info("Package details saving for case [" + aCase + "]");
        return caseDatabaseService.save(aCase);
    }

    private void addPackageToCase(String packageItemId, Case aCase) throws CMSException {
        Package existPackage = aCase.getPurchasedPackage();
        SalesOrder salesOrder = aCase.getSalesOrder();
        if (existPackage.getItemRefId() == null) {
            Optional<Item> itemOpt = itemDatabaseService.findById(packageItemId);
            if (!itemOpt.isPresent()) {
                logger.info("A package item not available in item code + [" + packageItemId + "]");
                throw new CMSException(StatusCode.E1009, "Package item not found for item id " + packageItemId);

            }
            Item item = itemOpt.get();
            existPackage.setItemRefId(item.getId());
            existPackage.setCode(item.getCode());
            existPackage.setName(item.getName());
            existPackage.setExpireDate(LocalDateTime.now().plusMonths(6));
            existPackage.setPackageQty(item.getSubItems().size());

/*            item.getSubItems().forEach(subItems -> {
                Item subItem = subItems.getItem();
                if (subItem != null) {
                    String purchasedId = CommonUtils.idGenerator();
                    existPackage.getDispatches().add(new Dispatch(subItem.getId(), subItem.getCode(), subItem.getName(), purchasedId));
                    SalesItem salesItem = new SalesItem(subItem, purchasedId);
                    feedItemPrices(aCase, salesItem);
                    salesOrder.getPurchaseItem().add(salesItem);
                    existPackage.setPurchasePrice(existPackage.getPurchasePrice() + salesItem.getUnitPrice().getPrice());
                }
            });*/
        } else {
            logger.info("A purchased package already available for case id + [" + aCase.getId() + "]");
            throw new CMSException(StatusCode.E1009, "Purchased package already available for case id " + aCase.getId());
        }
        aCase.setSalesOrder(salesOrder);
        aCase.setPurchasedPackage(existPackage);
    }

    public SalesOrder getSalesOrder(String caseId) throws CMSException {
        logger.info("Sales order details generating for case id [" + caseId + "]");
        validateInitRequest(caseId);
        Case aCase = caseDatabaseService.findByCaseId(caseId);
        return aCase.getSalesOrder();
    }

    public SalesOrder updateSalesOrder(String caseId, SalesOrder salesOrder) throws CMSException {
        logger.info("Sales order details generating for case id [" + caseId + "]");
        validateInitRequest(caseId);
        Case existCase = caseDatabaseService.findByCaseId(caseId);
        validateCaseUpdate(existCase);
        prepareSalesOrder(existCase, salesOrder);
        existCase.setSalesOrder(existCase.getSalesOrder());
        return caseDatabaseService.save(existCase).getSalesOrder();
    }

    public Case closeCase(String caseId) throws CMSException {
        logger.info("Closing the case for case id [" + caseId + "]");
        validateInitRequest(caseId);
        Case existCase = caseDatabaseService.findByCaseId(caseId);
        validateCaseClose(existCase);
        existCase.setStatus(CaseStatus.CLOSED);
        existCase.getSalesOrder().setStatus(SalesStatus.CLOSED);
        return caseDatabaseService.save(existCase);
    }

    public Case updateCaseSingleVisit(String caseId, String state) throws CMSException {
        logger.info("Changing case to single visit for case id [" + caseId + "]");
        validateInitRequest(caseId);
        Case existCase = caseDatabaseService.findByCaseId(caseId);
        validateCaseUpdate(existCase);
        if (state != null && "1".equals(state.trim())) {
            existCase.setSingleVisit(true);
        } else if (state != null && "0".equals(state.trim())) {
            existCase.setSingleVisit(false);
        }
        return caseDatabaseService.save(existCase);
    }

    public CaseItemPriceResponse calculatePurchaseItemPrices(String caseId, ItemChargeDetail.ItemChargeRequest salesItems) throws CMSException {

        logger.info("Temporary price calculation for case id [" + caseId + "], salesItems : " + salesItems);
        validateInitRequest(caseId);
        Case aCase = caseDatabaseService.findByCaseId(caseId);
        CaseItemPriceResponse itemPriceResponse = new CaseItemPriceResponse();

        int totalPrice = 0;
/*        List<ItemChargingMetadata> chargingMetadata = priceCalculationService
                .calculateSalesItemPricesWithClinic(aCase.getAttachedMedicalCoverages(), salesItems, aCase.getClinicId());
        for (SalesItem salesItem : salesItems) {
            for (ItemChargingMetadata metadata : chargingMetadata) {
                if (salesItem.getPurchasedId().equals(metadata.getPurchasedId())) {
                    int unitPrice = metadata.getCharge().getPrice();
                    int calculateGst = 0;
                    if (!metadata.getCharge().isTaxIncluded()) {
                        calculateGst = invoiceCalculationService.calculateGst(unitPrice);
                        totalPrice += calculateGst;
                    }
                    totalPrice += unitPrice;
                    ItemPrice itemPrice = new ItemPrice(metadata.getItemId(), metadata.getPurchasedId(), unitPrice, calculateGst);
                    itemPrice.getInventoryData().add(new InventoryData("1000", LocalDate.of(2020, 1, 1)));
                    itemPrice.getInventoryData().add(new InventoryData("1001", LocalDate.of(2020, 2, 1)));
                    itemPrice.getInventoryData().add(new InventoryData("1002", LocalDate.of(2020, 3, 1)));
                    itemPriceResponse.getItemPrices().add(itemPrice);
                }
            }
        }
        itemPriceResponse.setTotalPrice(totalPrice);
        itemPriceResponse.setTotalPaid(invoiceCalculationService.getTotalPaidAmount(aCase));*/
        logger.info("Sending case item price data: " + itemPriceResponse);
        return itemPriceResponse;
    }

    public void validateInitRequest(String caseId) throws CMSException {
        if (caseId == null) {
            logger.info("Data not found due case id null");
            throw new CMSException(StatusCode.E2004, "Case id is null");
        }
        if (!caseDatabaseService.exists(caseId)) {
            logger.info("Case data not found for id [" + caseId + "]");
            throw new CMSException(StatusCode.E2000, String.format("Case id %s is not exist", caseId));
        }
    }

    public void validateCaseUpdate(Case aCase) throws CMSException {
        if (CaseStatus.CLOSED.equals(aCase.getStatus())) {
            logger.info("Cannot proceed to update the case due already in CLOSED state");
            throw new CMSException(StatusCode.E1009, "Cannot update, case in CLOSED state");
        }
    }

    public void validateCaseClose(Case aCase) throws CMSException {
        validateCaseUpdate(aCase);

        for (Dispatch dispatch : aCase.getPurchasedPackage().getDispatches()) {
            if (!dispatch.isUtilize()) {
                logger.info("Cannot close the case, the purchased packages are not been fully utilized");
                throw new CMSException(StatusCode.E1009, "Purchased item not fully utilized in case " + aCase.getId());
            }
        }
    }
}
