package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.patient.PatientNote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientNoteRepository extends MongoRepository<PatientNote, String> {

    PatientNote findByPatientId(String patientId);

}
