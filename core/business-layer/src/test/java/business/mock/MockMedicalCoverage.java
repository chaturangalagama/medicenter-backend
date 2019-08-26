package business.mock;

import com.ilt.cms.core.entity.CopayAmount;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.common.ContactPerson;
import com.ilt.cms.core.entity.common.CorporateAddress;
import com.ilt.cms.core.entity.common.Relationship;
import com.ilt.cms.core.entity.coverage.CapLimiter;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.coverage.MedicalServiceScheme;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class MockMedicalCoverage {

    public static MedicalServiceScheme mockMedicalServiceScheme(Charge chargeEntity) {
        MedicalServiceScheme medicalServiceScheme = new MedicalServiceScheme();
        medicalServiceScheme.setMedicalServiceItemID("kdjkfldsi233ekl323");
        medicalServiceScheme.setModifiedCharge(chargeEntity);
        return medicalServiceScheme;
    }

    public static CoveragePlan mockCoveragePlan(){
        CoveragePlan coveragePlan = new CoveragePlan();
        coveragePlan.setId("fdsfesk4242fs3r");
        coveragePlan.setName("Name 1");
        coveragePlan.setCapPerVisit(new CapLimiter(1, 2));
        coveragePlan.setCapPerDay(new CapLimiter(1, 2));
        coveragePlan.setCapPerWeek(new CapLimiter(1, 2));
        coveragePlan.setCapPerMonth(new CapLimiter(1, 2));
        coveragePlan.setCapPerYear(new CapLimiter(1, 2));
        coveragePlan.setCapPerLifeTime(new CapLimiter(1, 2));
        coveragePlan.setLimitResetType(CoveragePlan.LimitResetType.FROM_FIRST_USE);
        coveragePlan.setCode("Code112");
        coveragePlan.setRemarks("remarks");
        coveragePlan.setClinicRemarks("clinic remarks");
        coveragePlan.setRegistrationRemarks("registration remarks");
        coveragePlan.setPaymentRemarks("Payment remarks");
        coveragePlan.setExcludedClinics(Arrays.asList("clinics 1", "clinics 2"));
        coveragePlan.setExcludeAllByDefault(false);
        coveragePlan.setMinimumNumberOfDiagnosisCodes(0);
        coveragePlan.setAllowedRelationship(Arrays.asList(Relationship.PARENT, Relationship.CHILDREN));


        return coveragePlan;
    }



    public static MedicalCoverage mockMedicalCoverage() {
        MedicalCoverage medicalCoverage = new MedicalCoverage();
        medicalCoverage.setName("Coverage 1");
        medicalCoverage.setType(MedicalCoverage.CoverageType.CORPORATE);
        medicalCoverage.setAddress(new CorporateAddress("Mr Boss", "Street 11", "09987"));
        medicalCoverage.setCode("COV01");
        medicalCoverage.setAccountManager("");
        medicalCoverage.setStartDate(LocalDate.now());
        medicalCoverage.setEndDate(LocalDate.now().plusMonths(1));
        medicalCoverage.setCreditTerms(90);
        medicalCoverage.setWebsite("www.idontknow.io");
        medicalCoverage.setTrackAttendance(true);
        medicalCoverage.setUsePatientAddressForBilling(true);
        medicalCoverage.setMedicineRefillAllowed(true);
        medicalCoverage.setShowDiscount(true);
        medicalCoverage.setShowMemberCard(true);
        medicalCoverage.setCostCenters(new ArrayList<>(Arrays.asList("Cost Center 1", "Cost Center 2")));
        medicalCoverage.setAccountManager("Account Manager");
        medicalCoverage.setContacts(new ArrayList<>(Arrays.asList(new ContactPerson("Mr James", "Admin", "650099222",
                "659800021", "650882223", "me@me.com"))));
        medicalCoverage.setPayAtClinic(true);


        ArrayList<CoveragePlan> coveragePlans = new ArrayList<>();
        CoveragePlan plan1 = new CoveragePlan();
        plan1.setId("11111");
        plan1.setName("Cover 100");
        plan1.setCapPerVisit(new CapLimiter(10, 3000));
        plan1.setCode("C100");
        plan1.setRemarks("Cover 100 plan");
        plan1.setCapPerYear(new CapLimiter(10, 3000));
        plan1.setCopayment(new CopayAmount(100, CopayAmount.PaymentType.PERCENTAGE));
        plan1.setCapPerMonth(new CapLimiter(10, 3000));
        plan1.setLimitResetType(CoveragePlan.LimitResetType.CALENDAR);
        plan1.setCapPerWeek(new CapLimiter(12, 3000));
        plan1.setCapPerYear(new CapLimiter(12, 3000));
        plan1.setCapPerLifeTime(new CapLimiter(100, 3000));
        plan1.setFilterDiagnosisCode(true);
        plan1.setMinimumNumberOfDiagnosisCodes(2);
        plan1.setClinicRemarks("Clinic remark");
        plan1.setRegistrationRemarks("registration remark");
        plan1.setPaymentRemarks("Payment remark");



        coveragePlans.add(plan1);
        CoveragePlan plan2 = new CoveragePlan();
        plan2.setId("22222");
        plan2.setCapPerVisit(new CapLimiter(10, 3000));
        plan2.setCode("C200");
        plan2.setCopayment(new CopayAmount(50, CopayAmount.PaymentType.DOLLAR));
        plan2.setName("Cover 200");
        plan2.setRemarks("Cover 200 plan");
        plan2.setCapPerWeek(new CapLimiter(12, 3000));
        plan2.setCapPerYear(new CapLimiter(12, 3000));
        plan2.setCapPerLifeTime(new CapLimiter(100, 3000));
        plan2.setFilterDiagnosisCode(false);
        plan2.setMinimumNumberOfDiagnosisCodes(0);
        coveragePlans.add(plan2);
        medicalCoverage.setCoveragePlans(coveragePlans);
        return medicalCoverage;
    }


}
