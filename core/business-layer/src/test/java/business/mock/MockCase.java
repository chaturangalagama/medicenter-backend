package business.mock;

import com.ilt.cms.core.entity.casem.*;
import com.ilt.cms.core.entity.casem.Case.CaseStatus;
import com.ilt.cms.core.entity.casem.Claim.AppealRejection;
import com.ilt.cms.core.entity.casem.Claim.ClaimResult;
import com.ilt.cms.core.entity.casem.Claim.ClaimStatus;
import com.ilt.cms.core.entity.casem.Claim.SubmissionResult;
import com.ilt.cms.core.entity.casem.Invoice.InvoiceType;
import com.ilt.cms.core.entity.casem.Invoice.PaymentMode;
import com.ilt.cms.core.entity.casem.Package;
import com.ilt.cms.core.entity.casem.Package.PackageStatus;
import com.ilt.cms.core.entity.casem.SalesOrder.SalesStatus;
import com.ilt.cms.core.entity.item.Cost;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.item.SellingPrice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

public class MockCase {

    public static Case createSampleCase() {
        Case aCase = new Case();
        aCase.setId("20181205000000");
        aCase.setPatientId("1234");
        aCase.setCaseNumber("20181205000001");
        aCase.setClinicId("C1234");
        aCase.setStatus(CaseStatus.OPEN);
        aCase.setVisitIds(new ArrayList<>(Arrays.asList("2018001", "2018002")));
        aCase.setSalesOrder(createSampleSaleOrder());
        aCase.setPurchasedPackage(createSamplePackage());
        return aCase;
    }

    private static SalesOrder createSampleSaleOrder() {
        SalesOrder salesOrderEntity = SalesOrder.newSalesOrder(10, "SN00001");
        salesOrderEntity.getPurchaseItems().addAll(Collections.singletonList(createSalesItemEntity()));
        salesOrderEntity.getInvoices().addAll(Collections.singletonList(createInvoiceEntity()));
        salesOrderEntity.setId("SALE1234");
        return salesOrderEntity;
    }

    private static SalesItem createSalesItemEntity() {
        SalesItem salesItemEntity = new SalesItem();
        salesItemEntity.setExcludedCoveragePlanIds((Set<String>) Arrays.asList("1234"));
        salesItemEntity.setCost(new Cost(100, true));
        salesItemEntity.setItemRefId("ITEM_REF_ID");
        salesItemEntity.setDosage(1);
        salesItemEntity.setDuration(2);
        salesItemEntity.setInstruct("TDS");
        salesItemEntity.setBatchNo("123");
        salesItemEntity.setExpireDate(LocalDate.now());
        salesItemEntity.setPurchaseQty(1000);
        salesItemEntity.setPurchaseUom("TAP");
//        salesItemEntity.setSubItems(Collections.singletonList(new SalesItem()));
        salesItemEntity.setItemPriceAdjustment(new ItemPriceAdjustment(100));
        salesItemEntity.setSoldPrice(100);
        salesItemEntity.setRemarks("Stop this pill if cured.");
        return salesItemEntity;
    }

    private static Invoice createInvoiceEntity() {
        Invoice invoiceEntity = new Invoice();
        invoiceEntity.setVisitId("V0003");
        invoiceEntity.setTaxAmount(10);
        invoiceEntity.setInvoiceTime(LocalDateTime.now());
        invoiceEntity.setInvoiceType(InvoiceType.DIRECT);
        invoiceEntity.setPaidAmount(1000);
        invoiceEntity.setPayableAmount(1000);
        invoiceEntity.setPaidTime(LocalDateTime.now());
        invoiceEntity.setPaymentInfos(Collections.emptyList());
        invoiceEntity.setPlanId("PLAN_ID_1111");
        invoiceEntity.setInvoiceNumber("123456677");
        return invoiceEntity;
    }

    private Claim createClaimEntity() {
        Claim claimEntity = new Claim();
        claimEntity.setAppealRejections(Collections.singletonList(new AppealRejection("PolicyExpired")));
        claimEntity.setAttendingDoctorId("DoctorId");
        claimEntity.setClaimDoctorId("ClaimedDoctor_ID");
        claimEntity.setClaimExpectedAmt(1000);
        claimEntity.setClaimId("CLAIM_ID");
        claimEntity.setClaimStatus(ClaimStatus.PENDING);
        claimEntity.setConsultationAmt(1000);
        claimEntity.setMedicationAmt(100);
        claimEntity.setMedicalTestAmt(100);
        claimEntity.setOtherAmt(100);
        claimEntity.setDiagnosisCodes(Collections.singletonList("DIAG_CODE_12"));
        claimEntity.setSubmissionDateTime(LocalDateTime.now());
        claimEntity.setPayersNric("863743629");
        claimEntity.setPayersName("Mr.Jhon");
        claimEntity.setRemark("Claim is pending");
        claimEntity.setSubmissionResult(new SubmissionResult("CLAIM_NO", "STATUS_CODE", "STATUS DESCRIPTION"));
        claimEntity.setPaidResult(createClaimResult());
        claimEntity.setClaimResult(createClaimResult());
        return claimEntity;
    }


    private ClaimResult createClaimResult() {
        ClaimResult claimResult = new ClaimResult();
        claimResult.setAmount(2000);
        claimResult.setReferenceNumber("REF_NUMBER_100");
        claimResult.setRemark("Last claim");
        claimResult.setResultDateTime(LocalDateTime.now());
        claimResult.setStatusCode("STATUS_111");
        return claimResult;
    }

    private static Package createSamplePackage() {
        Package packageEntity = new Package();
        packageEntity.setItemRefId("ITEM_123");
        packageEntity.setName("PackageName");
        packageEntity.setCode("PKG123");
        packageEntity.setItemRefId("ITEM_123");
        packageEntity.setPackageQty(10);
        packageEntity.setPurchaseDate(LocalDateTime.now());
        packageEntity.setExpireDate(LocalDateTime.now());
        packageEntity.setPurchasePrice(22500);
        packageEntity.setDispatches(Collections.singletonList(createSampleDispatches()));
        packageEntity.setStatus(PackageStatus.ON_GOING);
        return packageEntity;
    }

    private static Dispatch createSampleDispatches() {
        Dispatch dispatchEntity = new Dispatch();
        dispatchEntity.setPurchasedId("SALES_ITEM_CODE");
        dispatchEntity.setItemRefId("SUB_ITEM_REF_ID");
        dispatchEntity.setItemCode("ITEM_CODE");
        dispatchEntity.setItemName("Item Name");
        dispatchEntity.setItemCode("ITEM_CODE");
        dispatchEntity.setUtilizedDate(LocalDateTime.now());
        dispatchEntity.setUtilize(false);
        dispatchEntity.setPayable(false);
        return dispatchEntity;
    }
}
