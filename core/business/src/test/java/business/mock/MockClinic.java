package business.mock;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.common.CorporateAddress;

import java.util.Arrays;
import java.util.List;

public class MockClinic {

    public static Clinic mockClinic(){
        return mockClinic("Bukit Batok", "West Avenue 5", "12688",
                "8989022", "8899221", "CL01", Arrays.asList("DO1", "DO2"), "HMC", "clinic@email.com");
    }

    public static Clinic mockClinic(String my_clinic, String in_singapore, String postalCode,
                                    String faxNumber, String contactNumber, String cl91, List<String> attendingDoctorId, String groupName, String emailAddress) {
        return new Clinic(my_clinic, groupName, emailAddress, new CorporateAddress("attention", in_singapore, postalCode), faxNumber,
                contactNumber, Status.ACTIVE, cl91, attendingDoctorId, Arrays.asList("marry001", "scarlet", "wonderwoman"));
    }
}
