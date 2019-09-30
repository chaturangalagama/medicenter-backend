package com.itl.cms.pm.repo;

import com.ilt.cms.db.service.MongoRunningNumberService;
import com.ilt.cms.pm.integration.ApplicationStarter;
import com.lippo.cms.exception.CMSException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationStarter.class)
@Rollback(false)
public class QueueTest {

    @Autowired
    private MongoRunningNumberService mongoRunningNumberService;

    @MockBean
    private AuditorAware<String> auditorAware;
    @Before
    public void setup() {
        Mockito.when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(""));
    }

    @Test
    public void testQueue() throws CMSException {
        long number = mongoRunningNumberService.queueNextNumber("HHD", (byte) 0);
        System.out.println("========================");
        System.out.println("number [" + number + "]");
        System.out.println("========================");
        Assert.assertNotEquals(0, number);
    }
}
