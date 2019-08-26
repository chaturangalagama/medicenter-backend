package com.ilt.cms.inventory.db.repository.spring.purchase;

import com.ilt.cms.inventory.model.purchase.Order;
import com.ilt.cms.inventory.model.purchase.Request;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findAllByRequestClinicId(String requestClinicId, Pageable page);

    @Query(value = "{'supplierId' : {$ne : null}}")
    List<Request> findPurchaseRequest(Pageable page);

    @Query(value = "{'senderClinicId' : {$ne : null}}")
    List<Request> findTransferRequest(Pageable page);

    @Query(value = "{'requestClinicId':'?0', 'supplierId' : {$ne : null}}")
    List<Request> findPurchaseRequestByClinicId(String clinicId, Pageable page);

    @Query(value = "{'requestClinicId':'?0','senderClinicId' : {$ne : null}}")
    List<Request> findTransferRequestByClinicId(String clinicId, Pageable page);
}
