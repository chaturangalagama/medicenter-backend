package com.ilt.cms.database.patient;

import com.ilt.cms.core.entity.vaccination.Vaccination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface VaccinationDatabaseService {
    boolean isIdValid(String vaccinationId);

    Vaccination findFirstByDosesIn(String doseId);

    boolean checkIfAllVaccinationDoseIdExists(String doseIds);

    Page<Vaccination> findAll(PageRequest pageRequest);

    List<Vaccination> findAll();

    Vaccination save(Vaccination vaccination);
}
