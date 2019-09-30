package business.service.appointment;


import business.config.service.SpringTestServiceConfiguration;
import com.ilt.cms.pm.business.service.patient.patientVisit.AppointmentService;
import com.ilt.cms.repository.patient.patientVisit.calendar.AppointmentRepository;
import com.ilt.cms.repository.patient.patientVisit.calendar.ClinicCalendarRepository;
import com.ilt.cms.repository.patient.patientVisit.calendar.DoctorCalendarRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
@Ignore
public class AppointmentServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceTest.class);

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ClinicCalendarRepository clinicCalendarRepository;
    @Autowired
    private DoctorCalendarRepository doctorCalendarRepository;

    @Before
    public void setup() throws Exception{
        /*when(appointmentRepository.findByClinicIdAndStartDateBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenAnswer(
                invocationOnMock -> {
                    String clinicId = invocationOnMock.getArgument(0);
                    LocalDateTime startDate = invocationOnMock.getArgument(1);
                    LocalDateTime endDate = invocationOnMock.getArgument(2);





                }
        );
        when(appointmentRepository.findByClinicIdAndDoctorIdAndStartDateBetween(anyString(), anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenAnswer(
                invocationOnMock -> {

                }
        );*/
    }

    public void findClinicAppointments() throws Exception{

    }


    public void findDoctorAppointments() throws Exception{

    }



    public void addBlockDoctorTime() throws Exception{

    }

    public void removeBlockDoctorTime() throws Exception{

    }


    public void createDoctorAppointment() throws Exception {


    }

    public void modifyDoctorAppointment() throws Exception {

    }

    public void deleteDoctorAppointment() throws Exception{

    }

    public void searchAppointment() throws Exception{

    }

    public void listConflictAppointment() throws Exception{

    }

    public void findClinicCalendar() throws Exception{

    }

    public void findDoctorCalendar() throws Exception{

    }

    public void updateClinicWorkingHour() throws Exception{

    }

    public void updateClinicHoliday() throws Exception{

    }

    public void updateDoctorLeave() throws Exception{

    }

    public void updateDoctorWorkingDay() throws Exception{

    }



}
