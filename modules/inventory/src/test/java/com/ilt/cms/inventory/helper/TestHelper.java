package com.ilt.cms.inventory.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lippo.commons.web.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestHelper {
    private static ObjectMapper JSON_MAPPER = new ObjectMapper();

    public static final String TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbXSwiZXhwIjoxNTEyMjAyNzM3fQ.SEhI9pO0idsC2ozxyT9elJhZAskVagnYPkpBVHNN9nAtMKHqDbHPpnICM1BC6CC5975-yVLTCUr1VDvAoaHMmw";


    static {
        Jackson2ObjectMapperBuilder json = Jackson2ObjectMapperBuilder.json();
        json.indentOutput(true);
        json.serializationInclusion(JsonInclude.Include.NON_NULL);
        json.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        JSON_MAPPER = json.build();
    }

    public static MockHttpServletRequestBuilder httpRequestBuilder(MockHttpServletRequestBuilder requestBuilder, Object obj) throws JsonProcessingException {
        if (obj != null) {
            return requestBuilder.accept(MediaType.APPLICATION_JSON).content(JSON_MAPPER.writeValueAsBytes(obj))
                    .contentType(MediaType.APPLICATION_JSON);
        } else {
            return requestBuilder.accept(MediaType.APPLICATION_JSON).content("{}")
                    .contentType(MediaType.APPLICATION_JSON);
        }
    }

    /*public static Patient mockPatient() {
        Patient patient = new Patient();
        patient.setName("patient1");
        patient.setAddress(new Address("Line 1", "Singapore", "00001"));
        patient.setCompanyId(null);
        patient.setContactNumber(new ContactNumber("6588881111"));
        patient.setDob(new Date());
        patient.setEmailAddress("test@test.com");
        patient.setEmergencyContactNumber(new EmergencyContactNumber("Name", Relationship.PARENT, "6599991111"));
        patient.setGender(Patient.Gender.OTHER);
        patient.setMaritalStatus(Patient.MaritalStatus.SINGLE);
        patient.setUserId(new UserId(UserId.IdType.PASSPORT, "P000000001"));
        patient.setNationality("Singaporean");
        patient.setStatus(Status.ACTIVE);
        patient.setCompany(new PatientCompany("Company Name", "Address", "postal", "Executor"));
        patient.setRemarks(null);
        patient.setAllergies(Arrays.asList(new PatientAllergy(PatientAllergy.AllergyType.ALLERGY_GROUP, "Sunlight", "I cant go outside",
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
        return patient;
    }*/


    public static void mockAuthenticationResponse(RestTemplate restTemplate, User user) {
        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.getBody()).thenReturn(user);
        when(restTemplate.exchange(any(RequestEntity.class), eq(User.class))).thenReturn(responseEntity);
    }


    public static User userDetails(List<String> roles) {
        User user = new User();
        user.setMessage("Success");
        User.Payload payload = new User().newPayload();
        payload.setRoles(roles);
        payload.setUserName("username");
        user.setPayload(payload);
        return user;
    }

    public static User userDetails() {
        return userDetails(Arrays.asList("ROLE_PATIENT_ACCESS", "ROLE_PATIENT_REGISTRATION"));
    }

    public static String mockObjectId() throws NoSuchAlgorithmException {
        int randomNum = ThreadLocalRandom.current().nextInt(100000, 999999 + 1);
        String password = randomNum+"";

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    }

}
