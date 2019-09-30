package business.service;

import com.ilt.cms.core.entity.sales.*;
import com.ilt.cms.core.entity.item.Cost;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.database.clinic.system.RunningNumberService;
import com.ilt.cms.database.clinic.inventory.ItemDatabaseService;
import com.ilt.cms.pm.business.service.clinic.billing.InvoiceService;
import com.ilt.cms.repository.patient.patientVisit.PatientVisitRepository;
import com.ilt.cms.repository.clinic.billing.SalesOrderRepository;
import com.lippo.cms.exception.CMSException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static util.Assert.assertThrows;

@RunWith(SpringRunner.class)
public class InvoiceServiceTest {
    private SalesOrderRepository salesOrderRepository = Mockito.mock(SalesOrderRepository.class);
    private RunningNumberService runningNumberService = Mockito.mock(RunningNumberService.class);
    private PatientVisitRepository patientVisitRepository = Mockito.mock(PatientVisitRepository.class);
    private ItemDatabaseService itemDatabaseService = Mockito.mock(ItemDatabaseService.class);

    private InvoiceService invoiceService;

    @Before
    public void setUp() throws CMSException {
        invoiceService = new InvoiceService(
                salesOrderRepository,
                runningNumberService,
                patientVisitRepository,
                itemDatabaseService
        );
        when(salesOrderRepository.findById(anyString())).thenAnswer(invocationOnMock -> {
            String salesOrderId = String.valueOf(invocationOnMock.getArguments()[0]);
            return Optional.of(mockSalesOrderWithId(salesOrderId));
        });

    }

