package com.lippo.cms.inventory;

import com.lippo.cms.inventory.model.InventoryUsage;
import com.lippo.cms.inventory.repository.LegacyInventoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lippo.cms.inventory.TestHelper.TOKEN;
import static com.lippo.cms.inventory.TestHelper.userDetails;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@SpringBootTest
public class InventorySystemTest {
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LegacyInventoryRepository legacyInventoryRepository;

    @Before
    public void setUp() throws Exception {
        TestHelper.mockAuthenticationResponse(restTemplate, userDetails(Arrays.asList()));
        List<InventoryUsage> inventoryUsageList = new ArrayList<>();
        inventoryUsageList.add(new InventoryUsage(InventoryUsage.InventoryType.DRUG, "1", 10));
//        when(legacyInventoryRepository.getInventoryDetailList(anyString(), inventoryUsageList)).thenReturn(inventoryUsageList);
//        when(legacyInventoryRepository.createDrugDispensingList(anyString(), inventoryUsageList)).thenReturn(inventoryUsageList);
    }

    //@Test
    public void getUsage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                .post("/get-usage/{clinicId}", "CLI1000").header("Authorization", TOKEN);

        List<InventoryUsage> inventoryUsageList = new ArrayList<>();
        inventoryUsageList.add(new InventoryUsage(InventoryUsage.InventoryType.DRUG, "1", 10));
        TestHelper.httpRequestBuilder(requestBuilder, Arrays.asList(inventoryUsageList));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(ApiDocument.getInventoryUsage())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    //@Test
    public void updateUsage() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = RestDocumentationRequestBuilders
                .post("/update-usage/{clinicId}", "CLI1000").header("Authorization", TOKEN);

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andDo(ApiDocument.updateInventoryUsage())
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}