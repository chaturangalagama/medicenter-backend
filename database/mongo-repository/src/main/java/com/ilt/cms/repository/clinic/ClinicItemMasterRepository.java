package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.item.ClinicItemMaster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicItemMasterRepository extends MongoRepository<ClinicItemMaster, String> {


    @Query("{clinicId : ?0, 'itemsForClinic.itemRefId' : ?1}")
    ClinicItemMaster findClinicItemPrice(String clinicId, String itemId);

}
