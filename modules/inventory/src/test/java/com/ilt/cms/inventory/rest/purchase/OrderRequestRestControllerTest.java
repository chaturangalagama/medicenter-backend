package com.ilt.cms.inventory.rest.purchase;

import com.ilt.cms.inventory.apidoc.OrderRequestDocument;
import com.ilt.cms.inventory.db.repository.spring.purchase.OrderRepository;
import com.ilt.cms.inventory.db.repository.spring.purchase.RequestRepository;
import com.ilt.cms.inventory.helper.TestHelper;
import com.ilt.cms.inventory.mock.*;
import com.ilt.cms.inventory.model.purchase.PurchaseRequest;
import com.ilt.cms.inventory.model.purchase.api.OrderRequest;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;
import com.ilt.cms.repository.spring.ClinicRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
@WithMockUser(username = "DR02", roles = {"VIEW_ORDER_REQUEST"})
public class OrderRequestRestControllerTest {

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RequestRepository requestRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ClinicRepository clinicRepository;


    @Before
    public void setup() throws Exception {

        when(requestRepository.findTransferRequest(any(Pageable.class))).thenReturn(Arrays.asList(MockTransferRequest.mockTransferRequest(TestHelper.mockObjectId(), RequestStatus.REQUESTED)));
        when(requestRepository.findPurchaseRequest(any(Pageable.class))).thenReturn(Arrays.asList(MockPurchaseRequest.mockPurchaseRequest(TestHelper.mockObjectId(), RequestStatus.REQUESTED)));
        when(requestRepository.findPurchaseRequestByClinicId(anyString(), any(Pageable.class))).thenReturn(Arrays.asList(MockPurchaseRequest.mockPurchaseRequest(TestHelper.mockObjectId(), RequestStatus.REQUESTED)));
        when(requestRepository.findTransferRequestByClinicId(anyString(), any(Pageable.class))).thenReturn(Arrays.asList(MockTransferRequest.mockTransferRequest(TestHelper.mockObjectId(), RequestStatus.REQUESTED)));

        when(orderRepository.findTransferOrder(any(Pageable.class))).thenReturn(Arrays.asList(MockTransferOrder.mockTransferOrder()));
        when(orderRepository.findPurchaseOrder(any(Pageable.class))).thenReturn(Arrays.asList(MockPurchaseOrder.mockPurchaseOrder()));
        when(orderRepository.findPurchaseOrderByClinicId(anyString(), any(Pageable.class))).thenReturn(Arrays.asList(MockPurchaseOrder.mockPurchaseOrder()));
        when(orderRepository.findTransferOrderByClinicId(anyString(), any(Pageable.class))).thenReturn(Arrays.asList(MockTransferOrder.mockTransferOrder()));

        when(clinicRepository.findAll()).thenReturn(Arrays.asList(
                MockClinic.mockClinic("C023232", "ABSOLUTE MS PTE LTD"),
                MockClinic.mockClinic("S000212", "ABSOLUTE SS PTE LTD")
        ));

    }

    @Test
    public void listOrderRequestList() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/order-request/list/{clinicId}", "4jn609lf4h3224343")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        Map<OrderRequest.Filter, String> filterMap = new HashMap<>();
        filterMap.put(OrderRequest.Filter.RECEIVED_DATE_MORE, "31-12-2010");
        filterMap.put(OrderRequest.Filter.RECEIVED_DATE_LESS, "31-12-2021");

        TestHelper.httpRequestBuilder(requestBuilder, filterMap);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(OrderRequestDocument.documentListOrderRequest())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }
}
