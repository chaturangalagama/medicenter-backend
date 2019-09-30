package com.ilt.cms.database.clinic;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.doctor.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorDatabaseService {
    Optional<Doctor> findOne(String doctorId);

    Doctor save(Doctor doctor);

    void delete(String doctorId);

    List<Doctor> findByStatus(Status status);

    List<Doctor> findAll();

    boolean exists(String doctorId);
}
