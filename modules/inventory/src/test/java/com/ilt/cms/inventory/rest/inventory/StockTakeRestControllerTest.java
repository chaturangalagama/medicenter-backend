package com.ilt.cms.inventory.rest.inventory;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.inventory.apidoc.StockDocument;
import com.ilt.cms.inventory.db.repository.spring.inventory.LocationRepository;
import com.ilt.cms.inventory.db.repository.spring.inventory.StockTakeRepository;
import com.ilt.cms.inventory.helper.TestHelper;
import com.ilt.cms.inventory.mock.MockClinic;
import com.ilt.cms.inventory.mock.MockDrugItem;
import com.ilt.cms.inventory.mock.MockLocation;
import com.ilt.cms.inventory.mock.MockStockTake;
import com.ilt.cms.inventory.model.inventory.Inventory;
import com.ilt.cms.inventory.model.inventory.Location;
import com.ilt.cms.inventory.model.inventory.StockCountItem;
import com.ilt.cms.inventory.model.inventory.StockTake;
import com.ilt.cms.inventory.model.inventory.api.AdjustmentStockTake;
import com.ilt.cms.inventory.model.inventory.enums.*;
import com.ilt.cms.repository.spring.ClinicRepository;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
@WithMockUser(username = "DR02", roles = {"INVENTORY"})
public class StockTakeRestControllerTest {

    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private StockTakeRepository stockTakeRepository;

    @MockBean
    private ClinicRepository clinicRepository;

    private Location mockLocation;

    private DrugItem mockDrugItem;

    @Before
    public void setup() throws Exception {
        String itemRefId = TestHelper.mockObjectId();
        String batchNumber = "BN-00001";
        mockLocation = MockLocation.mockLocation();
        List<Inventory> inventories = new ArrayList<>(
                Arrays.asList(
                        MockLocation.mockInventory(itemRefId, batchNumber, "BOX",
                                LocalDate.of(2020, 12, 8), 1000, false),
                        MockLocation.mockInventory(TestHelper.mockObjectId(), "BN-00002", "BOX",
                                LocalDate.of(2020, 12, 8), 1000, false)
                )
        );
        mockLocation.setInventory(inventories);
        mockDrugItem = MockDrugItem.mockDrugItem(Arrays.asList(TestHelper.mockObjectId(), TestHelper.mockObjectId()));
        mockDrugItem.setId(itemRefId);

        when(itemRepository.findById(anyString())).thenAnswer(
                invocationOnMock -> {
                    String id = invocationOnMock.getArgument(0);
                    DrugItem drugItem = MockDrugItem.mockDrugItem(Arrays.asList(TestHelper.mockObjectId()));
                    return Optional.of(drugItem);
                }
        );
        when(itemRepository.findAllByDrugBrandNameRegex(anyString(), any(Sort.class))).thenReturn(Arrays.asList(mockDrugItem));
        when(itemRepository.searchItem(anyString(), anyString(), anyString(), any(Sort.class))).thenReturn(Arrays.asList(mockDrugItem));
        when(itemRepository.searchItem(anyString(), anyString(), anyString(), anyList(), any(Sort.class))).thenReturn(Arrays.asList(mockDrugItem));
        when(itemRepository.findByItemFilterClinicIdsAndCode(anyString(), anyString())).thenAnswer(
                invocationOnMock -> {
                    String clinicId = invocationOnMock.getArgument(0);
                    String batchNo = invocationOnMock.getArgument(1);
                    mockDrugItem.setId(itemRefId);
                    return Optional.of(mockDrugItem);
                }
        );
        when(locationRepository.findByClinicId(anyString())).thenReturn(Optional.of(mockLocation));
        when(stockTakeRepository.save(any(StockTake.class))).thenAnswer(
                invocationOnMock -> {
                    StockTake stockTake = invocationOnMock.getArgument(0);
                    return stockTake;
                }
        );
        when(stockTakeRepository.findAllByClinicId(anyString(), any(Pageable.class))).thenReturn(Arrays.asList(MockStockTake.mockStockTake()));
        when(stockTakeRepository.findByStockTakeName(anyString())).thenAnswer(
                invocationOnMock -> {
                    String stockTakeName = invocationOnMock.getArgument(0);
                    StockTake stockTake = MockStockTake.mockStockTake();
                    stockTake.setStockTakeName(stockTakeName);
                    return Optional.of(stockTake);
                }
        );
        when(stockTakeRepository.findAllByStockTakeStatusOrApproveStatus(anyString(), anyString(), any(Pageable.class))).thenAnswer(
                invocationOnMock -> {
                    String stockTakeStatus = invocationOnMock.getArgument(0);
                    String stockTakeApproveStatus = invocationOnMock.getArgument(1);

                    return Arrays.asList(MockStockTake.mockStockTake(StockCountType.PARTIAL, ItemRange.A_E,
                            StockTakeStatus.valueOf(stockTakeStatus), StockTakeApproveStatus.valueOf(stockTakeApproveStatus),
                            100, 99.0, 100.0));
                }
        );
        when(stockTakeRepository.findById(anyString())).thenAnswer(
                invocationOnMock -> {
                    String id = invocationOnMock.getArgument(0);
                    StockTake stockTake = MockStockTake.mockStockTake();
                    Field idField = PersistedObject.class.getDeclaredField("id");
                    idField.setAccessible(true);
                    idField.set(stockTake, id);
                    stockTake.setStockTakeStatus(StockTakeStatus.COMPLETED);
                    stockTake.setStockCountItems(
                            new ArrayList<>(Arrays.asList(
                                    MockStockTake.mockStockCountItem("huhb39934324k3", "C-00001", "item01", "DRUG", "9232030ii99966", "ND2211",
                                            "BOX", LocalDate.of(2030, 12, 31), "ND2211", "TAB",
                                            LocalDate.of(2030, 12, 31), 100, 99.0, 99.0, "mistake to count"),
                                    MockStockTake.mockStockCountItem("nkhnjk3343bj43", "C-00002", "item02", "SERVICE", "9232030i4366", "ND2211",
                                            "BOX", LocalDate.of(2030, 12, 31), "ND2211", "TAB",
                                            LocalDate.of(2030, 12, 31), 422, 390.0, 400.0, "mistake to count")
                            ))
                    );
                    return Optional.of(stockTake);
                }
        );
        when(clinicRepository.findByIdAndStatus(anyString(), any(Status.class))).thenAnswer(
                invocationOnMock -> {
                    String clinicId = invocationOnMock.getArgument(0);
                    Clinic clinic = MockClinic.mockClinic(clinicId, "test clinic");
                    return Optional.of(clinic);
                }
        );
    }

