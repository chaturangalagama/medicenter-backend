package com.ilt.cms.repository.patient;

import com.ilt.cms.core.entity.vital.Vital;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VitalRepository extends MongoRepository<Vital, String> {


    List<Vital> findAllByPatientIdAndTakenTimeAfterOrderByTakenTime(String patientId, LocalDateTime takenTime);

    @Query(value = "{ $and : [ {'patientId' : ?0}, { 'takenTime': {$gte : ?1 } }, { 'takenTime' : {$lte : ?2} } ] }")
    List<Vital> findPatientVitals(String patientId, LocalDateTime after, LocalDateTime before, Sort.Order order);

}
