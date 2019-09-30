package com.ilt.cms.database.clinic;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.Status;

import java.util.List;
import java.util.Optional;

public interface ClinicDatabaseService {

    Optional<Clinic> findOne(String clinicId);

    Optional<Clinic> findActiveClinic(String clinicId);

    Clinic save(Clinic clinic);

    boolean delete(String clinicId);

    List<Clinic> listAllByIds();

    List<Clinic> listAllByIds(List<String> clinicIds);

    boolean checkClinicCodeExists(String clinicCode);

    Optional<Clinic> findClinicByClinicCode(String clinicCode);

    boolean exists(String clinicId);
}
