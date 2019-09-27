package com.ilt.cms.repository.spring;

import com.ilt.cms.core.entity.item.ClinicGroupItemMaster;
import com.ilt.cms.core.entity.item.MedicalCoverageItem;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicGroupItemMasterRepository extends MongoRepository<ClinicGroupItemMaster, String> {

    @ExistsQuery(value = "{'groupName' : ?0 }")
    boolean checkGroupNameExists(String groupName);

    ClinicGroupItemMaster findByGroupName(String groupName);

    @Query("{groupName : ?0, 'clinicGroupItemPrices.itemRefId' : ?1}")
    ClinicGroupItemMaster findGroupPrice(String groupName, String itemId);

}