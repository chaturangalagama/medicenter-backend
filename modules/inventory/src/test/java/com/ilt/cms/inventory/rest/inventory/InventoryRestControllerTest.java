package com.ilt.cms.inventory.rest.inventory;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.inventory.apidoc.InventoryDocument;
import com.ilt.cms.inventory.db.repository.spring.common.UOMMatrixRepository;
import com.ilt.cms.inventory.db.repository.spring.common.UOMRepository;
import com.ilt.cms.inventory.db.repository.spring.inventory.LocationRepository;
import com.ilt.cms.inventory.helper.TestHelper;
import com.ilt.cms.inventory.mock.MockDrugItem;
import com.ilt.cms.inventory.mock.MockLocation;
import com.ilt.cms.inventory.mock.MockUOMMatrix;
import com.ilt.cms.inventory.model.common.UomMatrix;
import com.ilt.cms.repository.spring.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
@WithMockUser(username = "DR02", roles = {"INVENTORY"})
public class InventoryRestControllerTest {
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private UOMMatrixRepository uomMatrixRepository;


    //private UOMRepository uomRepository;

    @Before
    public void setup() throws Exception {
        when(itemRepository.findById(anyString())).thenReturn(Optional.of(MockDrugItem.mockDrugItem(Arrays.asList(TestHelper.mockObjectId(), TestHelper.mockObjectId()))));
        when(itemRepository.save(any(DrugItem.class))).thenReturn(MockDrugItem.mockDrugItem(Arrays.asList(TestHelper.mockObjectId(), TestHelper.mockObjectId())));
        when(itemRepository.findAll(any(Pageable.class))).thenReturn(
                new PageImpl<Item>(Arrays.asList(MockDrugItem.mockDrugItem(Arrays.asList(TestHelper.mockObjectId(), TestHelper.mockObjectId()))))
                );
        when(itemRepository.findByItemFilterClinicIdsAndCode(anyString(), anyString())).thenAnswer(
                invocationOnMock -> {
                    String clinicId = invocationOnMock.getArgument(0);
                    String itemCode = invocationOnMock.getArgument(1);
                    DrugItem drugItem = MockDrugItem.mockDrugItem(Arrays.asList(clinicId));
                    drugItem.setId("j23hjk32992jkjlaaa");
                    drugItem.setCode(itemCode);
                    return Optional.of(drugItem);
                }
        );
        when(locationRepository.findByClinicId(anyString())).thenReturn(Optional.of(MockLocation.mockLocation()));
        when(uomMatrixRepository.findByUomCode(anyString())).thenAnswer(
                invocationOnMock -> {
                    String uomCode = invocationOnMock.getArgument(0);
                    UomMatrix uomMatrix = MockUOMMatrix.mockUOMMatrix();
                    uomMatrix.setUomCode(uomCode);
                    return Optional.of(uomMatrix);
                }
        );
        when(uomMatrixRepository.save(any(UomMatrix.class))).thenAnswer(
                invocationOnMock -> {
                    UomMatrix uomMatrix = invocationOnMock.getArgument(0);
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(uomMatrix, "3934342knkkd2432");
                    return uomMatrix;
                }
        );
    }

    @Test
    public void createDrugItem() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/inventory/create/drug-item")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, MockDrugItem.mockDrugItem(Arrays.asList(TestHelper.mockObjectId(), TestHelper.mockObjectId())));

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(InventoryDocument.documentCreateDrugItem())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void findDrugItem() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/inventory/search/drug-item/detail/{itemId}", "994335njkj3")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(InventoryDocument.documentSearchDrugItem())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void listAllDrugItem() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/inventory/list/drug-item/{clinicId}", TestHelper.mockObjectId())
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(InventoryDocument.documentListAllDrugItem())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void findDrugItemQuantity() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/inventory/find/drug-item/quantity/{clinicId}/{itemCode}/{uom}",
                        TestHelper.mockObjectId(), "VSR-0001", "TABLET")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(InventoryDocument.documentFindDrugItemQuantity())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }
}
