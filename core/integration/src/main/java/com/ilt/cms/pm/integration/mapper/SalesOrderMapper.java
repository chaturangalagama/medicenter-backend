package com.ilt.cms.pm.integration.mapper;

import com.ilt.cms.api.entity.casem.*;
import com.ilt.cms.api.entity.casem.ClaimEntity.ClaimStatus;
import com.ilt.cms.api.entity.casem.SalesOrderEntity.SalesStatus;
import com.ilt.cms.core.entity.casem.*;
import com.ilt.cms.core.entity.casem.Claim.AppealRejection;
import com.ilt.cms.core.entity.casem.ItemPriceAdjustment.PaymentType;
import com.ilt.cms.core.entity.item.Cost;
import com.ilt.cms.core.entity.item.SellingPrice;

import java.util.stream.Collectors;

public class SalesOrderMapper extends Mapper {

    /**
     * Converting {@link SalesOrderEntity} to {@link SalesOrder} which converting to received data from rest api to model
     *
     * @param soe
     * @return {@link SalesOrder}
     */
    public static SalesOrder mapToSalesOrder(SalesOrderEntity soe) {
        SalesOrder so = new SalesOrder();
        if (soe == null) return so;
        so = SalesOrder.newSalesOrder(soe.getTaxValue(),soe.getSalesRefNo());
        if (SalesStatus.OPEN.equals(soe.getStatus())) {
            so.setStatus(SalesOrder.SalesStatus.OPEN);
        } else if (SalesStatus.CLOSED.equals(soe.getStatus())) {
            so.setStatus(SalesOrder.SalesStatus.CLOSED);
        }
        if (soe.getInvoices() != null) {
            so.setInvoices(soe.getInvoices().stream().map(SalesOrderMapper::mapToInvoiceCore).collect(Collectors.toList()));
        }
        if (soe.getPurchaseItem() != null) {
            so.setPurchaseItems(soe.getPurchaseItem().stream().map(SalesOrderMapper::mapToSalesItem).collect(Collectors.toList()));
        }
        return so;
    }

    /**
     * Converting {@link SalesOrder} to {@link SalesOrderEntity} which converting to send data from service to rest layer
     *
     * @param salesOrder
     * @return {@link SalesOrder}
     */
    public static SalesOrderEntity mapToSalesOrderEntity(SalesOrder salesOrder) {
        SalesOrderEntity salesOrderEntity = new SalesOrderEntity();
        if (salesOrder == null) return salesOrderEntity;
        if (SalesOrder.SalesStatus.OPEN.equals(salesOrder.getStatus())) {
            salesOrderEntity.setStatus(SalesStatus.OPEN);
        } else if (SalesOrder.SalesStatus.CLOSED.equals(salesOrder.getStatus())) {
            salesOrderEntity.setStatus(SalesStatus.CLOSED);
        }
        if (salesOrder.getInvoices() != null) {
            salesOrderEntity.setInvoices(salesOrder.getInvoices().stream().map(SalesOrderMapper::mapToInvoiceEntity).collect(Collectors.toList()));
        }
        if (salesOrder.getPurchaseItems() != null) {
            salesOrderEntity.setPurchaseItem(salesOrder.getPurchaseItems()
                    .stream()
                    .map(salesItem -> mapToSalesItemEntity(salesItem, salesOrder.getTaxValue()))
                    .collect(Collectors.toList()));
        }

        salesOrderEntity.setTaxValue(salesOrder.getTaxValue());
//        salesOrderEntity.setSalesRefNo(salesOrder.getSalesRefNo());
//        salesOrderEntity.setTotalPrice(salesOrder.totalPrice());
//        salesOrderEntity.setTotalPayableTax(salesOrder.totalPayableTax());
//        salesOrderEntity.setTotalPaid(salesOrder.totalPaid());
//        salesOrderEntity.setOutstanding((salesOrder.totalPrice() - salesOrder.totalPaid()));
        return salesOrderEntity;
    }