    @Test
    public void calculateDueAmountForCaseTest() throws CMSException {
        int dueAmount = invoiceService.calculateDueAmountForCase("case-1");
        assertEquals("Due amount mismatch!", 0, dueAmount);

        SalesOrder salesOrder = mockSalesOrderWithId("sales-order-2");
        salesOrder.getInvoices().add(new Invoice() {{
            this.setInvoiceTime(LocalDateTime.now());
            this.setPaidAmount(200);
            this.setPayableAmount(300);
            this.setInvoiceType(InvoiceType.DIRECT);
        }});

        when(salesOrderRepository.findById("sales-order-2")).thenReturn(Optional.of(salesOrder));

        dueAmount = invoiceService.calculateDueAmountForCase("case-1");
        assertEquals("Due amount mismatch!", 100, dueAmount);

        assertThrows("Expected exception mismatch!", CMSException.class, () -> {
            try {
                invoiceService.calculateDueAmountForCase(null);
            } catch (CMSException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Test
    public void testMakeDirectPaymentForCase() throws CMSException {

        List<PaymentInfo> paymentInfos = new ArrayList<>();
        paymentInfos.add(new PaymentInfo("456456", Invoice.PaymentMode.CASH, 1000, "4348451", "00002"));
        List<Invoice> invoiceList = new ArrayList<>();

        Invoice invoicePaid = new Invoice();
        invoicePaid.setInvoiceNumber("00001");
        invoicePaid.setInvoiceTime(LocalDateTime.now());
        invoicePaid.setPaidAmount(200);
        invoicePaid.setPayableAmount(500);
        invoicePaid.setInvoiceType(Invoice.InvoiceType.DIRECT);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("00002");
        invoice.setInvoiceTime(LocalDateTime.now());
        invoice.setPaidAmount(0);
        invoice.setPayableAmount(1000);
        invoice.setInvoiceType(Invoice.InvoiceType.DIRECT);
        invoiceList.add(invoice);
        invoiceList.add(invoicePaid);

        SalesOrder salesOrder = mockSalesOrderWithId("sales-order-1");
        salesOrder.setInvoices(invoiceList);

        List<Invoice> invoices = invoiceService.makeDirectPaymentForCase("case-1", paymentInfos);
        for (Invoice finalInvoice : invoices) {
            if (finalInvoice.getInvoiceNumber().equals("00001"))
                assertEquals("Paid amount match for previous invoice!", 200, finalInvoice.getPaidAmount());

            if (finalInvoice.getInvoiceNumber().equals("00002"))
                assertEquals("Paid amount match for new invoice", 1000, finalInvoice.getPaidAmount());
        }
    }

    @Test
    public void testMakeDirectPaymentWithoutInvoiceId() throws CMSException {

        List<PaymentInfo> paymentInfos = new ArrayList<>();
        paymentInfos.add(new PaymentInfo("456456", Invoice.PaymentMode.CASH, 1000, "4348451", null));
        paymentInfos.add(new PaymentInfo("456456", Invoice.PaymentMode.CASH, 300, "4348451", null));
        List<Invoice> invoiceList = new ArrayList<>();

        Invoice invoicePaid = new Invoice();
        invoicePaid.setInvoiceNumber("00001");
        invoicePaid.setInvoiceTime(LocalDateTime.now());
        invoicePaid.setPaidAmount(200);
        invoicePaid.setPayableAmount(500);
        invoicePaid.setInvoiceType(Invoice.InvoiceType.DIRECT);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("00002");
        invoice.setInvoiceTime(LocalDateTime.now());
        invoice.setPaidAmount(0);
        invoice.setPayableAmount(1000);
        invoice.setInvoiceType(Invoice.InvoiceType.DIRECT);
        invoiceList.add(invoice);
        invoiceList.add(invoicePaid);

        SalesOrder salesOrder = mockSalesOrderWithId("sales-order-1");
        salesOrder.setInvoices(invoiceList);

        List<Invoice> invoices = invoiceService.makeDirectPaymentForCase("case-1", paymentInfos);
        for (Invoice finalInvoice : invoices) {
            if (finalInvoice.getInvoiceNumber().equals("00001"))
                assertEquals("Paid amount match for previous invoice!", 500, finalInvoice.getPaidAmount());

            if (finalInvoice.getInvoiceNumber().equals("00002"))
                assertEquals("Paid amount match for new invoice", 1000, finalInvoice.getPaidAmount());
        }
    }

    private List<SalesItem> mockSalesItems() {
        return Arrays.asList(
                new SalesItem() {{
                    this.setItemRefId("item-ref-1");
                    this.setItemType(Item.ItemType.CONSULTATION);
                    this.setCost(new Cost(500, false));
                }},
                new SalesItem() {{
                    this.setItemRefId("item-ref-2");
                    this.setItemType(Item.ItemType.DRUG);
                    this.setCost(new Cost(100, false));
                }},
                new SalesItem() {{
                    this.setItemRefId("item-ref-3");
                    this.setItemType(Item.ItemType.VACCINATION);
                    this.setCost(new Cost(150, false));
                }}
        );
    }

    private List<Invoice> mockInvoices() {
        LocalDateTime today = LocalDateTime.now();
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(
                new Invoice() {{
                    this.setInvoiceTime(LocalDateTime.of(LocalDate.of(today.getYear(), today.getMonth(), 1), LocalTime.MIDNIGHT).plusMinutes(1));
                    this.setPaidAmount(250);
                    this.setPayableAmount(250);
                    this.setInvoiceType(InvoiceType.DIRECT);
                }}
        );

        invoices.add(
                new Invoice() {{
                    this.setInvoiceTime(LocalDateTime.of(LocalDate.of(today.getYear(), today.getMonth(), 5), LocalTime.MIDNIGHT).plusMinutes(1));
                    this.setPaidAmount(200);
                    this.setPayableAmount(200);
                    this.setInvoiceType(InvoiceType.DIRECT);
                }}
        );

        return invoices;
    }

    private SalesOrder mockSalesOrderWithId(String salesOrderId) {
        return new SalesOrder() {{
            this.setId(salesOrderId);
            this.setPurchaseItems(mockSalesItems());
            this.setInvoices(mockInvoices());
            this.setStatus(SalesStatus.OPEN);
        }};
    }
}
