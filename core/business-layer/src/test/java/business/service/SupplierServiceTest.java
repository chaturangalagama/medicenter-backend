package business.service;

import business.config.service.SpringTestServiceConfiguration;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.common.ContactPerson;
import com.ilt.cms.core.entity.common.CorporateAddress;
import com.ilt.cms.core.entity.supplier.Supplier;
import com.ilt.cms.pm.business.service.clinic.inventory.SupplierService;
import com.ilt.cms.repository.clinic.inventory.SupplierRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class SupplierServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(SupplierServiceTest.class);

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Before
    public void setUp() throws Exception {


        Supplier supplier1 = mockSupplier();
        Supplier supplier2 = mockSupplier();
        Field name = Supplier.class.getDeclaredField("name");
        name.setAccessible(true);
        name.set(supplier2, "Supplier 2");

        Field id = PersistedObject.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(supplier1, "0001");
        id.set(supplier2, "0002");

        when(supplierRepository.findAll()).thenReturn(Arrays.asList(supplier1, supplier2));
    }

    private Supplier mockSupplier() throws NoSuchFieldException, IllegalAccessException {
        Supplier supplier1 = new Supplier("Supplier 1", new CorporateAddress("Mr Boss",
                "Street 11", "09987"), Arrays.asList(
                new ContactPerson("Mr James", "Admin", "650099222",
                        "659800021", "650882223", "me@me.com")), Status.ACTIVE);
        Field id = PersistedObject.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(supplier1, "0001");
        return supplier1;
    }


    @Test
    public void listAll() {
        List<Supplier> suppliers = supplierService.listAll();
        assertEquals(2, suppliers.size());
    }
}
