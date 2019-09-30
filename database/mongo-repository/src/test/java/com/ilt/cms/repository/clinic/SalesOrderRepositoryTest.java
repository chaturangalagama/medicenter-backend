package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.sales.Invoice;
import com.ilt.cms.core.entity.sales.SalesOrder;
import com.ilt.cms.repository.clinic.billing.SalesOrderRepository;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.FieldDefinitionBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import io.github.benas.randombeans.randomizers.collection.ListRandomizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest(
        includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = {"com.ilt.cms.repository.spring.*"}))
public class SalesOrderRepositoryTest {


    private static AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SalesOrderRepository salesOrderRepository;


    private Randomizer<SalesOrder> salesOrderRandomizer;
    private Randomizer<Invoice> invoiceRandomizer;

    @Before
    public void setUp() throws Exception {
        invoiceRandomizer = new Randomizer() {
            private EnhancedRandom invoiceRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

            @Override
            public Invoice getRandomValue() {
                Invoice invoice = invoiceRandom.nextObject(Invoice.class);
                invoice.setInvoiceType(Invoice.InvoiceType.CREDIT);
                invoice.setInvoiceTime(LocalDate.now().atStartOfDay());
                return invoice;
            }
        };

        salesOrderRandomizer = new Randomizer() {
            EnhancedRandom salesOrderRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
                    .randomizationDepth(2)
                    .exclude(FieldDefinitionBuilder.field().named("purchaseItem").get())
                    .exclude(FieldDefinitionBuilder.field().named("id").get())
                    .randomize(FieldDefinitionBuilder.field()
                            .named("invoices")
                            .ofType(List.class)
                            .inClass(SalesOrder.class).get(), new ListRandomizer(invoiceRandomizer, 4))
                    .build();

            @Override
            public SalesOrder getRandomValue() {
                return salesOrderRandom.nextObject(SalesOrder.class);
            }
        };
        mongoTemplate.dropCollection(SalesOrder.class);
    }

    @Test
    public void findSalesOrdersWithInvoicesCreatedInSearchPeriodWithVisitIds() {
        initializeContext();
        SalesOrder salesOrder = salesOrderRandomizer.getRandomValue();
        salesOrder.getInvoices().stream().sequential()
                .forEach(invoice -> {
                    invoice.setPlanId("TEST-COVERAGE-PLAN-00" + counter.incrementAndGet());
                    invoice.setVisitId("TEST-VISIT-00" + counter.get());
                    invoice.setInvoiceTime(LocalDate.now().atStartOfDay());
                });

        mongoTemplate.save(salesOrder);

        List<SalesOrder> salesOrders = salesOrderRepository
                .findSalesOrdersWithInvoicesCreatedInSearchPeriodWithVisitIds(Arrays.asList("TEST-VISIT-001"),
                        LocalDate.now().atStartOfDay().minusDays(2),
                        LocalDate.now().atStartOfDay().plusDays(1));

        assertNotNull("SalesOrder was not found", salesOrders);
        assertEquals("SalesOrder was not found", 1, salesOrders.size());
        Optional<Invoice> invoiceOptional = salesOrders.get(0).getInvoices()
                .stream().filter(invoice -> invoice.getVisitId().equals("TEST-VISIT-001"))
                .findFirst();
        assertTrue(invoiceOptional.isPresent());
        assertEquals("Plan id was different", "TEST-COVERAGE-PLAN-001", invoiceOptional.get().getPlanId());
        assertEquals("Visit id was different", "TEST-VISIT-001", invoiceOptional.get().getVisitId());

    }

    private void initializeContext() {
        mongoTemplate.dropCollection(SalesOrder.class);
        counter.set(0);
    }
}