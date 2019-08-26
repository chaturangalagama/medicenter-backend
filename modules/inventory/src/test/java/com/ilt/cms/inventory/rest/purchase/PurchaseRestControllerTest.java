package com.ilt.cms.inventory.rest.purchase;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.db.service.MongoRunningNumberService;
import com.ilt.cms.inventory.apidoc.PurchaseDocument;
import com.ilt.cms.inventory.db.repository.spring.common.UOMMatrixRepository;
import com.ilt.cms.inventory.db.repository.spring.common.UOMRepository;
import com.ilt.cms.inventory.db.repository.spring.inventory.LocationRepository;
import com.ilt.cms.inventory.db.repository.spring.purchase.OrderRepository;
import com.ilt.cms.inventory.db.repository.spring.purchase.RequestRepository;
import com.ilt.cms.inventory.helper.TestHelper;
import com.ilt.cms.inventory.mock.MockDrugItem;
import com.ilt.cms.inventory.mock.MockLocation;
import com.ilt.cms.inventory.mock.MockPurchaseOrder;
import com.ilt.cms.inventory.mock.MockPurchaseRequest;
import com.ilt.cms.inventory.model.common.UOMMaster;
import com.ilt.cms.inventory.model.common.UomMatrix;
import com.ilt.cms.inventory.model.purchase.PurchaseOrder;
import com.ilt.cms.inventory.model.purchase.PurchaseRequest;

import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;
import com.ilt.cms.repository.spring.ItemRepository;
import lombok.experimental.Helper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
@WithMockUser(username = "DR02", roles = {"PURCHASE"})
public class PurchaseRestControllerTest {

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RequestRepository requestRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UOMMatrixRepository uomMatrixRepository;

    @MockBean
    private UOMRepository uomRepository;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private MongoRunningNumberService mongoRunningNumberService;

    private PurchaseRequest mockDraftPurchaseRequest;

    private PurchaseRequest mockRequestedPurchaseRequest;

    private String draftRequestId;

    private String requestedRequestId;


    @Before
    public void setup() throws Exception {
        draftRequestId = TestHelper.mockObjectId();
        requestedRequestId = TestHelper.mockObjectId();
        mockDraftPurchaseRequest = MockPurchaseRequest.mockPurchaseRequest(draftRequestId, RequestStatus.DRAFT);
        mockRequestedPurchaseRequest = MockPurchaseRequest.mockPurchaseRequest(requestedRequestId, RequestStatus.REQUESTED);

        when(requestRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(MockPurchaseRequest.mockPurchaseRequest(TestHelper.mockObjectId(), RequestStatus.DRAFT)));
        when(requestRepository.save(any(PurchaseRequest.class))).thenAnswer(
          invocationOnMock -> {
              PurchaseRequest purchaseRequest = invocationOnMock.getArgument(0);
              Field id = PersistedObject.class.getDeclaredField("id");
              id.setAccessible(true);
              id.set(purchaseRequest, TestHelper.mockObjectId());
              return purchaseRequest;
          }
        );
        when(requestRepository.findById(draftRequestId)).thenAnswer(
                invocationOnMock -> {
                    String requestId = invocationOnMock.getArgument(0);
                    PurchaseRequest purchaseRequest = MockPurchaseRequest.mockPurchaseRequest(requestId, RequestStatus.DRAFT);

                    return Optional.of(purchaseRequest);
                }
        );
        when(requestRepository.findById(requestedRequestId)).thenAnswer(
                invocationOnMock -> {
                    String requestId = invocationOnMock.getArgument(0);
                    PurchaseRequest purchaseRequest = MockPurchaseRequest.mockPurchaseRequest(requestId, RequestStatus.REQUESTED);

                    return Optional.of(purchaseRequest);
                }
        );
        when(requestRepository.findPurchaseRequest(any(Pageable.class))).thenReturn(
                Arrays.asList(MockPurchaseRequest.mockPurchaseRequest(requestedRequestId, RequestStatus.REQUESTED)));

        when(orderRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(MockPurchaseOrder.mockPurchaseOrder()));
        when(orderRepository.save(any(PurchaseOrder.class))).thenAnswer(
          invocationOnMock -> {
              PurchaseOrder order = invocationOnMock.getArgument(0);
              if(order.getId() == null){
                  order.setId(TestHelper.mockObjectId());
              }
              return order;
          }
        );
        when(orderRepository.findById(anyString())).thenReturn(Optional.of(MockPurchaseOrder.mockPurchaseOrder()));
        when(orderRepository.findPurchaseOrderByClinicId(anyString(), any(Pageable.class))).thenReturn(Arrays.asList(MockPurchaseOrder.mockPurchaseOrder()));
        when(orderRepository.findPurchaseOrder(any(Pageable.class))).thenReturn(Arrays.asList(MockPurchaseOrder.mockPurchaseOrder()));

//        when(uomMatrixRepository.findByUomCode(anyString())).thenReturn(Optional.of(new UomMatrix("BOX",
//                Map.ofEntries(
//                        Map.entry("BOX", new BigDecimal(1)),
//                        Map.entry("TABLET", new BigDecimal(0.5))
//                )
//        )));

        when(mongoRunningNumberService.generateOrderNumber()).thenReturn("12345678");
        when(mongoRunningNumberService.generateRequestNumber()).thenReturn("12345678");
        when(mongoRunningNumberService.generateDeliveryNote()).thenReturn("3225678");
        when(mongoRunningNumberService.generateGRNNumber()).thenReturn("9876543");
        when(itemRepository.findById(anyString())).thenReturn(Optional.of(MockDrugItem.mockDrugItem(Arrays.asList(draftRequestId, requestedRequestId))));
        when(itemRepository.findAllByItemFilterClinicIds(anyString(), any(Sort.class))).thenAnswer(
                invocationOnMock -> {
                    String clinicId = invocationOnMock.getArgument(0);
                    DrugItem item1 = MockDrugItem.mockDrugItem(Arrays.asList(draftRequestId, requestedRequestId));
                    item1.setId("22k32e30023223");
                    item1.setName("Drug 1");
                    item1.getItemFilter().getClinicIds().add(clinicId);
                    DrugItem item2 = MockDrugItem.mockDrugItem(Arrays.asList(draftRequestId, requestedRequestId));
                    item2.setId("434kknd99433n43");
                    item2.setName("Drug 2");
                    item2.getItemFilter().getClinicIds().add(clinicId);
                    return Arrays.asList(item1, item2);

                }
        );
        when(locationRepository.findByClinicId(anyString())).thenReturn(Optional.of(MockLocation.mockLocation()));
        when(locationRepository.findById(anyString())).thenReturn(Optional.of(MockLocation.mockLocation()));
        when(uomRepository.findByCode(anyString())).thenAnswer(invocationOnMock -> {
            String uomCode =invocationOnMock.getArgument(0);
            return Optional.of(new UOMMaster(uomCode, uomCode));
        });
    }


