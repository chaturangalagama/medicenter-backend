package business.service;

import com.ilt.cms.core.entity.CopayAmount;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.casem.ItemPriceAdjustment;
import com.ilt.cms.core.entity.casem.SalesOrder;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.pm.business.service.billing.PriceCalculationService;
import com.ilt.cms.pm.business.service.billing.NewCaseService;
import com.ilt.cms.pm.business.service.billing.SalesOrderService;
import com.ilt.cms.repository.spring.CaseRepository;
import com.ilt.cms.repository.spring.ClinicRepository;
import com.ilt.cms.repository.spring.PatientRepository;
import com.ilt.cms.repository.spring.PatientVisitRegistryRepository;
import com.lippo.cms.exception.CMSException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;
import java.util.*;

import static com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeDetailResponse;
import static com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeRequest;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CaseServiceTest {

    public static final int SYSTEM_GST_VALUE = 7;
    private NewCaseService caseService;
    private PriceCalculationService priceCalculationService;
    private CaseRepository caseRepository;
    private SalesOrderService salesOrderService;

    @Before
    public void setUp() throws Exception {

        caseRepository = mock(CaseRepository.class);

        PatientRepository patientRepository = mock(PatientRepository.class);
        ClinicRepository clinicRepository = mock(ClinicRepository.class);

        RunningNumberService runningNumberService = mock(RunningNumberService.class);
        when(runningNumberService.generateInvoiceNumber()).thenReturn(String.valueOf(Math.random()));

        PatientVisitRegistryRepository visitRepository = mock(PatientVisitRegistryRepository.class);
        salesOrderService = mock(SalesOrderService.class);



        priceCalculationService = mock(PriceCalculationService.class);

        caseService = new NewCaseService(caseRepository, patientRepository, clinicRepository, runningNumberService, visitRepository, salesOrderService,
                priceCalculationService);

        caseService.setSystemGstValue(SYSTEM_GST_VALUE);
    }


    @Test
    public void testInvoiceBreakdown() throws CMSException {

        Case aCase = new Case();
        aCase.setSalesOrder(SalesOrder.newSalesOrder(SYSTEM_GST_VALUE, "10000"));
        when(caseRepository.findById(any())).thenReturn(Optional.of(aCase));

        List<ItemChargeDetail> chargeDetails = chargeItemList();

        ItemChargeDetailResponse chargeDetailResponse = new ItemChargeDetailResponse(new HashMap<>(), chargeDetails);

        List<Invoice> invoices = caseService.invoiceBreakdown("CA0001", new ItemChargeRequest(new HashMap<>(), chargeDetails));
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceType() == Invoice.InvoiceType.DIRECT) {
                assertEquals("Direct payment", 294250, invoice.getPayableAmount());
            } else if (invoice.getPlanId().equals("PL001")) {
                assertEquals("PL001 invoice value", 10700, invoice.getPayableAmount());
            } else if (invoice.getPlanId().equals("PL002")) {
                assertEquals("PL002 invoice value", 16050, invoice.getPayableAmount());
            }
        }
    }


    @Test
    public void testInvoiceBreakdownWithLimitExceededPlan() throws CMSException {

        Case aCase = new Case();
        aCase.setSalesOrder(SalesOrder.newSalesOrder(SYSTEM_GST_VALUE, "10000"));
        when(caseRepository.findById(any())).thenReturn(Optional.of(aCase));

        List<ItemChargeDetail> chargeDetails = chargeItemList();

        ItemChargeDetailResponse chargeDetailResponse = new ItemChargeDetailResponse(new HashMap<>(), chargeDetails);

        List<Invoice> invoices = caseService.invoiceBreakdown("CA0001", new ItemChargeRequest(new HashMap<>(), chargeDetails));
        assertEquals("There should be only 2 invoices generated", 2, invoices.size());
        assertFalse("There should be no invoice for PL001", invoices.stream().anyMatch(invoice -> "PL001".equals(invoice.getPlanId())));
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceType() == Invoice.InvoiceType.DIRECT) {
                assertEquals("Direct payment", 304950, invoice.getPayableAmount());
                System.out.println("Direct invoice amount is correct as PL001's amount is charged as direct (294250+10700) : 304950");
            } else if (invoice.getPlanId().equals("PL002")) {
                assertEquals("PL002 invoice value", 16050, invoice.getPayableAmount());
            }
        }
    }

    @Test
    public void testInvoiceBreakdownWithCoPay() throws CMSException {

        Case aCase = new Case();
        aCase.setSalesOrder(SalesOrder.newSalesOrder(SYSTEM_GST_VALUE, "10000"));
        when(caseRepository.findById(any())).thenReturn(Optional.of(aCase));

        List<ItemChargeDetail> chargeDetails = chargeItemList();

        ItemChargeDetailResponse chargeDetailResponse = new ItemChargeDetailResponse(new HashMap<>(), chargeDetails);

        List<Invoice> invoices = caseService.invoiceBreakdown("CA0001", new ItemChargeRequest(new HashMap<>(), chargeDetails));
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceType() == Invoice.InvoiceType.DIRECT) {
                assertEquals("Direct payment", 291575, invoice.getPayableAmount());
            } else if (invoice.getPlanId().equals("PL-COPAY-01")) {
                assertEquals("PL001 invoice value", 14980, invoice.getPayableAmount());
            } else if (invoice.getPlanId().equals("PL-COPAY-02")) {
                assertEquals("PL002 invoice value", 14445, invoice.getPayableAmount());
            }
        }
    }

    @Test
    public void testInvoiceBreakdownDirectOnly() throws CMSException {

        Case aCase = new Case();
        aCase.setSalesOrder(SalesOrder.newSalesOrder(SYSTEM_GST_VALUE, "10000"));
        when(caseRepository.findById(any())).thenReturn(Optional.of(aCase));

        List<ItemChargeDetail> chargeDetails = chargeItemList();
        ItemChargeDetailResponse chargeDetailResponse = new ItemChargeDetailResponse(new HashMap<>(), chargeDetails);

        List<Invoice> invoices = caseService.invoiceBreakdown("CA0001", new ItemChargeRequest(new HashMap<>(), chargeDetails));
        assertEquals("Only one invoice", 1, invoices.size());
        Invoice directInvoice = invoices.get(0);
        assertSame(directInvoice.getInvoiceType(), Invoice.InvoiceType.DIRECT);
        assertEquals("Total amount", 321000, directInvoice.getPayableAmount());
    }

    @Test
    public void testRemoveInvoicesForSingleVisit() throws CMSException {
        Case aCase = new Case();
        aCase.setSalesOrder(SalesOrder.newSalesOrder(SYSTEM_GST_VALUE, "10000"));

        Invoice invoice = new Invoice();
        invoice.setVisitId("V0001");
        invoice.setTaxAmount(10);
        invoice.setInvoiceTime(LocalDateTime.now());
        invoice.setInvoiceType(Invoice.InvoiceType.DIRECT);
        invoice.setPaidAmount(1000);
        invoice.setPayableAmount(1000);
        invoice.setPaidTime(LocalDateTime.now());
        invoice.setPaymentInfos(Collections.emptyList());
        invoice.setPlanId("PLAN_ID_1111");
        invoice.setInvoiceNumber("123456677");

        aCase.getSalesOrder().setInvoices(Arrays.asList(invoice));
        when(caseRepository.findById(any())).thenReturn(Optional.of(aCase));
        when(salesOrderService.updateSalesOrder(any(SalesOrder.class))).thenReturn(aCase.getSalesOrder());

        List<Invoice> invoices = caseService.removeInvoicesForVisit("CASE0001", "V0001");
        assertEquals("Invoice should be empty", 0, invoices.size());

    }

    @Test
    public void testRemoveInvoicesForMultipleVisit() throws CMSException {
        Case aCase = new Case();
        aCase.setSalesOrder(SalesOrder.newSalesOrder(SYSTEM_GST_VALUE, "10000"));

        List<Invoice> invoiceList = new ArrayList<>();
        Invoice invoiceOne = new Invoice();
        invoiceOne.setVisitId("V0001");
        invoiceOne.setTaxAmount(10);
        invoiceOne.setInvoiceTime(LocalDateTime.now());
        invoiceOne.setInvoiceType(Invoice.InvoiceType.DIRECT);
        invoiceOne.setPaidAmount(1000);
        invoiceOne.setPayableAmount(1000);
        invoiceOne.setPaidTime(LocalDateTime.now());
        invoiceOne.setPaymentInfos(Collections.emptyList());
        invoiceOne.setPlanId("PLAN_ID_1111");
        invoiceOne.setInvoiceNumber("123456677");
        invoiceList.add(invoiceOne);

        Invoice invoiceTwo = new Invoice();
        invoiceTwo.setVisitId("V0002");
        invoiceTwo.setTaxAmount(10);
        invoiceTwo.setInvoiceTime(LocalDateTime.now());
        invoiceTwo.setInvoiceType(Invoice.InvoiceType.DIRECT);
        invoiceTwo.setPaidAmount(1000);
        invoiceTwo.setPayableAmount(1000);
        invoiceTwo.setPaidTime(LocalDateTime.now());
        invoiceTwo.setPaymentInfos(Collections.emptyList());
        invoiceTwo.setPlanId("PLAN_ID_1111");
        invoiceTwo.setInvoiceNumber("123456677");
        invoiceList.add(invoiceTwo);


        aCase.getSalesOrder().setInvoices(invoiceList);
        when(caseRepository.findById(any())).thenReturn(Optional.of(aCase));
        when(salesOrderService.updateSalesOrder(any(SalesOrder.class))).thenReturn(aCase.getSalesOrder());

        List<Invoice> invoices = caseService.removeInvoicesForVisit("CASE0001", "V0001");
        assertEquals("Invoice should have one ", 1, invoices.size());
        assertNotEquals("V0001", invoices.get(0).getVisitId());

    }

    private List<ItemChargeDetail> chargeItemList() {
        ItemChargeDetail item1 = new ItemChargeDetail("I0001", 100, new Charge(1000, false),
                new ItemPriceAdjustment());
        ItemChargeDetail item2 = new ItemChargeDetail("I0002", 100, new Charge(1000, false),
                new ItemPriceAdjustment());
        ItemChargeDetail item3 = new ItemChargeDetail("I0003", 100, new Charge(1000, false),
                new ItemPriceAdjustment());

        return Arrays.asList(item1, item2, item3);
    }

   /* @Test
    public void testInvoiceBreakdownForMultipleVisitsWithCreditPayment() throws CMSException {

        Case aCase = new Case();
        aCase.setSalesOrder(SalesOrder.newSalesOrder(SYSTEM_GST_VALUE, "10000"));
        aCase.setAttachedMedicalCoverages(Arrays.asList(new AttachedMedicalCoverage("PL001"), new AttachedMedicalCoverage("PL002")));
        when(caseRepository.findById(any())).thenReturn(Optional.of(aCase));

        Invoice invoiceOne = new Invoice();
        invoiceOne.setVisitId("V0001");
        invoiceOne.setTaxAmount(700);
        invoiceOne.setInvoiceTime(LocalDateTime.now());
        invoiceOne.setInvoiceType(Invoice.InvoiceType.CREDIT);
        invoiceOne.setPaidAmount(0);
        invoiceOne.setPayableAmount(10700);
        invoiceOne.setPaidTime(LocalDateTime.now());
        invoiceOne.setPaymentInfos(Collections.emptyList());
        invoiceOne.setPlanId("PL001");
        invoiceOne.setInvoiceNumber("123456677");

        aCase.getSalesOrder().setInvoices(Arrays.asList(invoiceOne));

        List<ItemChargeDetail> chargeDetails = chargeItemList();

        ItemChargeDetailResponse chargeDetailResponse = new ItemChargeDetailResponse(new HashMap<>(), chargeDetails);
        when(priceCalculationService.calculateSalesPrice(any(), any(), any())).thenReturn(chargeDetailResponse);

        List<Invoice> invoices = caseService.invoiceBreakdown("CA0001", new ItemChargeRequest(new HashMap<>(), chargeDetails));
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceType() == Invoice.InvoiceType.DIRECT) {
                assertEquals("Direct payment", 294250, invoice.getPayableAmount());
            } else if (invoice.getPlanId().equals("PL001")) {
                assertEquals("PL001 invoice value", 10700, invoice.getPayableAmount());
            } else if (invoice.getPlanId().equals("PL002")) {
                assertEquals("PL002 invoice value", 16050, invoice.getPayableAmount());
            }
        }
    }

    @Test
    public void testInvoiceBreakdownForMultipleVisitsWithDirectPayment() throws CMSException {

        Case aCase = new Case();
        aCase.setSalesOrder(SalesOrder.newSalesOrder(SYSTEM_GST_VALUE, "10000"));
        aCase.setAttachedMedicalCoverages(Arrays.asList(new AttachedMedicalCoverage("PL001"), new AttachedMedicalCoverage("PL002")));
        when(caseRepository.findById(any())).thenReturn(Optional.of(aCase));

        Invoice creditInvoice = new Invoice();
        creditInvoice.setVisitId("V0001");
        creditInvoice.setTaxAmount(700);
        creditInvoice.setInvoiceTime(LocalDateTime.now());
        creditInvoice.setInvoiceType(Invoice.InvoiceType.CREDIT);
        creditInvoice.setPaidAmount(0);
        creditInvoice.setPayableAmount(10700);
        creditInvoice.setPaidTime(LocalDateTime.now());
        creditInvoice.setPaymentInfos(Collections.emptyList());
        creditInvoice.setPlanId("PL001");
        creditInvoice.setInvoiceNumber("123456677");

        Invoice directInvoice = new Invoice();
        directInvoice.setVisitId("V0001");
        directInvoice.setTaxAmount(700);
        directInvoice.setInvoiceTime(LocalDateTime.now());
        directInvoice.setInvoiceType(Invoice.InvoiceType.DIRECT);
        directInvoice.setPaidAmount(3000);
        directInvoice.setPayableAmount(10700);
        directInvoice.setPaidTime(LocalDateTime.now());
        directInvoice.setPaymentInfos(Collections.emptyList());
        directInvoice.setInvoiceNumber("123456677");

        List<Invoice> initInvoices = new ArrayList<>();
        initInvoices.add(creditInvoice);
        initInvoices.add(directInvoice);
        aCase.getSalesOrder().setInvoices(initInvoices);

        List<ItemChargeDetail> chargeDetails = chargeItemList();

        ItemChargeDetailResponse chargeDetailResponse = new ItemChargeDetailResponse(new HashMap<>(), chargeDetails);
        when(priceCalculationService.calculateSalesPrice(any(), any(), any())).thenReturn(chargeDetailResponse);

        List<Invoice> invoices = caseService.invoiceBreakdown("CA0001", new ItemChargeRequest(new HashMap<>(), chargeDetails));
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceType() == Invoice.InvoiceType.DIRECT) {
                assertEquals("Direct payment", 291250, invoice.getPayableAmount());
            } else if (invoice.getPlanId().equals("PL001")) {
                assertEquals("PL001 invoice value", 10700, invoice.getPayableAmount());
            } else if (invoice.getPlanId().equals("PL002")) {
                assertEquals("PL002 invoice value", 16050, invoice.getPayableAmount());
            }
        }
    }

    @Test
    public void testInvoiceBreakdownForMultipleVisitsWithCreditPaymentCopay() throws CMSException {

        Case aCase = new Case();
        aCase.setSalesOrder(SalesOrder.newSalesOrder(SYSTEM_GST_VALUE, "10000"));
        aCase.setAttachedMedicalCoverages(Arrays.asList(new AttachedMedicalCoverage("PL-COPAY-01"), new AttachedMedicalCoverage("PL-COPAY-02")));
        when(caseRepository.findById(any())).thenReturn(Optional.of(aCase));

        Invoice invoiceOne = new Invoice();
        invoiceOne.setVisitId("V0001");
        invoiceOne.setTaxAmount(980);
        invoiceOne.setInvoiceTime(LocalDateTime.now());
        invoiceOne.setInvoiceType(Invoice.InvoiceType.CREDIT);
        invoiceOne.setPaidAmount(0);
        invoiceOne.setPayableAmount(14980);
        invoiceOne.setPaidTime(LocalDateTime.now());
        invoiceOne.setPaymentInfos(Collections.emptyList());
        invoiceOne.setPlanId("PL-COPAY-01");
        invoiceOne.setInvoiceNumber("123456677");

        Invoice invoiceTwo = new Invoice();
        invoiceTwo.setVisitId("V0001");
        invoiceTwo.setTaxAmount(945);
        invoiceTwo.setInvoiceTime(LocalDateTime.now());
        invoiceTwo.setInvoiceType(Invoice.InvoiceType.CREDIT);
        invoiceTwo.setPaidAmount(0);
        invoiceTwo.setPayableAmount(14445);
        invoiceTwo.setPaidTime(LocalDateTime.now());
        invoiceTwo.setPaymentInfos(Collections.emptyList());
        invoiceTwo.setPlanId("PL-COPAY-02");
        invoiceTwo.setInvoiceNumber("123456677");

        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(invoiceOne);
        invoiceList.add(invoiceTwo);

        aCase.getSalesOrder().setInvoices(invoiceList);

        List<ItemChargeDetail> chargeDetails = chargeItemList();

        ItemChargeDetailResponse chargeDetailResponse = new ItemChargeDetailResponse(new HashMap<>(), chargeDetails);
        when(priceCalculationService.calculateSalesPrice(any(), any(), any())).thenReturn(chargeDetailResponse);

        List<Invoice> invoices = caseService.invoiceBreakdown("CA0001", new ItemChargeRequest(new HashMap<>(), chargeDetails));
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceType() == Invoice.InvoiceType.DIRECT) {
                assertEquals("Direct payment", 291575, invoice.getPayableAmount());
            } else if (invoice.getPlanId().equals("PL-COPAY-01")) {
                assertEquals("PL001 invoice value", 14980, invoice.getPayableAmount());
            } else if (invoice.getPlanId().equals("PL-COPAY-02")) {
                assertEquals("PL002 invoice value", 14445, invoice.getPayableAmount());
            }
        }
    }*/
}
