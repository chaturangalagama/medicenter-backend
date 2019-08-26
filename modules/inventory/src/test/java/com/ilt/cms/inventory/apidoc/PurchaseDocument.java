package com.ilt.cms.inventory.apidoc;

import com.ilt.cms.inventory.model.purchase.enums.OrderStatus;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;
import com.lippo.cms.util.CMSConstant;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public class PurchaseDocument extends ApiDocumentation{
    /*public static ResultHandler documentListPurchaseRequest() {
        return document("list-purchase-request",
                pathParameters(), requestFields(),
                responseAPI(true));
    }

    public static ResultHandler documentListPurchaseOrder() {
        return document("list-purchase-order",
                pathParameters(), requestFields(),
                responseAPI(true));
    }*/

    public static ResultHandler documentCreateRequest() {
        return document("create-purchase-request",
                pathParameters(), requestFields(purchaseRequestParameters()),
                responseAPI(true));
    }

    public static ResultHandler documentUpdateRequest() {
        return document("update-purchase-request",
                pathParameters(
                        parameterWithName("requestId").description("Id of the purchase request that want to modify")
                ), requestFields(purchaseRequestParameters()),
                responseAPI(true));
    }

    public static ResultHandler documentCreateOrder() {
        return document("create-purchase-order",
                pathParameters(), requestFields(purchaseOrderParameters()),
                responseAPI(true));
    }

    public static ResultHandler documentUpdateOrder() {
        return document("update-purchase-order",
                pathParameters(
                        parameterWithName("orderId").description("Id of the purchase order that want to modify")
                ), requestFields(purchaseOrderParameters()),
                responseAPI(true));
    }

    public static ResultHandler documentCreateOrderByRequestId() {
        return document("create-purchase-order-by-request-id",
                pathParameters(
                        parameterWithName("requestId").description("Id of the purchase request that want to create order")
                ), requestFields(),
                responseAPI(true));
    }

    public static ResultHandler documentApproveRequest() {
        return document("approve-purchase-request",
                pathParameters(
                        parameterWithName("requestId").description("Id of the purchase request that want to approve")
                ), requestFields(),
                responseAPI(false));
    }

    public static ResultHandler documentRejectRequest() {
        return document("reject-purchase-request",
                pathParameters(
                        parameterWithName("requestId").description("Id of the purchase request that want to reject")
                ), requestFields(),
                responseAPI(false));
    }

    public static ResultHandler documentAddGRN() {
        return document("add-purchase-grn",
                pathParameters(
                        parameterWithName("orderId").description("Id of the purchase order that want to add GRN")
                ), requestFields(
                        goodReceiveNoteParameters()
                ),
                responseAPI(true));
    }

    public static ResultHandler documentAddGRVN() {
        return document("add-purchase-grvn",
                pathParameters(
                        parameterWithName("orderId").description("Id of the purchase order that want to add GRVN")
                ), requestFields(
                        goodReceiveVoidNoteParameters()
                ),
                responseAPI(true));
    }


    private static List<FieldDescriptor> purchaseRequestParameters(){

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("id").ignored().optional().type(JsonFieldType.STRING).description("Id of the request" ),
                        fieldWithPath("requestClinicId").description("clinic id that create this request" ),
                        fieldWithPath("requestNo").description("System generate number for request"),
                        fieldWithPath("requestTime").type(JsonFieldType.STRING).description("Start time to request"),
                        fieldWithPath("requestStatus").description("Request status: "+Arrays.toString(RequestStatus.values())),
                        fieldWithPath("supplierId").optional().type(JsonFieldType.STRING).description("Id of clinic want to transfer from"),
                        fieldWithPath("new").ignored().optional().type(JsonFieldType.BOOLEAN).description("???")
                )
        );
        fieldDescriptors.addAll(requestItemParameters("requestItems[]"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> requestItemParameters(String... variableNames){
        String prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix+".itemRefId").description("Id of the item" ),
                        fieldWithPath(prefix+".uom").type(JsonFieldType.STRING).description("Code of the unit of measure"),
                        fieldWithPath(prefix+".quantity").description("Quantity of the item requested"))
        );
        fieldDescriptors.addAll(unitPriceParameters(prefix,"unitPrice"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> purchaseOrderParameters(){
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("id").optional().type(JsonFieldType.STRING).description("Id of the order" ),
                        fieldWithPath("supplierId").description("Id of the supplier"),
                        fieldWithPath("requestId").description("Id of the purchase request"),
                        fieldWithPath("requestClinicId").description("Id of the request clinic"),
                        fieldWithPath("orderNo").description("System generate number for the order"),
                        fieldWithPath("orderTime").optional().type(JsonFieldType.STRING).description("Order time of the order with format:["+ CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS +"]"),
                        fieldWithPath("completeTime").optional().type(JsonFieldType.STRING).description("Order complete time of the order with format:["+ CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS +"]"),
                        fieldWithPath("orderStatus").description("Order status:" + Arrays.toString(OrderStatus.values())),
                        fieldWithPath("new").ignored().optional().type(JsonFieldType.BOOLEAN).description("???")

                )
        );
        fieldDescriptors.addAll(goodOrderedItemParameters("goodPurchasedItems[]"));
        fieldDescriptors.addAll(goodReceiveNoteParameters("goodReceiveNotes[]"));
        fieldDescriptors.addAll(goodReceiveVoidNoteParameters("goodReceiveVoidNotes[]"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> goodOrderedItemParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix).description("Order item detail"),
                        fieldWithPath(prefix+"itemRefId").description("Id of the order item"),
                        fieldWithPath(prefix+"uom").type(JsonFieldType.STRING).description("Code of the unit of measure"),
                        fieldWithPath(prefix+"quantity").description("Quantity of item ")

                )
        );
        //fieldDescriptors.addAll(UomParameters(prefix,"uom"));
        fieldDescriptors.addAll(unitPriceParameters(prefix,"unitPrice"));
        return fieldDescriptors;
    }


    private static List<FieldDescriptor> goodReceiveNoteParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        //fieldWithPath(prefix).optional().type(JsonFieldType.ARRAY).description("Good Receive Note"),
                        fieldWithPath(prefix+"createDate").description("Date of create good receive note"),
                        fieldWithPath(prefix+"grnDate").description("Date of received items"),
                        fieldWithPath(prefix+"delivererId").description("Id of clinic that send items from" ),
                        fieldWithPath(prefix+"grnNo").description("Number of the GRN "),
                        fieldWithPath(prefix+"additionalRemark").optional().type(JsonFieldType.STRING).description("Addition remark")
                )
        );
        if(!prefix.equals("")){
            prefix = prefix.substring(0, prefix.length() -2);
        }
        fieldDescriptors.addAll(goodReceivedItem(prefix,"receivedItems[]"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> goodReceivedItem(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix).optional().type(JsonFieldType.ARRAY).description("Good Received items"),
                        fieldWithPath(prefix+"id").ignored().optional().type(JsonFieldType.STRING).description("Id of the goodReceivedItem"),
                        fieldWithPath(prefix+"itemRefId").description("Id of the item" ),
                        fieldWithPath(prefix+"uom").type(JsonFieldType.STRING).description("Code of the unit of measure"),
                        fieldWithPath(prefix+"quantity").description("Quantity of the item received "),
                        fieldWithPath(prefix+"batchNumber").description("Batch number of the item"),
                        fieldWithPath(prefix+"expiryDate").optional().type(JsonFieldType.STRING).description("Expiry date of item"),
                        fieldWithPath(prefix+"comment").optional().type(JsonFieldType.STRING).description("Comment of the item"),
                        fieldWithPath(prefix+"countInStock").description("System flag for identify count")
                )
        );
        //fieldDescriptors.addAll(UomParameters(prefix,"uom"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> goodReceiveVoidNoteParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix+"createDate").description("Date of create good receive void note"),
                        //fieldWithPath(prefix).optional().type(JsonFieldType.ARRAY).description("Good Receive Note"),
                        fieldWithPath(prefix+"grnVoidDate").description("Date of received items"),
                        fieldWithPath(prefix+"delivererId").description("Id of clinic that send items from" ),
                        fieldWithPath(prefix+"grnVoidNo").description("Number of the GRN "),
                        fieldWithPath(prefix+"additionalRemark").optional().type(JsonFieldType.STRING).description("Id of clinic inventory")
                )
        );

        fieldDescriptors.addAll(goodReturnItem(prefix,"returnItems[]"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> goodReturnItem(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix).optional().type(JsonFieldType.ARRAY).description("Good Received items"),
                        fieldWithPath(prefix+"id").ignored().optional().type(JsonFieldType.STRING).description("Id of the goodReceivedItem"),
                        fieldWithPath(prefix+"itemRefId").description("Id of the item" ),
                        fieldWithPath(prefix+"uom").type(JsonFieldType.STRING).description("Code of the unit of measure"),
                        fieldWithPath(prefix+"quantity").description("Quantity of the item return "),
                        fieldWithPath(prefix+"batchNumber").description("Batch number of the item"),
                        fieldWithPath(prefix+"expiryDate").optional().type(JsonFieldType.STRING).description("Expiry date of the item"),
                        fieldWithPath(prefix+"comment").optional().type(JsonFieldType.STRING).description("Comment of the item"),
                        fieldWithPath(prefix+"countInStock").description("System flag for identify count")
                )
        );
        //fieldDescriptors.addAll(UomParameters(prefix,"uom"));
        return fieldDescriptors;
    }

}