    private static Invoice mapToInvoiceCore(InvoiceEntity ie) {
        Invoice invoice = new Invoice();
        if (ie == null) return invoice;
        invoice.setInvoiceNumber(ie.getInvoiceId());
        invoice.setTaxAmount(ie.getIncludedTaxAmount());
        invoice.setInvoiceTime(ie.getInvoiceTime());
        invoice.setPaidTime(ie.getPaidTime());
        invoice.setPaidAmount(ie.getPaidAmount());
        invoice.setPayableAmount(ie.getPayableAmount());
        invoice.setPlanId(ie.getPlanId());
//        invoice.setReference(ie.getReference());
        invoice.setClaim(mapToClaimCore(ie.getClaim()));
        setPaymentModeToCore(ie, invoice);
        return invoice;
    }

    private static InvoiceEntity mapToInvoiceEntity(Invoice invoice) {
        InvoiceEntity ie = new InvoiceEntity();
        if (invoice == null) return ie;
        ie.setInvoiceId(invoice.getInvoiceNumber());
        ie.setIncludedTaxAmount(invoice.getTaxAmount());
        ie.setInvoiceTime(invoice.getInvoiceTime());
        ie.setPaidAmount(invoice.getPaidAmount());
        ie.setPayableAmount(invoice.getPayableAmount());
        ie.setPaidTime(invoice.getPaidTime());
        ie.setPlanId(invoice.getPlanId());
//        ie.setReference(invoice.getReference());
        if (invoice.getClaim() != null){
            ie.setClaim(mapToClaimCoreEntity(invoice.getClaim()));
        }
        setEnumStatesToEntity(invoice, ie);
        return ie;
    }

    private static void setPaymentModeToCore(InvoiceEntity ie, Invoice invoice) {
        invoice.setPaymentInfos(ie.getPaymentInfos());
    }

    private static void setEnumStatesToEntity(Invoice invoice, InvoiceEntity ie) {
        ie.setPaymentInfos(invoice.getPaymentInfos());

        ie.setInvoiceState(invoice.getInvoiceState());
        ie.setInvoiceType(invoice.getInvoiceType());
    }

    private static Claim mapToClaimCore(ClaimEntity ce) {
        Claim claim = new Claim();
        if (ce == null) return claim;
        if (ce.getAppealRejections() != null) {
            claim.setAppealRejections(ce.getAppealRejections().stream().map(SalesOrderMapper::mapToAppealRejection).collect(Collectors.toList()));
        }
        claim.setAttendingDoctorId(ce.getAttendingDoctorId());
        claim.setClaimDoctorId(ce.getClaimDoctorId());
        claim.setClaimId(ce.getClaimId());
        setClaimStatus(ce, claim);
        claim.setDiagnosisCodes(ce.getDiagnosisCodes());
        claim.setGstAmount(ce.getGstAmount());
        claim.setClaimExpectedAmt(ce.getClaimExpectedAmt());
        claim.setConsultationAmt(ce.getConsultationAmt());
        claim.setMedicalTestAmt(ce.getMedicalTestAmt());
        claim.setMedicationAmt(ce.getMedicationAmt());
        claim.setOtherAmt(ce.getOtherAmt());
        claim.setClaimResult(mapToClaimResult(ce.getClaimResult()));
        claim.setPaidResult(mapToClaimResult(ce.getPaidResult()));
        claim.setPayersName(ce.getPayersName());
        claim.setPayersNric(ce.getPayersNric());
        claim.setRemark(ce.getRemark());
        claim.setSubmissionDateTime(ce.getSubmissionDateTime());
        claim.setSubmissionResult(mapToSubmissionResult(ce.getSubmissionResult()));
        return claim;
    }

