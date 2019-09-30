package com.ilt.cms.db.service.clinic;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.database.clinic.DoctorDatabaseService;
import com.ilt.cms.repository.clinic.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MongoDoctorDatabaseService implements DoctorDatabaseService {

    private DoctorRepository doctorRepository;

    public MongoDoctorDatabaseService(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }
    @Override
    public Optional<Doctor> findOne(String doctorId) {
        return doctorRepository.findById(doctorId);
    }

    @Override
    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public void delete(String doctorId) {
        doctorRepository.deleteById(doctorId);
    }

    @Override
    public List<Doctor> findByStatus(Status status) {
        return doctorRepository.findByStatus(status);
    }

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    @Override
    public boolean exists(String doctorId) {
        return doctorRepository.existsById(doctorId);
    }
}
