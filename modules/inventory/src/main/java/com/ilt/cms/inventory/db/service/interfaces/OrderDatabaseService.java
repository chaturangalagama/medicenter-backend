package com.ilt.cms.inventory.db.service.interfaces;

import com.ilt.cms.inventory.model.inventory.Inventory;
import com.ilt.cms.inventory.model.purchase.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface OrderDatabaseService {

    List<Request> findAllRequest();

    List<Request> findPurchaseRequest();

    List<Request> findTransferRequest();

    List<Request> findPurchaseRequestByClinicId(String clinicId);

    List<Request> findTransferRequestByClinicId(String clinicId);

    List<Order> findAllOrder();

    List<Order> findPurchaseOrder();

    List<Order> findTransferOrder();

    List<Order> findPurchaseOrderByClinicId(String clinicId);

    List<Order> findTransferOrderByClinicId(String clinicId);

    Optional<PurchaseRequest> findPurchaseRequestById(String purchaseRequestId);

    Optional<PurchaseOrder> findPurchaseOrderById(String purchaseOrderId);

    PurchaseRequest savePurchaseRequest(PurchaseRequest purchaseRequest);

    PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);


    TransferRequest saveTransferRequest(TransferRequest transferRequest);

    Optional<TransferRequest> findTransferRequestById(String transferRequestId);

    TransferOrder saveTransferOrder(TransferOrder transferOrder);

    Optional<TransferOrder> findTransferOrderById(String transferOrderId);
}
