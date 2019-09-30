package com.ilt.cms.repository.clinic.inventory;

import com.ilt.cms.core.entity.supplier.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {

    @Query("{'name' : {$regex : ?0, $options : 'i'}}")
    List<Supplier> findAllByNameRegex(String nameRegex);
}
