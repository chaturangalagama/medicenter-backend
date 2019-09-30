package com.ilt.cms.db.service.clinic;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.patient.PatientNote;
import com.ilt.cms.database.clinic.PatientNoteDatabaseService;
import com.ilt.cms.repository.clinic.PatientNoteRepository;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class MongoPatientNoteDatabaseService implements PatientNoteDatabaseService {

    private PatientNoteRepository patientNoteRepository;
    private MongoTemplate mongoTemplate;

    public MongoPatientNoteDatabaseService(PatientNoteRepository patientNoteRepository, MongoTemplate mongoTemplate){
        this.patientNoteRepository = patientNoteRepository;
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public PatientNote findByPatientId(String patientId) {
        return patientNoteRepository.findByPatientId(patientId);
    }

    @Override
    public PatientNote addNewPatientNoteDetails(String patientId, PatientNote.PatientNoteDetails patientNoteDetails){
        return mongoTemplate.findAndModify(Query.query(Criteria.where("patientId").is(patientId)),
                new Update().push("noteDetails", patientNoteDetails),
                FindAndModifyOptions.options().returnNew(true).upsert(true), PatientNote.class);
    }
    @Override
    public PatientNote updatePatientNoteDetailsStatus(String patientNoteDetailId, Status newStatus){
        return mongoTemplate.findAndModify(Query.query(Criteria.where("noteDetails")
                        .elemMatch(Criteria.where("patientNoteId").is(patientNoteDetailId))),
                new Update().set("noteDetails.$.status", newStatus),
                FindAndModifyOptions.options().returnNew(true), PatientNote.class);
    }
}
