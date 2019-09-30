package com.ilt.cms.db.service.clinic;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.repository.clinic.ClinicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MongoClinicDatabaseService implements ClinicDatabaseService {

    private ClinicRepository clinicRepository;

    public MongoClinicDatabaseService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    @Override
    public Optional<Clinic> findOne(String clinicId) {
        return clinicRepository.findById(clinicId);
    }

    @Override
    public Optional<Clinic> findActiveClinic(String clinicId) {
        return clinicRepository.findByIdAndStatus(clinicId, Status.ACTIVE);
    }

    @Override
    public Clinic save(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    @Override
    public List<Clinic> listAllByIds() {
        return clinicRepository.findAll();
    }

    @Override
    public List<Clinic> listAllByIds(List<String> clinicIds) {
        return clinicRepository.findByIdIn(clinicIds);
    }

    @Override
    public boolean checkClinicCodeExists(String clinicCode) {
        return clinicRepository.checkClinicCodeExists(clinicCode);
    }

    @Override
    public Optional<Clinic> findClinicByClinicCode(String clinicCode) {
        return clinicRepository.findClinicByClinicCode(clinicCode);
    }

    @Override
    public boolean exists(String clinicId) {
        return clinicRepository.existsById(clinicId);
    }

    @Override
    public boolean delete(String clinicId) {
        clinicRepository.deleteById(clinicId);
        return true;
    }
}
