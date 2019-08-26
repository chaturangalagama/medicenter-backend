package com.ilt.cms.inventory.rest.uom;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.inventory.apidoc.InventoryDocument;
import com.ilt.cms.inventory.apidoc.UOMMatrixDocument;
import com.ilt.cms.inventory.db.repository.spring.common.UOMMatrixRepository;
import com.ilt.cms.inventory.db.repository.spring.common.UOMRepository;
import com.ilt.cms.inventory.helper.TestHelper;
import com.ilt.cms.inventory.mock.MockDrugItem;
import com.ilt.cms.inventory.mock.MockUOMMatrix;
import com.ilt.cms.inventory.model.common.UomMatrix;
import com.ilt.cms.repository.spring.ItemRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs(outputDir = "target/snippets")
@WithMockUser(username = "DR02", roles = {"UOM_MATRIX"})
public class UOMMatrixRestControllerTest {
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UOMMatrixRepository uomMatrixRepository;

    @Before
    public void setup() throws Exception {
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
    public void findRatio() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/uom-matrix/search/ratio/{sourceUom}/{destinationUom}", "BOX", "TAB")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, null);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(UOMMatrixDocument.documentFindRatio())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }

    @Test
    public void createUOMMatrix() throws Exception{
        MockHttpServletRequestBuilder requestBuilder =
                RestDocumentationRequestBuilders.post("/uom-matrix/create/uom-matrix")
                        .header("Authorization", TestHelper.TOKEN).with(csrf());

        TestHelper.httpRequestBuilder(requestBuilder, MockUOMMatrix.mockUOMMatrix());

        MvcResult result = mockMvc.perform(requestBuilder)
                .andDo(UOMMatrixDocument.documentCreateUOMMatrix())
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
    }
}
