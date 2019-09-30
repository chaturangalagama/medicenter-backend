package com.ilt.cms.db.service.patient;

import com.ilt.cms.core.entity.vaccination.Vaccination;
import com.ilt.cms.database.patient.VaccinationDatabaseService;
import com.ilt.cms.repository.patient.VaccinationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoVaccinationDatabaseService implements VaccinationDatabaseService {

    private VaccinationRepository vaccinationRepository;

    public MongoVaccinationDatabaseService(VaccinationRepository vaccinationRepository){
        this.vaccinationRepository = vaccinationRepository;
    }
    @Override
    public boolean isIdValid(String vaccinationId) {
        return vaccinationRepository.isIdValid(vaccinationId);
    }

    @Override
    public Vaccination findFirstByDosesIn(String doseId) {
        return vaccinationRepository.findFirstByDosesIn(doseId);
    }

    @Override
    public boolean checkIfAllVaccinationDoseIdExists(String doseIds) {
        return vaccinationRepository.checkIfAllVaccinationDoseIdExists(doseIds);
    }

    @Override
    public Page<Vaccination> findAll(PageRequest pageRequest) {
        return vaccinationRepository.findAll(pageRequest);
    }

    @Override
    public List<Vaccination> findAll() {
        return vaccinationRepository.findAll();
    }

    @Override
    public Vaccination save(Vaccination vaccination) {
        return vaccinationRepository.save(vaccination);
    }
}