    private static ClaimEntity mapToClaimCoreEntity(Claim ce) {
        ClaimEntity claim = new ClaimEntity();
        if (ce == null) return claim;
        if (ce.getAppealRejections() != null) {
            claim.setAppealRejections(ce.getAppealRejections().stream().map(SalesOrderMapper::mapToAppealRejectionEntity).collect(Collectors.toList()));
        }
        claim.setAttendingDoctorId(ce.getAttendingDoctorId());
        claim.setClaimDoctorId(ce.getClaimDoctorId());
        claim.setClaimId(ce.getClaimId());
        setClaimStatusToEntity(ce, claim);
        claim.setDiagnosisCodes(ce.getDiagnosisCodes());
        claim.setGstAmount(ce.getGstAmount());
        claim.setClaimExpectedAmt(ce.getClaimExpectedAmt());
        claim.setConsultationAmt(ce.getConsultationAmt());
        claim.setMedicalTestAmt(ce.getMedicalTestAmt());
        claim.setMedicationAmt(ce.getMedicationAmt());
        claim.setOtherAmt(ce.getOtherAmt());
        claim.setClaimResult(mapToClaimResultEntity(ce.getClaimResult()));
        claim.setPaidResult(mapToClaimResultEntity(ce.getPaidResult()));
        claim.setPayersName(ce.getPayersName());
        claim.setPayersNric(ce.getPayersNric());
        claim.setRemark(ce.getRemark());
        claim.setSubmissionDateTime(ce.getSubmissionDateTime());
        claim.setSubmissionResult(mapToSubmissionResultEntity(ce.getSubmissionResult()));
        return claim;
    }

    private static void setClaimStatus(ClaimEntity ce, Claim claim) {
        if (ClaimStatus.APPEALED.equals(ce.getClaimStatus())) {
            claim.setClaimStatus(Claim.ClaimStatus.APPEALED);
        } else if (ClaimStatus.APPROVED.equals(ce.getClaimStatus())) {
            claim.setClaimStatus(Claim.ClaimStatus.APPROVED);
        } else if (ClaimStatus.FAILED.equals(ce.getClaimStatus())) {
            claim.setClaimStatus(Claim.ClaimStatus.FAILED);
        } else if (ClaimStatus.PAID.equals(ce.getClaimStatus())) {
            claim.setClaimStatus(Claim.ClaimStatus.PAID);
        } else if (ClaimStatus.PENDING.equals(ce.getClaimStatus())) {
            claim.setClaimStatus(Claim.ClaimStatus.PENDING);
        } else if (ClaimStatus.REJECTED.equals(ce.getClaimStatus())) {
            claim.setClaimStatus(Claim.ClaimStatus.REJECTED);
        } else if (ClaimStatus.REJECTED_PERMANENT.equals(ce.getClaimStatus())) {
            claim.setClaimStatus(Claim.ClaimStatus.REJECTED_PERMANENT);
        } else if (ClaimStatus.SUBMITTED.equals(ce.getClaimStatus())) {
            claim.setClaimStatus(Claim.ClaimStatus.SUBMITTED);
        }
    }

    private static void setClaimStatusToEntity(Claim claim, ClaimEntity ce) {
        if (Claim.ClaimStatus.APPEALED.equals(claim.getClaimStatus())) {
            ce.setClaimStatus(ClaimStatus.APPEALED);
        } else if (Claim.ClaimStatus.APPROVED.equals(claim.getClaimStatus())) {
            ce.setClaimStatus(ClaimStatus.APPROVED);
        } else if (Claim.ClaimStatus.FAILED.equals(claim.getClaimStatus())) {
            ce.setClaimStatus(ClaimStatus.FAILED);
        } else if (Claim.ClaimStatus.PAID.equals(claim.getClaimStatus())) {
            ce.setClaimStatus(ClaimStatus.PAID);
        } else if (Claim.ClaimStatus.PENDING.equals(claim.getClaimStatus())) {
            ce.setClaimStatus(ClaimStatus.PENDING);
        } else if (Claim.ClaimStatus.REJECTED.equals(claim.getClaimStatus())) {
            ce.setClaimStatus(ClaimStatus.REJECTED);
        } else if (Claim.ClaimStatus.REJECTED_PERMANENT.equals(claim.getClaimStatus())) {
            ce.setClaimStatus(ClaimStatus.REJECTED_PERMANENT);
        } else if (Claim.ClaimStatus.SUBMITTED.equals(claim.getClaimStatus())) {
            ce.setClaimStatus(ClaimStatus.SUBMITTED);
        }
    }

