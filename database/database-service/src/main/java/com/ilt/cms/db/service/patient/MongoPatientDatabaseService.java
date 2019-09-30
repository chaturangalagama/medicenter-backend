package com.ilt.cms.db.service.patient;

import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.database.clinic.system.RunningNumberService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.repository.patient.PatientRepository;
import com.lippo.cms.exception.PatientException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.util.exception.RestValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class MongoPatientDatabaseService implements PatientDatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(MongoPatientDatabaseService.class);
    private PatientRepository patientRepository;

    private RunningNumberService runningNumberService;
    private MongoTemplate mongoTemplate;

    public MongoPatientDatabaseService(PatientRepository patientRepository,
                                       RunningNumberService runningNumberService,
                                       MongoTemplate mongoTemplate) {
        this.patientRepository = patientRepository;
        this.runningNumberService = runningNumberService;
        this.mongoTemplate = mongoTemplate;
    }

    //@Override
    @Deprecated
    public Patient findOne(String patientId) {
        return patientRepository.findById(patientId).orElse(null);
    }

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public boolean exists(String patientId) {
        return patientRepository.existsById(patientId);
    }

    @Override
    public Patient patientRegistration(Patient patient) throws RestValidationException, PatientException {

        boolean userIdAvailable = isUserIdAvailable(patient.getUserId());

        if (!userIdAvailable) {
            throw new PatientException(StatusCode.E1000);
        }
        patient.resetNewPatientRegistrationDetails();
        patient.setRegistrationDate(LocalDate.now());
        patient.setLastVisitedDate(LocalDate.now());
        patient.setPatientNumber(runningNumberService.generatePatientNumber());

        if (!patient.parameterValuesValid()) {
            throw new RestValidationException("Patient information is not valid");
        }

        Patient persistedPatient = patientRepository.save(patient);

        return persistedPatient;
    }

    @Override
    public Optional<Patient> findPatientById(String patientId) {
        return patientRepository.findById(patientId);
    }

    @Override
    public Patient findPatient(String type, String searchId) throws PatientException {

        Optional<Patient> patientOpt;
        switch (type) {
            case "userid": {
                if (!searchId.contains(":"))
                    throw new PatientException(StatusCode.E1002, "Search type format invalid");
                String[] split = searchId.split(":");
                String idType = split[0];
                String idNumber = split[1];
                patientOpt = patientRepository.findByUserId(new UserId(UserId.IdType.valueOf(idType), idNumber));
                break;
            }
            case "name": {
                List<Patient> patients = patientRepository.findAllByNameLike(searchId);
                patientOpt = patients.stream().findFirst();
                break;
            }
            case "systemuserid": {
                patientOpt = patientRepository.findById(searchId);
                break;

            }
            default: {
                throw new PatientException(StatusCode.E1001);
            }
        }
        if (!patientOpt.isPresent()) {
            throw new PatientException(StatusCode.E2000);
        }
        Patient patient = patientOpt.get();
        return patient;
    }

    @Override
    public boolean validateIdNumberUse(String searchId) throws PatientException {
        //PatientRepository patientRepository = this.patientRepository.getPatientRepository();
        if (!searchId.contains(":"))
            throw new PatientException(StatusCode.E1002, "Search type format invalid");
        String[] split = searchId.split(":");
        String idType = split[0];
        String idNumber = split[1];
        boolean exists = patientRepository.existsByUserId(new UserId(UserId.IdType.valueOf(idType), idNumber));
        //return new HttpApiResponse(exists);
        return exists;
    }

    @Override
    public Patient updatePatient(String patientId, Patient patientUpdate) throws PatientException {
        Optional<Patient> foundPatient = patientRepository.findById(patientId);
        //Patient patient = patientRepository.getPatientRepository().findOne(patientId);
        if (!foundPatient.isPresent()) {
            throw new PatientException(StatusCode.E2000);
        } else {
            Patient patient = foundPatient.get();
            updatePatientInformation(patient, patientUpdate);
            Patient newPatient = patientRepository.save(patient);
            //return new HttpApiResponse(newPatient);
            return newPatient;
        }
    }

    @Override
    public List<Patient> likeSearchPatient(String value) {
        logger.info("limiting search for 15 list size");
        List<Patient> patients = patientRepository
                .patientLikeSearch(value, new PageRequest(0, 15)); //todo take it to a config
        //return new HttpApiResponse(patients);
        return patients;
    }

    @Override
    public List<Patient> findAll(List<String> patientIds){
        return patientRepository.findByIdIn(patientIds);
    }

    @Override
    public Patient likeSearchPatient(String patientId, String nameOrNirc) {
        Criteria criteria = new Criteria();
        if (nameOrNirc != null) {
            criteria.orOperator(
                    Criteria.where("name").regex(".*" + nameOrNirc.trim() + ".*", "i"),
                    Criteria.where("userId.number").regex(".*" + nameOrNirc.trim() + ".*", "i")
            );
        }

        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("id").is(patientId));
        return mongoTemplate.findOne(query, Patient.class);
    }

    @Override
    public boolean isUserIdAvailable(UserId userId) {
        return !mongoTemplate.exists(Query.query(
                Criteria.where("userId.idType").is(userId.getIdType())
                        .and("userId.number").is(userId.getNumber())), Patient.class);
    }


    /**
     * Updates the patient information which are allowed part of the update call
     * <p>
     * Does not update the allergies, patientVaccinations, coverages and patientId
     *
     * @param currentPatient
     * @param newPatientInfo
     * @return Patient
     */
    private Patient updatePatientInformation(Patient currentPatient, Patient newPatientInfo) {
        currentPatient.setName(newPatientInfo.getName());
        currentPatient.setDob(newPatientInfo.getDob());
        currentPatient.setUserId(newPatientInfo.getUserId());
        currentPatient.setGender(newPatientInfo.getGender());
        currentPatient.setContactNumber(newPatientInfo.getContactNumber());
        currentPatient.setAddress(newPatientInfo.getAddress());
        currentPatient.setEmailAddress(newPatientInfo.getEmailAddress());
        currentPatient.setEmergencyContactNumber(newPatientInfo.getEmergencyContactNumber());
        currentPatient.setCompany(newPatientInfo.getCompany());
        currentPatient.setNationality(newPatientInfo.getNationality());
        currentPatient.setCompanyId(newPatientInfo.getCompanyId());
        currentPatient.setMaritalStatus(newPatientInfo.getMaritalStatus());
        currentPatient.setRemarks(newPatientInfo.getRemarks());
        currentPatient.setAllergies(newPatientInfo.getAllergies());
        currentPatient.setTitle(newPatientInfo.getTitle());
        currentPatient.setPreferredMethodOfCommunication(newPatientInfo.getPreferredMethodOfCommunication());
        currentPatient.setConsentGiven(newPatientInfo.isConsentGiven());
        currentPatient.setRace(newPatientInfo.getRace());
        currentPatient.setPreferredLanguage(newPatientInfo.getPreferredLanguage());
        currentPatient.setPrimaryCareNetwork(newPatientInfo.getPrimaryCareNetwork());
        currentPatient.setOnGoingMedications(newPatientInfo.getOnGoingMedications());

        return currentPatient;

    }


}
