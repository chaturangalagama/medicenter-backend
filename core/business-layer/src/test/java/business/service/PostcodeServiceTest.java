package business.service;

import business.config.service.SpringTestServiceConfiguration;
import com.ilt.cms.core.entity.Postcode;
import com.ilt.cms.database.PostcodeDatabaseService;
import com.ilt.cms.pm.business.service.PostcodeService;
import com.ilt.cms.repository.spring.PostcodeRepository;
//import jdk.jfr.Unsigned;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class PostcodeServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(PostcodeServiceTest.class);

    @Autowired
    private PostcodeService postcodeService;
    @Autowired
    private PostcodeRepository postcodeRepository;

    @Before
    public void setUp() throws Exception {

        Postcode postcode = mockPostcode();
        when(postcodeRepository.findFirstByCode(anyString())).thenAnswer(
                (Answer<Postcode>) invocation -> {
                    String searchStr = invocation.getArgument(0);
                    Postcode postcode1 = mockPostcode();
                    Field name = Postcode.class.getDeclaredField("code");
                    name.setAccessible(true);
                    name.set(postcode1, searchStr);
                    return postcode1;
                }
        );

    }

    private Postcode mockPostcode(){
        Postcode postcode = new Postcode("code1", "93 Kallang Way 3, 349110, Singapore");
        return postcode;
    }

    @Test
    public void findPostcode() {
        Postcode postcode = postcodeService.findPostcode("ABC");
        assertNotNull(postcode);
    }
}