    private static Claim.AppealRejection mapToAppealRejection(ClaimEntity.AppealRejection appealRejection) {
        if (appealRejection == null) return new AppealRejection();
        return new AppealRejection(appealRejection.getReason());
    }

    private static ClaimEntity.AppealRejection mapToAppealRejectionEntity(Claim.AppealRejection appealRejection) {
        if (appealRejection == null) return new ClaimEntity.AppealRejection();
        return new ClaimEntity.AppealRejection(appealRejection.getReason());
    }

    private static Claim.ClaimResult mapToClaimResult(ClaimEntity.ClaimResult claimResult) {
        Claim.ClaimResult claimResultCore = new Claim.ClaimResult();
        if (claimResult == null) return claimResultCore;
        claimResultCore.setAmount(claimResult.getAmount());
        claimResultCore.setReferenceNumber(claimResult.getReferenceNumber());
        claimResultCore.setRemark(claimResult.getRemark());
        claimResultCore.setResultDateTime(claimResult.getResultDateTime());
        claimResultCore.setStatusCode(claimResult.getStatusCode());
        return claimResultCore;
    }

    private static ClaimEntity.ClaimResult mapToClaimResultEntity(Claim.ClaimResult claimResult) {
        ClaimEntity.ClaimResult claimResultCore = new ClaimEntity.ClaimResult();
        if (claimResult == null) return claimResultCore;
        claimResultCore.setAmount(claimResult.getAmount());
        claimResultCore.setReferenceNumber(claimResult.getReferenceNumber());
        claimResultCore.setRemark(claimResult.getRemark());
        claimResultCore.setResultDateTime(claimResult.getResultDateTime());
        claimResultCore.setStatusCode(claimResult.getStatusCode());
        return claimResultCore;
    }

    private static Claim.SubmissionResult mapToSubmissionResult(ClaimEntity.SubmissionResult submissionResult) {
        if (submissionResult == null) return new Claim.SubmissionResult();
        return new Claim.SubmissionResult(
                submissionResult.getClaimNo(),
                submissionResult.getStatusCode(),
                submissionResult.getStatusDescription());
    }

    private static ClaimEntity.SubmissionResult mapToSubmissionResultEntity(Claim.SubmissionResult submissionResult) {
        if (submissionResult == null) return new ClaimEntity.SubmissionResult();
        return new ClaimEntity.SubmissionResult(
                submissionResult.getClaimNo(),
                submissionResult.getStatusCode(),
                submissionResult.getStatusDescription());
    }

    public static SalesItem mapToSalesItem(SalesItemEntity sie) {
        SalesItem salesItem = new SalesItem(); // since this is mapper removing 'SalesItem(itemMap.get(si.getItemRefId()), si)'
        if (sie == null) return salesItem;
        salesItem.setSoldPrice(sie.getOriginalTotalPrice());
        salesItem.setExcludedCoveragePlanIds(sie.getExcludedCoveragePlanIds());
        salesItem.setItemRefId(sie.getItemRefId());
        salesItem.setPurchaseQty(sie.getPurchaseQty());
        salesItem.setDosage(sie.getDosage());
        salesItem.setDuration(sie.getDuration());
        salesItem.setInstruct(sie.getInstruct());
        salesItem.setBatchNo(sie.getBatchNo());
        salesItem.setExpireDate(sie.getExpireDate());
        salesItem.setPurchaseUom(sie.getPurchaseUom());
        salesItem.setRemarks(sie.getRemarks());

        salesItem.setCost(mapToCost(sie.getCost()));
        salesItem.setSoldPrice(mapToSellingPrice(sie.getUnitPrice()).getPrice());
        salesItem.setItemPriceAdjustment(mapToCore(sie.getPriceAdjustment()));
        return salesItem;
    }

