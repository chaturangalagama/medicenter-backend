package business.mock;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import com.ilt.cms.core.entity.coverage.RelationshipMapping;
import com.ilt.cms.core.entity.patient.Address;
import com.ilt.cms.core.entity.patient.ContactNumber;

import java.time.LocalDate;

public class MockPolicyHolder {
    public static PolicyHolder.PolicyHolderChas mockPolicyHolderChas(){
        PolicyHolder.PolicyHolderChas policyHolder = new PolicyHolder.PolicyHolderChas();
        UserId userId = new UserId(UserId.IdType.NRIC, "123456");
        policyHolder.setIdentificationNumber(userId);
        policyHolder.setName("Policy holders name");
        policyHolder.setMedicalCoverageId("M223222");
        policyHolder.setPlanId("P332222");
        policyHolder.setPatientCoverageId("P223222");
        policyHolder.setSpecialRemarks("Just have an eye out for this guy.");
        policyHolder.setStartDate(LocalDate.now());
        policyHolder.setEndDate(LocalDate.now().plusMonths(1));
        policyHolder.setAddress(new Address("Choa Chu Kang Crescent, 683690 Singapore, Singapore","Singapore", "683690"));
        policyHolder.setHome(new ContactNumber("81005992"));
        policyHolder.setStatus(Status.ACTIVE);
        return policyHolder;
    }

    public static PolicyHolder.PolicyHolderMediSave mockPolicyHolderMediSave(){
        PolicyHolder.PolicyHolderMediSave policyHolder = new PolicyHolder.PolicyHolderMediSave();
        UserId userId = new UserId(UserId.IdType.PASSPORT, "123456");
        policyHolder.setIdentificationNumber(userId);
        policyHolder.setName("Policy holders name");
        policyHolder.setMedicalCoverageId("M223222");
        policyHolder.setPlanId("P332222");
        policyHolder.setPatientCoverageId("P223222");
        policyHolder.setSpecialRemarks("Just have an eye out for this guy.");
        policyHolder.setStartDate(LocalDate.now());
        policyHolder.setEndDate(LocalDate.now().plusMonths(1));
        policyHolder.setAddress(new Address("Choa Chu Kang Crescent, 683690 Singapore, Singapore","Singapore", "683690"));
        policyHolder.setHome(new ContactNumber("81005992"));
        policyHolder.setStatus(Status.ACTIVE);
        return policyHolder;
    }

    public static PolicyHolder.PolicyHolderInsurance mockPolicyHolderInsurance(){
        PolicyHolder.PolicyHolderInsurance policyHolder = new PolicyHolder.PolicyHolderInsurance();
        UserId userId = new UserId(UserId.IdType.NRIC_BLUE, "123456");
        policyHolder.setIdentificationNumber(userId);
        policyHolder.setName("Policy holders name");
        policyHolder.setMedicalCoverageId("M223222");
        policyHolder.setPlanId("P332222");
        policyHolder.setPatientCoverageId("P223222");
        policyHolder.setSpecialRemarks("Just have an eye out for this guy.");
        policyHolder.setStartDate(LocalDate.now());
        policyHolder.setEndDate(LocalDate.now().plusMonths(1));
        policyHolder.setAddress(new Address("93 Kallang Way 3, 349110, Singapore","Singapore", "349110"));
        policyHolder.setHome(new ContactNumber("67466530"));
        policyHolder.setStatus(Status.ACTIVE);
        return policyHolder;
    }

    public static PolicyHolder.PolicyHolderCorporate mockPolicyHolderCorporate(){
        PolicyHolder.PolicyHolderCorporate policyHolder = new PolicyHolder.PolicyHolderCorporate();
        UserId userId = new UserId(UserId.IdType.FIN, "123456");
        policyHolder.setIdentificationNumber(userId);
        policyHolder.setName("Policy holders name");
        policyHolder.setMedicalCoverageId("M223222");
        policyHolder.setPlanId("P332222");
        policyHolder.setPatientCoverageId("P223222");
        policyHolder.setSpecialRemarks("Just have an eye out for this guy.");
        policyHolder.setStartDate(LocalDate.now());
        policyHolder.setEndDate(LocalDate.now().plusMonths(1));
        policyHolder.setAddress(new Address("C190 Yishun Avenue 7, 768925, Singapore","Singapore", "768925"));
        policyHolder.setHome(new ContactNumber("81005992"));
        policyHolder.setStatus(Status.ACTIVE);
        return policyHolder;
    }
}
