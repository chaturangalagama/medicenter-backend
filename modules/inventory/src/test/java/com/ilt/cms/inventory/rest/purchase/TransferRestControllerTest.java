package com.ilt.cms.inventory.rest.purchase;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.db.service.MongoRunningNumberService;
import com.ilt.cms.inventory.apidoc.PurchaseDocument;
import com.ilt.cms.inventory.apidoc.TransferDocument;
import com.ilt.cms.inventory.db.repository.spring.common.UOMMatrixRepository;
import com.ilt.cms.inventory.db.repository.spring.common.UOMRepository;
import com.ilt.cms.inventory.db.repository.spring.inventory.LocationRepository;
import com.ilt.cms.inventory.db.repository.spring.purchase.OrderRepository;
import com.ilt.cms.inventory.db.repository.spring.purchase.RequestRepository;
import com.ilt.cms.inventory.helper.TestHelper;
import com.ilt.cms.inventory.mock.*;
import com.ilt.cms.inventory.model.common.UOMMaster;
import com.ilt.cms.inventory.model.common.UomMatrix;
import com.ilt.cms.inventory.model.purchase.*;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;
import com.ilt.cms.repository.spring.ItemRepository;
import org.junit.Before;
import org.junit.Ignore;
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
@WithMockUser(username = "DR02", roles = {"TRANSFER"})
public class TransferRestControllerTest {

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

    private TransferRequest mockDraftTransferRequest;

    private TransferRequest mockRequestedtransferRequest;

    private String draftRequestId;

    private String requestedRequestId;

    private String itemRefId;

    private String batchNo;

