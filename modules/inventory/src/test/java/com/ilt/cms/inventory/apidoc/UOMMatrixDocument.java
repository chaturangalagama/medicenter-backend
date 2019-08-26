package com.ilt.cms.inventory.apidoc;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class UOMMatrixDocument extends ApiDocumentation{
    public static ResultHandler documentFindRatio() {
        return document("find-uom-ratio",
                pathParameters(
                        parameterWithName("sourceUom").description("uom that represent to 1 unit"),
                        parameterWithName("destinationUom").description("uom that want to change")
                ), requestFields(),
                responseAPI(true));
    }

    public static ResultHandler documentCreateUOMMatrix() {
        return document("create-uom-matrix",
                pathParameters(), requestFields(uomMatrixParameters()),
                responseAPI(true));
    }

    private static List<FieldDescriptor> uomMatrixParameters() {

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("id").optional().type(JsonFieldType.STRING).description("Id of the UomMatrix" ),
                        fieldWithPath("uomCode").description("code of the master uom" ),
                        subsectionWithPath("exchangeRatio").description("map of the ratio between master uom and others uom" ),
                        subsectionWithPath("new").ignored().optional().type(JsonFieldType.BOOLEAN).description("???")
                )
        );
        return fieldDescriptors;
    }
}
