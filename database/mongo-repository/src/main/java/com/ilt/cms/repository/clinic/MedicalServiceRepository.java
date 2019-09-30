package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.service.MedicalService;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalServiceRepository extends MongoRepository<MedicalService, String> {

    @ExistsQuery(value = "{'medicalServiceItemList.id' : ?0 }")
    boolean queryMedicalServiceItem(String medicalServiceItemId);

    @ExistsQuery(value = "{ '_id' : ?0, 'medicalServiceItemList.id' : ?1 }")
    boolean queryMedicalServiceItem(String medicalServiceId, String medicalServiceItemId);

    @Query(value = "{ 'medicalServiceItemList._id' : { $in : ?0} }")
    List<MedicalService> findByMedicalServiceItem(List<String> medicalServiceItemId);

    @Query(value = "{ 'medicalServiceItemList._id' :  ?0 }")
    MedicalService findByMedicalServiceItem(String medicalServiceItemId);
}