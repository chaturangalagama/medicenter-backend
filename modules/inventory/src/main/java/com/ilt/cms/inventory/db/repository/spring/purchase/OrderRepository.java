package com.ilt.cms.inventory.db.repository.spring.purchase;

import com.ilt.cms.inventory.model.purchase.Order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findAllByRequestClinicId(String requestClinicId, Pageable page);

    @Query(value = "{'supplierId' : {$ne : null}}")
    List<Order> findPurchaseOrder(Pageable page);

    @Query(value = "{'senderClinicId' : {$ne : null}}")
    List<Order> findTransferOrder(Pageable page);

    @Query(value = "{'requestClinicId':?0, 'supplierId' : {$ne : null}}")
    List<Order> findPurchaseOrderByClinicId(String clinicId, Pageable page);

    @Query(value = "{$and : [ {$or : [{'requestClinicId':?0}, {'senderClinicId' : ?0}]}, {'senderClinicId' : {$ne : null}}]}")
    List<Order> findTransferOrderByClinicId(String clinicId, Pageable page);

}
