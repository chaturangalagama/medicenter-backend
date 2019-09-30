package com.ilt.cms.pm.integration.mapper.clinic.billing;

import com.ilt.cms.api.entity.sales.*;
import com.ilt.cms.api.entity.sales.SalesOrderEntity.SalesStatus;
import com.ilt.cms.core.entity.sales.*;
import com.ilt.cms.core.entity.sales.ItemPriceAdjustment.PaymentType;
import com.ilt.cms.core.entity.item.Cost;
import com.ilt.cms.core.entity.item.SellingPrice;
import com.ilt.cms.pm.integration.mapper.Mapper;

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

    public static SalesItem mapToSalesItem(SalesItemEntity sie) {
        SalesItem salesItem = new SalesItem(); // since this is mapper removing 'SalesItem(itemMap.get(si.getItemRefId()), si)'
        if (sie == null) return salesItem;
        salesItem.setSoldPrice(sie.getOriginalTotalPrice());
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
