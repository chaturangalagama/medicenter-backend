package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.doctor.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {

    Doctor findByUsername(String username);

    List<Doctor> findByStatus(Status status);

    @Query(value = "{ '_id' : { $in : ?0} }")
    List<Doctor> findListOfDoctorsById(List<String> doctorId);

    @Query(value = "{$and : [{'_id': {$in: ?0}}, {'doctorGroup': ?1}]}")
    List<Doctor> findByIdsAndGroup(List<String> doctorId, Doctor.DoctorGroup group);

}
