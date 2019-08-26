package com.ilt.cms.inventory.service.purchase;

import com.ilt.cms.core.entity.Clinic;

import com.ilt.cms.core.entity.item.SellingPrice;
import com.ilt.cms.inventory.model.purchase.*;
import com.ilt.cms.inventory.model.purchase.api.OrderRequest;
import com.ilt.cms.pm.business.service.ClinicService;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.CMSConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationService.class);
    private PurchaseService purchaseService;

    private TransferService transferService;

    private ClinicService clinicService;

    public IntegrationService(PurchaseService purchaseService, TransferService transferService, ClinicService clinicService){
        this.purchaseService = purchaseService;
        this.transferService = transferService;
        this.clinicService = clinicService;
    }

    public List<OrderRequest> listOrderRequests(String clinicId, Map<OrderRequest.Filter, String> filterMap){

        List<OrderRequest> list = new ArrayList<>();

        String listSizeStr = filterMap.get(OrderRequest.Filter.LIST_SIZE);

        String type = filterMap.get(OrderRequest.Filter.TRANSFER_PURCHASE_TYPE);
        if(type == null){
            type = "NOT DEFINE";
        }
        List<Clinic> clinics = clinicService.listAll();
        Map<String, String> clinicMap = clinics.stream()
                .collect(Collectors.toMap(Clinic::getId, Clinic::getName));

        List<OrderRequest> purchaseORs = new ArrayList<>();
        List<OrderRequest> transferORs = new ArrayList<>();
        if(clinicId != null) {
            switch (type){
                case "PURCHASE":
                    purchaseORs = mapPurchaseOrderRequest(clinicId, clinicMap);
                    break;
                case "TRANSFER":
                    transferORs = mapTransferOrderRequest(clinicId, clinicMap);
                    break;
                    default:
                        purchaseORs = mapPurchaseOrderRequest(clinicId, clinicMap);
                        transferORs = mapTransferOrderRequest(clinicId, clinicMap);
            }

        }
        list.addAll(purchaseORs);
        list.addAll(transferORs);
        List<OrderRequest> OrderRequests = list.stream()
                .filter(orderRequest -> isMatch(orderRequest, filterMap))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        if(listSizeStr != null) {
            int listSize = Integer.parseInt(filterMap.get(OrderRequest.Filter.LIST_SIZE));
            OrderRequests.subList(0, listSize);
        }
        logger.info("Return [" + OrderRequests.size() + "] records");
        return OrderRequests;

    }

    private boolean isMatch(OrderRequest orderRequest, Map<OrderRequest.Filter, String> filterMap){
        List<Boolean> isMatchs = new ArrayList<>();
        filterMap.entrySet().stream().forEach(map -> {
            switch (map.getKey()){
                case STATUS:
                    isMatchs.add(map.getValue().equals(orderRequest.getStatus()));
                    break;
                case ORDER_REQUEST_NO:
                    isMatchs.add(map.getValue().equals(orderRequest.getOrderRequestNo()));
                    break;
                case RECEIVED_DATE_LESS:
                    if(orderRequest.getReceivedDateTime() == null){
                        isMatchs.add(true);
                        break;
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CMSConstant.JSON_DATE_FORMAT);
                    LocalDate date = LocalDate.parse(map.getValue(), formatter);
                    LocalTime time = LocalTime.of(23,59,59);
                    isMatchs.add(orderRequest.getReceivedDateTime().isBefore(LocalDateTime.of(date, time)));
                    break;
                case RECEIVED_DATE_MORE:
                    if(orderRequest.getReceivedDateTime() == null){
                        isMatchs.add(true);
                        break;
                    }
                    formatter = DateTimeFormatter.ofPattern(CMSConstant.JSON_DATE_FORMAT);
                    date = LocalDate.parse(map.getValue(), formatter);
                    time = LocalTime.of(0,0,0);
                    isMatchs.add(orderRequest.getReceivedDateTime().isAfter(LocalDateTime.of(date, time)));
                    break;
            }
        });
        return isMatchs.stream().allMatch(b-> b);
    }

    private OrderRequest mapOrderRequest(Map<String, String> clinicNameMap, TransferOrder order){
        OrderRequest orderRequest = new OrderRequest(clinicNameMap.get(order.getRequestClinicId()), order.getOrderNo(), order.getOrderTime(),
                null, order.getOrderStatus().name(), null, null, order, null);
        return orderRequest;
    }

    private OrderRequest mapOrderRequest(Map<String, String> clinicNameMap, PurchaseOrder order){
        OrderRequest orderRequest = new OrderRequest(clinicNameMap.get(order.getRequestClinicId()), order.getOrderNo(), order.getOrderTime(),
                new SellingPrice(order.getGoodPurchasedItems().stream()
                        .filter(goodPurchasedItem -> goodPurchasedItem.getUnitPrice() != null)
                        .mapToInt(goodPurchasedItem-> goodPurchasedItem.getUnitPrice().getPrice())
                        .sum(), false),
                order.getOrderStatus().name(), order, null, null, null);
        return orderRequest;
    }

    private OrderRequest mapOrderRequest(Map<String, String> clinicNameMap, TransferRequest request){
        OrderRequest orderRequest = new OrderRequest(clinicNameMap.get(request.getRequestClinicId()), request.getRequestNo(), request.getRequestTime(),
                null,
                request.getRequestStatus().name(), null, null, null, request);
        return orderRequest;
    }

    private OrderRequest mapOrderRequest(Map<String, String> clinicNameMap, PurchaseRequest request){
        OrderRequest orderRequest = new OrderRequest(clinicNameMap.get(request.getRequestClinicId()), request.getRequestNo(), request.getRequestTime(),
                new SellingPrice(request.getRequestItems().stream()
                        .filter(requestItem -> requestItem.getUnitPrice() != null)
                        .mapToInt(requestItem-> requestItem.getUnitPrice().getPrice())
                        .sum(), false),
                request.getRequestStatus().name(), null, request, null, null);
        return orderRequest;
    }

    public List<OrderRequest> mapPurchaseOrderRequest(String clinicId, Map<String, String> clinicMap){
        List<PurchaseOrder> purchaseOrders;
        List<PurchaseRequest> purchaseRequests;
        List<OrderRequest> orderRequests = new ArrayList<>();
        List<OrderRequest> orderRequestOrders;
        List<OrderRequest> orderRequestRequests;
        if(clinicId != null) {
            purchaseOrders = purchaseService.listPurchaseOrderByClinicId(clinicId);
            logger.info("Found [" + purchaseOrders.size() + "] purchase order by clinic["+clinicId+"]");
            purchaseRequests = purchaseService.listPurchaseRequestByClinicId(clinicId);
            logger.info("Found ["+purchaseRequests.size()+"] purchase request by clinic["+clinicId+"]");
        }else{
            purchaseRequests = purchaseService.listPurchaseRequest();
            logger.info("Found ["+purchaseRequests.size()+"] purchase request");
            purchaseOrders = purchaseService.listPurchaseOrder();
            logger.info("Found ["+purchaseOrders.size()+"] purchase order");
        }
        orderRequestOrders = purchaseOrders.stream()
                .map(purchaseOrder -> mapOrderRequest(clinicMap, purchaseOrder))
                .collect(Collectors.toList());
        orderRequestRequests = purchaseRequests.stream()
                .map(purchaseRequest -> mapOrderRequest(clinicMap, purchaseRequest))
                .collect(Collectors.toList());
        orderRequests.addAll(orderRequestOrders);
        orderRequests.addAll(orderRequestRequests);
        return orderRequests;
    }

    public List<OrderRequest> mapTransferOrderRequest(String clinicId, Map<String, String> clinicMap){
        List<TransferOrder> transferOrders;
        List<TransferRequest> transferRequests;
        List<OrderRequest> orderRequests = new ArrayList<>();
        List<OrderRequest> orderRequestOrders;
        List<OrderRequest> orderRequestRequests;
        if(clinicId != null) {
            transferOrders = transferService.listTransferOrderByClinicId(clinicId);
            logger.info("Found [" + transferOrders.size() + "] transfer order by clinic["+clinicId+"]");
            transferRequests = transferService.listTransferRequestByClinicId(clinicId);
            logger.info("Found ["+transferRequests.size()+"] transfer request by clinic["+clinicId+"]");
        }else{
            transferOrders = transferService.listTransferOrder();
            logger.info("Found ["+transferOrders.size()+"] transfer request");
            transferRequests = transferService.listTransferRequest();
            logger.info("Found ["+transferRequests.size()+"] transfer order");
        }
        orderRequestOrders = transferOrders.stream()
                .map(purchaseOrder -> mapOrderRequest(clinicMap, purchaseOrder))
                .collect(Collectors.toList());
        orderRequestRequests = transferRequests.stream()
                .map(purchaseRequest -> mapOrderRequest(clinicMap, purchaseRequest))
                .collect(Collectors.toList());
        orderRequests.addAll(orderRequestOrders);
        orderRequests.addAll(orderRequestRequests);
        return orderRequests;
    }
}