    @Test
    public void createRequest() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/purchase/create/request")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockPurchaseRequest.mockPurchaseRequest(null, RequestStatus.REQUESTED));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(PurchaseDocument.documentCreateRequest())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void modifyRequest() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/purchase/update/request/{requestId}", draftRequestId)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockPurchaseRequest.mockPurchaseRequest(draftRequestId, RequestStatus.DRAFT));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(PurchaseDocument.documentUpdateRequest())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void approveRequest() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/purchase/approve/request/{requestId}", requestedRequestId)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(PurchaseDocument.documentApproveRequest())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void rejectRequest() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/purchase/reject/request/{requestId}", requestedRequestId)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(PurchaseDocument.documentRejectRequest())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    /*@Test
    public void createOrderByRequestId() throws Exception{
        when(requestRepository.findById(anyString())).thenAnswer(
                invocationOnMock -> {
                    String requestId = invocationOnMock.getArgument(0);
                    PurchaseRequest purchaseRequest = MockPurchaseRequest.mockPurchaseRequest(RequestStatus.REQUESTED);
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(purchaseRequest, requestId);
                    return Optional.of(purchaseRequest);
                }
        );

        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/purchase/create/order/{requestId}", "hkh2234bb32")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(PurchaseDocument.documentCreateOrderByRequestId())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }*/

    /*@Test
    public void createOrder() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/purchase/create/order")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockPurchaseOrder.mockPurchaseOrder());

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(PurchaseDocument.documentCreateOrder())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }*/

    /*@Test
    public void modifyOrder() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/purchase/update/order/{orderId}", "23322998328nnb2j3")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockPurchaseOrder.mockPurchaseOrder());

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(PurchaseDocument.documentUpdateOrder())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }*/

    @Test
    public void addGoodReceivedNote() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/purchase/add/grn/{orderId}", TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockPurchaseOrder.mockGoodReceivedNote(LocalDate.now(),
                LocalDate.now(), TestHelper.mockObjectId(), "100000303", "Item received"));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(PurchaseDocument.documentAddGRN())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void addGoodReceivedVoidNote() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/purchase/add/grvn/{orderId}", TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockPurchaseOrder.mockGoodReceivedVoidNote(LocalDate.now(),
                LocalDate.now(), TestHelper.mockObjectId(), "100000303", "Item received"));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(PurchaseDocument.documentAddGRVN())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }
}