    @Before
    public void setup() throws Exception {
        itemRefId = TestHelper.mockObjectId();
        draftRequestId = TestHelper.mockObjectId();
        requestedRequestId = TestHelper.mockObjectId();
        batchNo = "BN-9001";
        mockDraftTransferRequest = MockTransferRequest.mockTransferRequest(draftRequestId, RequestStatus.DRAFT);
        mockRequestedtransferRequest = MockTransferRequest.mockTransferRequest(requestedRequestId, RequestStatus.REQUESTED);

        when(requestRepository.findAll(any(Sort.class))).thenReturn(
                Arrays.asList(MockTransferRequest.mockTransferRequest(requestedRequestId, RequestStatus.REQUESTED)));
        when(requestRepository.save(any(TransferRequest.class))).thenAnswer(
                invocationOnMock -> {
                    TransferRequest transferRequest = invocationOnMock.getArgument(0);
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(transferRequest, "29938329231n32i");
                    return transferRequest;
                }
        );
        when(requestRepository.findById(draftRequestId)).thenAnswer(
                invocationOnMock -> {
                    String requestId = invocationOnMock.getArgument(0);
                    TransferRequest transferRequest = MockTransferRequest.mockTransferRequest(draftRequestId, RequestStatus.DRAFT);
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(transferRequest, requestId);
                    return Optional.of(transferRequest);
                }
        );
        when(requestRepository.findById(requestedRequestId)).thenAnswer(
                invocationOnMock -> {
                    String requestId = invocationOnMock.getArgument(0);
                    TransferRequest transferRequest = MockTransferRequest.mockTransferRequest(requestedRequestId, RequestStatus.REQUESTED);
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(transferRequest, requestId);
                    return Optional.of(transferRequest);
                }
        );
        when(requestRepository.findTransferRequest(any(Pageable.class))).thenReturn(
                Arrays.asList(MockTransferRequest.mockTransferRequest(requestedRequestId, RequestStatus.REQUESTED)));

        when(orderRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(MockTransferOrder.mockTransferOrder()));
        when(orderRepository.save(any(TransferOrder.class))).thenAnswer(
                invocationOnMock -> {
                    TransferOrder order = invocationOnMock.getArgument(0);
                    if(order.getId() == null){
                        order.setId(TestHelper.mockObjectId());
                    }
                    return order;
                }
        );
        when(orderRepository.findById(anyString())).thenAnswer(
                invocationOnMock -> {
                    String id = invocationOnMock.getArgument(0);
                    TransferOrder transferOrder = MockTransferOrder.mockTransferOrder();
                    Field idField = PersistedObject.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(transferOrder, id);
                    GoodReceiveNote goodReceiveNote = MockTransferOrder.mockGoodReceivedNote(LocalDate.now(), LocalDate.now(),
                            TestHelper.mockObjectId(), "28937943", "Return item");
                    goodReceiveNote.setReceivedItems(Arrays.asList(MockTransferOrder.mockGoodReceivedItem(
                            TestHelper.mockObjectId(), itemRefId, "BOX", 10, batchNo, LocalDate.of(2012, 10, 21), "expiry", false)
                    ));
                    transferOrder.getGoodReceiveNotes().add(goodReceiveNote);
                    return Optional.of(transferOrder);
                }
        );
        when(orderRepository.findTransferOrderByClinicId(anyString(), any(Pageable.class))).thenReturn(Arrays.asList(MockTransferOrder.mockTransferOrder()));
        when(orderRepository.findTransferOrder(any(Pageable.class))).thenReturn(Arrays.asList(MockTransferOrder.mockTransferOrder()));

//        when(uomMatrixRepository.findByUomCode(anyString())).thenReturn(Optional.of(new UomMatrix("BOX",
//                Map.ofEntries(
//                        Map.entry("BOX", new BigDecimal(1)),
//                        Map.entry("TABLET", new BigDecimal(0.5))
//                )
//        )));

        when(mongoRunningNumberService.generateOrderNumber()).thenReturn("111111");
        when(mongoRunningNumberService.generateRequestNumber()).thenReturn("222222");
        when(mongoRunningNumberService.generateDeliveryNote()).thenReturn("333333");
        when(mongoRunningNumberService.generateGRNNumber()).thenReturn("444444");
        when(mongoRunningNumberService.generateDeliveryVoidNote()).thenReturn("555555");
        when(itemRepository.findById(anyString())).thenReturn(Optional.of(MockDrugItem.mockDrugItem(Arrays.asList(draftRequestId, requestedRequestId))));
        when(itemRepository.findAllByItemFilterClinicIds(anyString(), any(Sort.class))).thenAnswer(
                invocationOnMock -> {
                    String clinicId = invocationOnMock.getArgument(0);
                    DrugItem item1 = MockDrugItem.mockDrugItem(Arrays.asList(draftRequestId, requestedRequestId));
                    item1.setId("22k32e30023223");
                    item1.setName("Drug 1");
                item1.getItemFilter().getClinicIds().add(clinicId);
                    DrugItem item2 = MockDrugItem.mockDrugItem(Arrays.asList(draftRequestId, requestedRequestId));
                    item2.setId("22k32e30sfe33223");
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
                RestDocumentationRequestBuilders.post("/transfer/create/request")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockTransferRequest.mockTransferRequest(null, RequestStatus.REQUESTED));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentCreateRequest())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void modifyRequest() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/transfer/update/request/{requestId}", draftRequestId)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockTransferRequest.mockTransferRequest(draftRequestId, RequestStatus.DRAFT));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentUpdateRequest())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void approveRequest() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/transfer/approve/request/{requestId}", requestedRequestId)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentApproveRequest())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void rejectRequest() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/transfer/reject/request/{requestId}", requestedRequestId)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentRejectRequest())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    /*@Test
    public void createOrder() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/transfer/create/order")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockTransferOrder.mockTransferOrder());

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentCreateOrder())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }*/

    /*@Test
    @Ignore
    public void createOrderByRequestId() throws Exception{
        when(requestRepository.findById(anyString())).thenAnswer(
                invocationOnMock -> {
                    String requestId = invocationOnMock.getArgument(0);
                    TransferRequest transferRequest = MockTransferRequest.mockTransferRequest(RequestStatus.REQUESTED);
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(transferRequest, requestId);
                    return Optional.of(transferRequest);
                }
        );
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/transfer/create/order/{requestId}", requestedRequestId)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentCreateOrderByRequestId())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void modifyOrder() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/transfer/update/order/{orderId}", "23322998328nnb2j3")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockTransferOrder.mockTransferOrder());

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentUpdateOrder())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }*/

    @Test
    public void addGoodReceivedNote() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/transfer/add/grn/{orderId}", TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockTransferOrder.mockGoodReceivedNote(LocalDate.now(),
                LocalDate.now(), TestHelper.mockObjectId(), "100000303", "Item received"));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentAddGRN())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void addGoodReceivedVoidNote() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/transfer/add/grvn/{orderId}", TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        GoodReceiveVoidNote goodReceiveVoidNote = MockTransferOrder.mockGoodReceivedVoidNote(LocalDate.now(),
                LocalDate.now(), TestHelper.mockObjectId(), "34399454", "Item received");
        goodReceiveVoidNote.setReturnItems(Arrays.asList(
                MockTransferOrder.mockGoodReturnItem(TestHelper.mockObjectId(), itemRefId, "BOX", 10,
                        batchNo, LocalDate.of(2012, 2, 23), "Expired", false)
        ));

        TestHelper.httpRequestBuilder(requestBuilder, goodReceiveVoidNote);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentAddGRVN())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void addDeliveryNote() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/transfer/add/dn/{orderId}", TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockTransferOrder.mockDeliveryNote(TestHelper.mockObjectId(),
                TestHelper.mockObjectId(), LocalDate.now(), LocalDate.now(), TestHelper.mockObjectId()));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentAddDN())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void addDeliveryVoidNote() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/transfer/add/dvn/{orderId}", TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());


        TestHelper.httpRequestBuilder(requestBuilder, MockTransferOrder.mockDeliveryVoidNote(TestHelper.mockObjectId(),
                TestHelper.mockObjectId(), LocalDate.now(), LocalDate.now(), TestHelper.mockObjectId()));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(TransferDocument.documentAddDVN())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }
}
