package com.ilt.cms.repository.patient;

import com.ilt.cms.core.entity.vaccination.Vaccination;
import org.springframework.data.mongodb.repository.ExistsQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRepository extends MongoRepository<Vaccination, String> {

    @ExistsQuery(value = "{'_id' : ?0 }")
    boolean isIdValid(String vaccinationId);

    @Query("{'doses.doseId' : ?0}")
    Vaccination findFirstByDosesIn(String doseId);


    @ExistsQuery("{ 'doses.doseId' : ?0 }")
    boolean checkIfAllVaccinationDoseIdExists(String doseIds);
}
