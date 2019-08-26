package com.ilt.cms.inventory.db.service;

import com.ilt.cms.inventory.db.repository.spring.purchase.OrderRepository;
import com.ilt.cms.inventory.db.repository.spring.purchase.RequestRepository;
import com.ilt.cms.inventory.db.service.interfaces.OrderDatabaseService;
import com.ilt.cms.inventory.model.purchase.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class MongoOrderDatabaseService implements OrderDatabaseService {

    private final int MAX_LIST_SIZE = 100;

    private OrderRepository orderRepository;

    private RequestRepository requestRepository;

    public MongoOrderDatabaseService(OrderRepository orderRepository, RequestRepository requestRepository){
        this.orderRepository = orderRepository;
        this.requestRepository = requestRepository;
    }
    @Override
    public List<Request> findAllRequest()
    {
        return requestRepository.findAll(new Sort(Sort.Direction.DESC, "requestTime"));
    }

    @Override
    public List<Request> findPurchaseRequest(){
        Pageable pageable = PageRequest.of(0, MAX_LIST_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));
        return requestRepository.findPurchaseRequest(pageable);
    }

    @Override
    public List<Request> findTransferRequest(){
        Pageable pageable = PageRequest.of(0, MAX_LIST_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));
        return requestRepository.findTransferRequest(pageable);
    }

    @Override
    public List<Request> findPurchaseRequestByClinicId(String clinicId){
        Pageable pageable = PageRequest.of(0, MAX_LIST_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));
        return requestRepository.findPurchaseRequestByClinicId(clinicId, pageable);
    }

    @Override
    public List<Request> findTransferRequestByClinicId(String clinicId){
        Pageable pageable = PageRequest.of(0, MAX_LIST_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));
        return requestRepository.findTransferRequestByClinicId(clinicId, pageable);
    }

    @Override
    public List<Order> findAllOrder()
    {
        return orderRepository.findAll(new Sort(Sort.Direction.DESC, "orderTime"));
    }

    @Override
    public List<Order> findPurchaseOrder(){
        Pageable pageable = PageRequest.of(0, MAX_LIST_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));
        return orderRepository.findPurchaseOrder(pageable);
    }

    @Override
    public List<Order> findTransferOrder(){
        Pageable pageable = PageRequest.of(0, MAX_LIST_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));
        return orderRepository.findTransferOrder(pageable);
    }

    @Override
    public List<Order> findPurchaseOrderByClinicId(String clinicId){
        Pageable pageable = PageRequest.of(0, MAX_LIST_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));
        return orderRepository.findPurchaseOrderByClinicId(clinicId, pageable);
    }

    @Override
    public List<Order> findTransferOrderByClinicId(String clinicId){
        Pageable pageable = PageRequest.of(0, MAX_LIST_SIZE, new Sort(Sort.Direction.DESC, "createdDate"));
        return orderRepository.findTransferOrderByClinicId(clinicId, pageable);
    }

    @Override
    public Optional<PurchaseRequest> findPurchaseRequestById(String purchaseRequestId) {
        Optional<Request> requestOpt = requestRepository.findById(purchaseRequestId);
        if(requestOpt.isPresent()){
            if(requestOpt.get() instanceof PurchaseRequest){
                PurchaseRequest purchaseRequest = (PurchaseRequest) requestOpt.get();
                return Optional.of(purchaseRequest);
            }
        }
        return Optional.empty();

    }

    @Override
    public Optional<PurchaseOrder> findPurchaseOrderById(String purchaseOrderId) {
        Optional<Order> orderOpt = orderRepository.findById(purchaseOrderId);
        if(orderOpt.isPresent()){
            if(orderOpt.get() instanceof PurchaseOrder){
                PurchaseOrder purchaseOrder = (PurchaseOrder) orderOpt.get();
                return Optional.of(purchaseOrder);
            }
        }
        return Optional.empty();
    }

    @Override
    public PurchaseRequest savePurchaseRequest(PurchaseRequest purchaseRequest) {
        return requestRepository.save(purchaseRequest);
    }

    @Override
    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        return orderRepository.save(purchaseOrder);
    }

    @Override
    public TransferRequest saveTransferRequest(TransferRequest transferRequest) {
        return requestRepository.save(transferRequest);
    }

    @Override
    public Optional<TransferRequest> findTransferRequestById(String transferRequestId) {
        Optional<Request> requestOpt = requestRepository.findById(transferRequestId);
        if(requestOpt.isPresent()){
            if(requestOpt.get() instanceof TransferRequest){
                TransferRequest transferRequest = (TransferRequest) requestOpt.get();
                return Optional.of(transferRequest);
            }
        }
        return Optional.empty();
    }

    @Override
    public TransferOrder saveTransferOrder(TransferOrder transferOrder) {
        return orderRepository.save(transferOrder);
    }

    @Override
    public Optional<TransferOrder> findTransferOrderById(String transferOrderId) {
        Optional<Order> orderOpt = orderRepository.findById(transferOrderId);
        if(orderOpt.isPresent()){
            if(orderOpt.get() instanceof TransferOrder){
                TransferOrder transferOrder = (TransferOrder) orderOpt.get();
                return Optional.of(transferOrder);
            }
        }
        return Optional.empty();
    }

}
