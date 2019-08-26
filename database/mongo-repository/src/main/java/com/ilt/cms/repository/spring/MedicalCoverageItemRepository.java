package com.ilt.cms.repository.spring;

import com.ilt.cms.core.entity.item.ItemCoverageScheme;
import com.ilt.cms.core.entity.item.MedicalCoverageItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalCoverageItemRepository extends MongoRepository<MedicalCoverageItem, String> {

    Optional<MedicalCoverageItem> findByPlanId(String planId);

    @Query("{planId : ?0, 'itemCoverageSchemes.itemId' : ?1}")
    MedicalCoverageItem findByPlanAndItemId(String planId, String ItemId);
}
