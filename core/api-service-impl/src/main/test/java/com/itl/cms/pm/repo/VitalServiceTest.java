package com.itl.cms.pm.repo;

import com.ilt.cms.core.entity.vital.Vital;
import com.ilt.cms.pm.integration.ApplicationStarter;
import com.ilt.cms.repository.clinic.VitalRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationStarter.class)
@Rollback(false)
public class VitalServiceTest {

    @Autowired
    private VitalRepository vitalRepository;

    @MockBean
    AuditorAware<String> auditorAware;

    @Before
    public void setup() {
        Vital save = vitalRepository.save(new Vital("0001", LocalDateTime.of(2019, 1, 1, 1,
                1), "UD", "001", "comment"));

    }
    @Test
    public void listVitals() {
        List<Vital> timeTaken = vitalRepository.findPatientVitals("0001", LocalDateTime.of(
                2019, 1, 1, 1,1
        ), LocalDateTime.of(2019, 1, 1, 1,
                2), Sort.Order.asc("timeTaken"));
        Assert.assertTrue(timeTaken.size() > 0);
    }
}