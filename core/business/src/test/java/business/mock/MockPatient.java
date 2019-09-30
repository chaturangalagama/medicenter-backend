package business.mock;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.common.Relationship;
import com.ilt.cms.core.entity.file.FileMetaData;
import com.ilt.cms.core.entity.patient.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

public class MockPatient {

    public static Patient mockPatient() throws NoSuchFieldException, IllegalAccessException {
        Patient patient = new Patient();
        Field id = PersistedObject.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(patient, "0001");
        patient.setName("patient1");
        patient.setAddress(new Address("Line 1", "Singapore", "00001"));
        patient.setCompanyId(null);
        patient.setContactNumber(new ContactNumber("6588881111"));
        patient.setDob(LocalDate.now());
        patient.setEmailAddress("test@test.com");
        patient.setEmergencyContactNumber(new EmergencyContactNumber("Name", Relationship.PARENT, "6599991111"));
        patient.setGender(Patient.Gender.OTHER);
        patient.setMaritalStatus(Patient.MaritalStatus.SINGLE);
        patient.setUserId(new UserId(UserId.IdType.PASSPORT, "P000000001"));
        patient.setNationality("Singaporean");
        patient.setStatus(Status.ACTIVE);
        patient.setCompany(new PatientCompany("Company Name", "Address", "postal", "Executor"));
        patient.setRemarks(null);
        patient.setAllergies(Arrays.asList(
                new PatientAllergy(PatientAllergy.AllergyType.ALLERGY_GROUP, "Sunlight", "I cant go outside",
                new Date()),
                new PatientAllergy(PatientAllergy.AllergyType.NAME_CONTAINING, "DR01", "I cant go inside out",
                        new Date())));
        PatientVaccination vaccination = new PatientVaccination();
        vaccination.setId("00001");
        vaccination.setDoctorId("D0001");
        vaccination.setGivenDate(new Date());
        vaccination.setVaccineId("D001");
        ArrayList<VaccinationSchedule> vaccinationSchedules = new ArrayList<>();
        VaccinationSchedule schedule = new VaccinationSchedule();
        schedule.setScheduledDate(new Date());
        schedule.setVaccineId("00001");
        vaccinationSchedules.add(schedule);
        vaccination.setVaccinationSchedules(vaccinationSchedules);
        patient.addPatientVaccination(vaccination);
        patient.setTitle("Mr");
        patient.setPreferredMethodOfCommunication("Phone");
        patient.setConsentGiven(true);
        patient.setRace("Martian");
        patient.setPreferredLanguage("Lingua franca");
        patient.setPrimaryCareNetwork(new PrimaryCareNetwork(true, false, LocalDate.now()));
        patient.setFileMetaData(new ArrayList<>(Arrays.asList(new FileMetaData("patient/2019/5c36a936819737b028fc4d26/91006379399707189251548313296650100000",
                "test", "test_file", "admin", "5ab99ffadbea1b2384db9af8", "xlsx", 45415, "\"for test\""))));
        return patient;
    }

}
