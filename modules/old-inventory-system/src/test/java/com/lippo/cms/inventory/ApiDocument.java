package com.lippo.cms.inventory;

import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.Arrays;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class ApiDocument {

    private static RequestHeadersSnippet requestHeaderForTheRequest() {
        return requestHeaders(
                headerWithName("Authorization").description("Bearer Key from AA module for the given user"),
                headerWithName("Content-Type").description("Content Type should be [application/json]")
        );
    }

    public static RequestFieldsSnippet inventoryFields() {
        return requestFields(
                fieldWithPath("inventoryUsages").description("Inventory that is given to the patient"));
    }

    private static ResponseFieldsSnippet responseAPI(boolean withPayload) {
        if (withPayload) {
            return responseFields(fieldWithPath("statusCode").description("Transaction status code"),
                    fieldWithPath("timestamp").description("Timestamp of the transaction"),
                    fieldWithPath("message").description("Description of the status code"),
                    fieldWithPath("debugMessage").optional().type("String").description("Debug message for failure cases"),
                    fieldWithPath("payload").optional().description("Saved patient information returned"));
        } else {
            return responseFields(fieldWithPath("statusCode").description("Transaction status code"),
                    fieldWithPath("timestamp").description("Timestamp of the transaction"),
                    fieldWithPath("message").optional().description("Description of the status code"),
                    fieldWithPath("debugMessage").optional().type("String").description("Debug message for failure cases"));
        }
    }

    public static ResultHandler getInventoryUsage() {
        return document("get-usage", requestHeaderForTheRequest(), pathParameters(
                parameterWithName("clinicId").description("Clinic which use the inventories")),
                inventoryFields(), responseAPI(true));
    }

    public static ResultHandler updateInventoryUsage() {
        return document("update-usage", requestHeaderForTheRequest(), pathParameters(
                parameterWithName("clinicId").description("Clinic which use the inventories")),
                inventoryFields(), responseAPI(true));
    }
}
