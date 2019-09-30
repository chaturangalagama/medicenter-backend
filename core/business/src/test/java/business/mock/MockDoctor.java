package business.mock;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.consultation.ConsultationTemplate;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.doctor.Speciality;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class MockDoctor {

    public static Doctor mockDoctor() throws NoSuchFieldException, IllegalAccessException {
        Field idField = PersistedObject.class.getDeclaredField("id");
        idField.setAccessible(true);
        return mockDoctor(idField, "Doctor Who", Speciality.Practice.ANAESTHESIOLOGY, Doctor.DoctorGroup.LOCUM,
                Arrays.asList(new ConsultationTemplate("Template Name", "<p>Doctor Name is : {{ doctor_name }}</p>")), "admin", "0001");
    }

    public static Doctor mockDoctor(Field idField, String doctor_who, Speciality.Practice anaesthesiology, Doctor.DoctorGroup locum,
                                     List<ConsultationTemplate> consultationTemplates, String admin, String value) throws IllegalAccessException {
        Doctor doctor1 = new Doctor(doctor_who, "MBBS", anaesthesiology, locum, Status.ACTIVE, consultationTemplates);
        doctor1.setUsername(admin);
        idField.set(doctor1, value);
        if (consultationTemplates != null) {
            for (ConsultationTemplate consultationTemplate : consultationTemplates) {
                idField.set(consultationTemplate, "00001");
            }
        }
        doctor1.setMcr("MCR10001");
        return doctor1;
    }
}
