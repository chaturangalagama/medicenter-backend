package business.mock;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.UserPaymentOption;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.patient.PatientVaccination;
import com.ilt.cms.core.entity.patient.VaccinationSchedule;
import com.ilt.cms.core.entity.vaccination.Dose;
import com.ilt.cms.core.entity.vaccination.Vaccination;
import com.ilt.cms.core.entity.vaccination.VaccinationScheme;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MockVaccination {

    public static Vaccination mockVaccination() throws NoSuchFieldException, IllegalAccessException {
        return mockVaccination("V00001", "vaccination 01", "code", -1,
                Arrays.asList(
                        mockDose("DS01", "DS1", "Code", "First Shot", new Charge(100, true),
                                new UserPaymentOption(10, 0, UserPaymentOption.PaymentType.PERCENTAGE), 10),
                        mockDose("DS02", "DS2", "Code", "Second Shot", new Charge(100, true),
                                new UserPaymentOption(10, 0, UserPaymentOption.PaymentType.PERCENTAGE), 60)));
    }

    public static Dose mockDose(){
        return mockDose("DS02", "DS2", "Code", "Second Shot", new Charge(100, true),
                new UserPaymentOption(10, 0, UserPaymentOption.PaymentType.PERCENTAGE), 60);
    }

    public static Dose mockDose(String doseId, String name, String code, String description, Charge charge, UserPaymentOption userPaymentOption, int nextDoseRecommendedGap){
        return new Dose(doseId, name, code, description, charge, userPaymentOption, nextDoseRecommendedGap);
    }

    public static Vaccination mockVaccination(String id, String name, String code, int ageInMonths, List<Dose> doses) throws NoSuchFieldException, IllegalAccessException {
        Vaccination vac1 = new Vaccination();
        Field fieldId = PersistedObject.class.getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(vac1, id);
        vac1.setName(name);
        vac1.setCode(code);
        vac1.setAgeInMonths(ageInMonths);
        vac1.setDoses(doses);
        return vac1;
    }

    public static PatientVaccination mockPatientVaccination() throws NoSuchFieldException, IllegalAccessException {
        return mockPatientVaccination("P00001", "D00001", "V00001", new Date(),
                "no place given", Arrays.asList(mockVaccinationSchedule()));
    }

    public static PatientVaccination mockPatientVaccination(String id, String doctorId, String vaccineId, Date givenDate,
                                                            String placeGiven, List<VaccinationSchedule> vaccinationSchedules)
            throws NoSuchFieldException, IllegalAccessException {
        PatientVaccination patientVaccination = new PatientVaccination();

        Field fieldId = PatientVaccination.class.getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(patientVaccination, id);
        patientVaccination.setDoctorId(doctorId);
        patientVaccination.setVaccineId(vaccineId);
        patientVaccination.setGivenDate(givenDate);
        patientVaccination.setPlaceGiven(placeGiven);
        patientVaccination.setVaccinationSchedules(vaccinationSchedules);
        return patientVaccination;
    }

    public static VaccinationSchedule mockVaccinationSchedule(){
        return mockVaccinationSchedule("V000001", new Date());
    }

    public static VaccinationSchedule mockVaccinationSchedule(String vaccineId, Date scheduleDate){
        VaccinationSchedule vaccinationSchedule = new VaccinationSchedule(vaccineId, scheduleDate);
        return vaccinationSchedule;
    }

    public static VaccinationScheme mockVaccinationScheme(){
        return mockVaccinationScheme("", new Charge(10, false));
    }

    public static VaccinationScheme mockVaccinationScheme(String doseId, Charge charge){
        VaccinationScheme vaccinationScheme = new VaccinationScheme(doseId, charge);
        return vaccinationScheme;
    }
}