    private static SalesItemEntity mapToSalesItemEntity(SalesItem salesItem, int gst) {
        SalesItemEntity sie = new SalesItemEntity();
        if (salesItem == null) return sie;
        sie.setOriginalTotalPrice(salesItem.getSoldPrice());
        sie.setExcludedCoveragePlanIds(salesItem.getExcludedCoveragePlanIds());
        sie.setCost(mapToCost(salesItem.getCost()));
        sie.setItemRefId(salesItem.getItemRefId());
        sie.setPurchaseQty(salesItem.getPurchaseQty());
        sie.setDosage(salesItem.getDosage());
        sie.setDuration(salesItem.getDuration());
        sie.setInstruct(salesItem.getInstruct());
        sie.setBatchNo(salesItem.getBatchNo());
        sie.setExpireDate(salesItem.getExpireDate());
        sie.setPurchaseUom(salesItem.getPurchaseUom());
        sie.setRemarks(salesItem.getRemarks());
//        if (salesItem.getSubItems() != null) {
//            sie.setSubItems(salesItem.getSubItems()
//                    .stream()
//                    .map(salesSubItem -> SalesOrderMapper.mapToSalesItemEntity(salesSubItem, gst))
//                    .collect(Collectors.toList()));
//        }
        sie.setUnitPrice(mapToSellingPriceEntity(new SellingPrice(salesItem.getSoldPrice(),false)));
        sie.setPriceAdjustment(mapToEntity(salesItem.getItemPriceAdjustment()));
        return sie;
    }

    public static ItemPriceAdjustmentEntity mapToEntity(ItemPriceAdjustment priceAdjustment) {
        ItemPriceAdjustmentEntity entity = new ItemPriceAdjustmentEntity();
        if (priceAdjustment == null) return entity;
        entity.setAdjustedValue(priceAdjustment.getAdjustedValue());
        if (PaymentType.DOLLAR.equals(priceAdjustment.getPaymentType())) {
            entity.setPaymentType(ItemPriceAdjustmentEntity.PaymentType.DOLLAR);
        } else if (PaymentType.PERCENTAGE.equals(priceAdjustment.getPaymentType())) {
            entity.setPaymentType(ItemPriceAdjustmentEntity.PaymentType.PERCENTAGE);
        }
        return entity;
    }

    public static ItemPriceAdjustment mapToCore(ItemPriceAdjustmentEntity entity) {
        ItemPriceAdjustment priceAdjustment = new ItemPriceAdjustment();
        if (entity == null) return priceAdjustment;
        priceAdjustment.setAdjustedValue(entity.getAdjustedValue());
        if (ItemPriceAdjustmentEntity.PaymentType.DOLLAR.equals(entity.getPaymentType())) {
            priceAdjustment.setPaymentType(ItemPriceAdjustment.PaymentType.DOLLAR);
        } else if (ItemPriceAdjustmentEntity.PaymentType.PERCENTAGE.equals(entity.getPaymentType())) {
            priceAdjustment.setPaymentType(PaymentType.PERCENTAGE);
        }
        return priceAdjustment;
    }

    private static Cost mapToCost(CostEntity costEntity) {
        if (costEntity == null) return new Cost();
        return new Cost(costEntity.getPrice(), costEntity.isTaxIncluded());
    }

    private static CostEntity mapToCost(Cost cost) {
        if (cost == null) return new CostEntity();
        return new CostEntity(cost.getPrice(), cost.isTaxIncluded());
    }

    private static SellingPrice mapToSellingPrice(SellingPriceEntity sellingPriceEntity) {
        if (sellingPriceEntity == null) return new SellingPrice();
        return new SellingPrice(sellingPriceEntity.getPrice(), sellingPriceEntity.isTaxIncluded());
    }

    private static SellingPriceEntity mapToSellingPriceEntity(SellingPrice sellingPriceEntity) {
        if (sellingPriceEntity == null) return new SellingPriceEntity();
        return new SellingPriceEntity(sellingPriceEntity.getPrice(), sellingPriceEntity.isTaxIncluded());
    }
}
