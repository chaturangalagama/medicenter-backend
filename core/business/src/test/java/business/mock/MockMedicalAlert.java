package business.mock;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.patient.MedicalAlert;

import java.lang.reflect.Field;
import java.util.*;

public class MockMedicalAlert {
    public static MedicalAlert mockMedicalAlert() throws NoSuchFieldException, IllegalAccessException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -2);
        Date addDate1 = calendar.getTime();
        calendar.add(Calendar.YEAR, 8);
        Date expiryDate1 = calendar.getTime();
        MedicalAlert.MedicalAlertDetails medicalAlertDetails1 = mockMedicalAlertDetails(MedicalAlert.AlertType.MEDICAL_CONDITION, "No Hands", "Lost in an accident", MedicalAlert.MedicalAlertDetails.Priority.HIGH, addDate1, expiryDate1);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -2);
        Date addDate2 = calendar.getTime();
        calendar.add(Calendar.YEAR, 8);
        Date expiryDate2 = calendar.getTime();
        MedicalAlert.MedicalAlertDetails medicalAlertDetails2 = mockMedicalAlertDetails(MedicalAlert.AlertType.CHRONIC_DISEASE, "Two brains", "From birth", MedicalAlert.MedicalAlertDetails.Priority.LOW, addDate2, expiryDate2);

        MedicalAlert medicalAlert = mockMedicalAlert("M0001", "P0001", Arrays.asList(medicalAlertDetails1, medicalAlertDetails2));

        return medicalAlert;
    }

    public static MedicalAlert mockMedicalAlert(String id, String patientId, List<MedicalAlert.MedicalAlertDetails> medicalAlertDetails) throws NoSuchFieldException, IllegalAccessException {
        MedicalAlert medicalAlert = new MedicalAlert(patientId);
        medicalAlertDetails.stream().forEach(medicalAlertDetail -> {
            medicalAlert.addAlert(medicalAlertDetail.getAlertType(),
                    medicalAlertDetail.getName(), medicalAlertDetail.getRemark(),
                    medicalAlertDetail.getPriority(), medicalAlertDetail.getAddedDate(),
                    medicalAlertDetail.getExpiryDate(), false);
        });
        Field fieldId = PersistedObject.class.getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(medicalAlert, "M0000001");
        return medicalAlert;
    }

    public static MedicalAlert.MedicalAlertDetails mockMedicalAlertDetails(MedicalAlert.AlertType alertType,
                                                                           String name, String remark,
                                                                           MedicalAlert.MedicalAlertDetails.Priority priority,
                                                                           Date addedDate,
                                                                           Date expiryDate){
        MedicalAlert.MedicalAlertDetails medicalAlertDetails = new MedicalAlert.MedicalAlertDetails(
                alertType, name, remark,
                priority, addedDate,
                expiryDate);
        return medicalAlertDetails;
    }
}
