package com.ilt.cms.repository.patient;

import com.ilt.cms.core.entity.patient.MedicalAlert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalAlertRepository extends MongoRepository<MedicalAlert, String> {

    MedicalAlert findByPatientId(String patientId);

    @Query("{ 'details.alertId' : ?0 }")
    MedicalAlert findByMedicalAlertId(String alertId);

}
