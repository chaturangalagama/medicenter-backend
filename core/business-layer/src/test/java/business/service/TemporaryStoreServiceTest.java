package business.service;

import business.config.service.SpringTestServiceConfiguration;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.TemporaryStore;
import com.ilt.cms.pm.business.service.clinic.system.TemporaryStoreService;
import com.ilt.cms.repository.clinic.system.TemporaryStoreRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class TemporaryStoreServiceTest {

    @Autowired
    private TemporaryStoreService temporaryStoreService;

    @Autowired
    private TemporaryStoreRepository temporaryStoreRepository;

    @Before
    public void setUp() {
        when(temporaryStoreRepository.findById(anyString())).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String searchStr = invocation.getArgument(0);
                    TemporaryStore temporaryStore = mockTemporyStore();
                    Field name = PersistedObject.class.getDeclaredField("id");
                    name.setAccessible(true);
                    name.set(temporaryStore, searchStr);
                    return Optional.of(temporaryStore);
                }
        );

    }

    private TemporaryStore mockTemporyStore() throws NoSuchFieldException, IllegalAccessException {
        TemporaryStore temporaryStore = new TemporaryStore("key", "value");
        Field id = PersistedObject.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(temporaryStore, "0001");
        return temporaryStore;
    }

    @Test
    public void storeValue() {
        TemporaryStore temporaryStore = temporaryStoreService.storeValue("url", "https://pastebin.com/raw/yVYJD0tY");
        assertNotNull(temporaryStore);
    }

    @Test
    public void retrieveValue() {
        TemporaryStore temporaryStore = temporaryStoreService.retrieveValue("key");
        assertNotNull(temporaryStore);
    }
}