    @Test
    public void listApprovalStockTakes() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/stock/list/stock-take/{stockTakeStatus}/{stockTakeApproveStatus}/{size}",
                        StockTakeStatus.COMPLETED, StockTakeApproveStatus.NOT_APPROVED, 100)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(StockDocument.documentListStockTake())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void searchStockTakeInventory() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/stock/search/inventory/{clinicId}/{itemGroup}/{page}/{size}",
                        TestHelper.mockObjectId(), ItemGroupType.DRUG, 0, 10)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        Map<String, String> searchRegex = new HashMap<>();
        searchRegex.put("ITEM_NAME_REGEX", "DRUG");
        TestHelper.httpRequestBuilder(requestBuilder, searchRegex);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(StockDocument.documentSearchInventory())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void searchInventoryByItemCode() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/stock/search/inventory/{clinicId}/{itemCode}/{batchNo}",
                        TestHelper.mockObjectId(), "C-00001", "BN-00001")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(StockDocument.documentSearchInventoryByItemCode())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    @Ignore
    public void adjustmentStockTake() throws Exception {

        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/stock/adjustment/stock-take/{clinicId}",
                        TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        AdjustmentStockTake adjustmentStockTake = new AdjustmentStockTake("I-0001", "BN-00001",
                "BOX", LocalDate.of(2019, 10, 28), 1000, "consumed", "remark");

        TestHelper.httpRequestBuilder(requestBuilder, adjustmentStockTake);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(StockDocument.documentAdjustmentStockTake())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void listStockTakeByClinicId() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/stock/list/stock-take/{clinicId}/{size}",
                        TestHelper.mockObjectId(), 10)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(StockDocument.documentListStockTakeByClinicId())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void startCountStockTake() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/stock/start/stock-take/{clinicId}",
                        TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        StockTake stockTake = new StockTake(LocalDate.now(), LocalTime.now(), StockCountType.PARTIAL, ItemRange.A_E.name(),
                "Admin", StockTakeStatus.IN_PROCESS_FIRST_STOCK_TAKE);
        TestHelper.httpRequestBuilder(requestBuilder, stockTake);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(StockDocument.documentStartCountStockTake())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();

    }

    @Test
    public void stopCountStockTake() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/stock/stop/stock-take/{stockTakeId}",
                        TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        List<StockCountItem> stockCountItems = new ArrayList<>(
                Arrays.asList(
                        MockStockTake.mockStockCountItem(TestHelper.mockObjectId(), "I-0001", "item 01",
                                "SERVICE", TestHelper.mockObjectId(), "BN-0002", "BOX",
                                LocalDate.now(), "BN-0003", "BOX", LocalDate.now(), 1000,
                                987.0, 980.0, "Used"),
                        MockStockTake.mockStockCountItem(TestHelper.mockObjectId(), "I-0002", "item 02",
                                "DRUG", TestHelper.mockObjectId(), "BN-0003", "BOX",
                                LocalDate.now(), "BN-0004", "BOX", LocalDate.now(), 1000,
                                987.0, 980.0, "Used")
                ));
        TestHelper.httpRequestBuilder(requestBuilder, stockCountItems);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(StockDocument.documentStopCountStockTake())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void submitStockTakeForReview() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/stock/submit/stock-take/{stockTakeId}",
                        TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(StockDocument.documentSubmitStockTake())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void approveStockTake() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/stock/approve/stock-take/{stockTakeId}",
                        TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(StockDocument.documentApproveStockTake())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void rejectStockTake() throws Exception {
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/stock/reject/stock-take/{stockTakeId}",
                        TestHelper.mockObjectId(), 10)
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(StockDocument.documentRejectStockTake())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }


}
