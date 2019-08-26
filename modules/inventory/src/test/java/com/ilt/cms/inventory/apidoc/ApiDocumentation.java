package com.ilt.cms.inventory.apidoc;

import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

public class ApiDocumentation {
    protected static RequestHeadersSnippet requestHeaderForTheRequest() {
        return requestHeaders(
                headerWithName("Authorization").description("Bearer Key from AA module for the given user"),
                headerWithName("Content-Type").description("Content Type should be [application/json]")
        );
    }

    protected static ResponseFieldsSnippet responseAPI(boolean withPayload) {
        if (withPayload) {
            return responseFields(subsectionWithPath("statusCode").description("Transaction status code"),
                    fieldWithPath("timestamp").description("Timestamp of the transaction"),
                    fieldWithPath("message").description("Description of the status code"),
                    fieldWithPath("debugMessage").optional().type("String").description("Debug message for failure cases"),
                    subsectionWithPath("payload").description("Payload detail.")

            );
        } else {
            return responseFields(fieldWithPath("statusCode").description("Transaction status code"),
                    fieldWithPath("timestamp").description("Timestamp of the transaction"),
                    fieldWithPath("message").optional().description("Description of the status code"),
                    fieldWithPath("debugMessage").optional().type("String").description("Debug message for failure cases"),
                    subsectionWithPath("payload").optional().type(JsonFieldType.OBJECT).description("Payload detail.")
            );

        }
    }

    protected static List<FieldDescriptor> UomParameters(String... variableNames){
        String prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
        return new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix).optional().type(JsonFieldType.OBJECT).description("Unit of Measure"),
                        fieldWithPath(prefix+".code").type(JsonFieldType.STRING).description("Code of the unit of measure"),
                        fieldWithPath(prefix+".displayName").optional().type(JsonFieldType.STRING).description("Display name of the unit of measure")
                )
        );
    }

    protected static List<FieldDescriptor> unitPriceParameters(String... variableNames){
        String prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
        return new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix).optional().type(JsonFieldType.OBJECT).description("Unit price amount for the item"),
                        fieldWithPath(prefix+".price").optional().type(JsonFieldType.NUMBER).description("price for the item"),
                        fieldWithPath(prefix+".taxIncluded").optional().type(JsonFieldType.BOOLEAN).description("Boolean for tax included in the price")
                )
        );
    }

    protected static List<FieldDescriptor> costParameters(String... variableNames){
        String prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
        return new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix).optional().type(JsonFieldType.OBJECT).description("cost amount for the item"),
                        fieldWithPath(prefix+".price").optional().type(JsonFieldType.NUMBER).description("price for the item"),
                        fieldWithPath(prefix+".taxIncluded").optional().type(JsonFieldType.BOOLEAN).description("Boolean for tax included in the price")
                )
        );
    }

    protected static List<FieldDescriptor> sellingPriceParameters(String... variableNames){
        String prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
        return new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix).optional().type(JsonFieldType.OBJECT).description("Selling price amount for the item"),
                        fieldWithPath(prefix+".price").optional().type(JsonFieldType.NUMBER).description("price for the item"),
                        fieldWithPath(prefix+".taxIncluded").optional().type(JsonFieldType.BOOLEAN).description("Boolean for tax included in the price")
                )
        );
    }
}
