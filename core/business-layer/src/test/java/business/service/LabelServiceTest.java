package business.service;

import business.config.repository.SpringTestRepositoryConfiguration;
import business.config.service.SpringTestServiceConfiguration;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.label.Label;
import com.ilt.cms.pm.business.service.clinic.LabelService;
import com.ilt.cms.repository.clinic.LabelRepository;
import com.lippo.cms.exception.CMSException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(SpringRunner.class)
@Import({SpringTestServiceConfiguration.class, SpringTestRepositoryConfiguration.class})

public class LabelServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(LabelServiceTest.class);


    @Autowired
    private LabelService labelService;
    @Autowired
    private LabelRepository labelRepository;


    @Before
    public void setUp() throws Exception {
        Field id = PersistedObject.class.getDeclaredField("id");
        id.setAccessible(true);
        Label label = new Label("prescription", "<div> <p>Content</p> </div>");
        id.set(label, "0001");
        Mockito.when(labelRepository.findById(anyString())).thenReturn(Optional.of(label));
        Mockito.when(labelRepository.findAll()).thenReturn(Arrays.asList(label, label));
        Mockito.when(labelRepository.save(any(Label.class))).thenReturn(label);
        Mockito.when(labelRepository.checkNameExists(anyString())).thenReturn(false);
        Mockito.when(labelRepository.findByName(anyString())).thenReturn(label);

    }

    @Test
    public void listAll() throws Exception {
        List<Label> labels = labelService.listAll();

        assertEquals(labels.size(), 2);
        assertEquals("0001", labels.get(0).getId());
    }

    @Test
    public void findByName() throws Exception {
        Label label = labelService.findByName("prescription");
        assertEquals("prescription", label.getName());

    }

    @Test
    public void add() throws CMSException {
        Label label = new Label("label 1","<p>Test template</p>");
        Label newLabel = labelService.add(label);
        assertNotNull(newLabel.getId());
    }

    @Test
    public void modify() throws CMSException {
        Label label = new Label("label 1","<p>Test template</p>");
        Label modify = labelService.modify("0001", label);
        assertEquals("0001", modify.getId());
    }
}
