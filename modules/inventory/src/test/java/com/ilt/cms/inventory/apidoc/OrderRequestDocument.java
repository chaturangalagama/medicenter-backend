package com.ilt.cms.inventory.apidoc;

import com.ilt.cms.inventory.model.purchase.api.OrderRequest;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;
import com.lippo.cms.util.CMSConstant;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class OrderRequestDocument extends ApiDocumentation{

    public static ResultHandler documentListOrderRequest() {
        return document("list-order-request",
                requestHeaderForTheRequest(),
                pathParameters(
                        parameterWithName("clinicId").description("Id of the clinic")
                ), requestFields(filterParameters()),
                responseAPI(true));
    }

    private static List<FieldDescriptor> filterParameters(){

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(

                Arrays.asList(
                        fieldWithPath(OrderRequest.Filter.ORDER_REQUEST_NO.name()).optional().type(JsonFieldType.STRING).description("search by order number/request number" ),
                        fieldWithPath(OrderRequest.Filter.STATUS.name()).optional().type(JsonFieldType.STRING).description("search by status"),
                        fieldWithPath(OrderRequest.Filter.TRANSFER_PURCHASE_TYPE.name()).optional().type(JsonFieldType.STRING).description("search by [TRANSFER, PURCHASE]"),
                        fieldWithPath(OrderRequest.Filter.RECEIVED_DATE_MORE.name()).optional().type(JsonFieldType.STRING).description("search by date after request date with format:" + CMSConstant.JSON_DATE_FORMAT),
                        fieldWithPath(OrderRequest.Filter.RECEIVED_DATE_LESS.name()).optional().type(JsonFieldType.STRING).description("search by date before requst date with format:" + CMSConstant.JSON_DATE_FORMAT),
                        fieldWithPath(OrderRequest.Filter.LIST_SIZE.name()).optional().type(JsonFieldType.STRING).description("Return size of the list"),
                        fieldWithPath("new").optional().type(JsonFieldType.BOOLEAN).description("???")
                )
        );

        return fieldDescriptors;
    }
}
