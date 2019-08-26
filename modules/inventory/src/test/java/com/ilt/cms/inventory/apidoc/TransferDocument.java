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

public class TransferDocument extends ApiDocumentation{
    public static ResultHandler documentListPurchaseRequest() {
        return document("list-transfer-request",
                pathParameters(), requestFields(),
                responseAPI(true));
    }

    public static ResultHandler documentListPurchaseOrder() {
        return document("list-transfer-order",
                pathParameters(), requestFields(),
                responseAPI(true));
    }

    public static ResultHandler documentCreateRequest() {
        return document("create-transfer-request",
                pathParameters(), requestFields(transferRequestParameters()),
                responseAPI(true));
    }

    public static ResultHandler documentUpdateRequest() {
        return document("update-transfer-request",
                pathParameters(
                        parameterWithName("requestId").description("Id of the transfer request that want to modify")
                ), requestFields(transferRequestParameters()),
                responseAPI(true));
    }

    public static ResultHandler documentCreateOrder() {
        return document("create-transfer-order",
                pathParameters(), requestFields(transferOrderParameters()),
                responseAPI(true));
    }

    public static ResultHandler documentUpdateOrder() {
        return document("update-transfer-order",
                pathParameters(
                        parameterWithName("orderId").description("Id of the transfer order that want to modify")
                ), requestFields(transferOrderParameters()),
                responseAPI(true));
    }

    public static ResultHandler documentCreateOrderByRequestId() {
        return document("create-transfer-order-by-request-id",
                pathParameters(
                        parameterWithName("requestId").description("Id of the transfer request that want to create order")
                ), requestFields(),
                responseAPI(true));
    }

    public static ResultHandler documentApproveRequest() {
        return document("approve-transfer-request",
                pathParameters(
                        parameterWithName("requestId").description("Id of the transfer request that want to approve")
                ), requestFields(),
                responseAPI(false));
    }

    public static ResultHandler documentRejectRequest() {
        return document("reject-transfer-request",
                pathParameters(
                        parameterWithName("requestId").description("Id of the transfer request that want to reject")
                ), requestFields(),
                responseAPI(false));
    }

    public static ResultHandler documentAddGRN() {
        return document("add-transfer-grn",
                pathParameters(
                        parameterWithName("orderId").description("Id of the transfer order that want to add GRN")
                ), requestFields(
                        goodReceiveNoteParameters()
                ),
                responseAPI(true));
    }

    public static ResultHandler documentAddGRVN() {
        return document("add-transfer-grvn",
                pathParameters(
                        parameterWithName("orderId").description("Id of the transfer order that want to add GRVN")
                ), requestFields(
                        goodReceiveVoidNoteParameters()
                ),
                responseAPI(true));
    }

    public static ResultHandler documentAddDN() {
        return document("add-transfer-dn",
                pathParameters(
                        parameterWithName("orderId").description("Id of the transfer order that want to add delivery note")
                ), requestFields(
                        deliveryNoteParameters()
                ),
                responseAPI(true));
    }

    public static ResultHandler documentAddDVN() {
        return document("add-transfer-dvn",
                pathParameters(
                        parameterWithName("orderId").description("Id of the transfer order that want to add delivery void note")
                ), requestFields(
                        deliveryVoidNoteParameters()
                ),
                responseAPI(true));
    }

    private static List<FieldDescriptor> transferRequestParameters(){

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("id").ignored().optional().type(JsonFieldType.STRING).description("Id of the request" ),
                        fieldWithPath("senderClinicId").description("clinic id that requested item transfer from" ),
                        fieldWithPath("requestClinicId").description("clinic id that create this request" ),
                        fieldWithPath("requestNo").optional().type(JsonFieldType.STRING).description("System generate number for request"),
                        fieldWithPath("requestTime").optional().type(JsonFieldType.STRING).description("Start time to request"),
                        fieldWithPath("requestStatus").description("Request status: "+Arrays.toString(RequestStatus.values())),
                        fieldWithPath("transferNote").optional().type(JsonFieldType.STRING).description("Notes for this transfer"),
                        fieldWithPath("new").ignored().optional().type(JsonFieldType.BOOLEAN).description("???")
                )
        );
        fieldDescriptors.addAll(requestItemParameters("requestItems[]"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> requestItemParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix+"itemRefId").description("Id of the item" ),
                        fieldWithPath(prefix+"uom").type(JsonFieldType.STRING).description("Code of the unit of measure"),
                        fieldWithPath(prefix+"quantity").description("Quantity of the item requested"))
        );
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> transferOrderParameters(){

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("id").ignored().optional().type(JsonFieldType.STRING).description("Id of the order" ),
                        fieldWithPath("senderClinicId").description("Id of the sender clinic"),
                        fieldWithPath("requestId").description("Id of the purchase request"),
                        fieldWithPath("requestClinicId").description("Id of the request clinic"),
                        fieldWithPath("orderNo").description("System generate number for the order"),
                        fieldWithPath("orderTime").optional().type(JsonFieldType.STRING).description("Order time of the order with format:["+ CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS +"]"),
                        fieldWithPath("completeTime").optional().type(JsonFieldType.STRING).description("Order complete time of the order with format:["+ CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS +"]"),
                        fieldWithPath("orderStatus").description("Order status:" + Arrays.toString(OrderStatus.values())),
                        fieldWithPath("new").ignored().optional().type(JsonFieldType.BOOLEAN).description("???")

                )
        );
        fieldDescriptors.addAll(transferRequestItemParameters("transferRequestItems[]"));
        fieldDescriptors.addAll(deliveryNoteParameters("deliveryNotes[]"));
        fieldDescriptors.addAll(goodReceiveNoteParameters("goodReceiveNotes[]"));
        fieldDescriptors.addAll(goodReceiveVoidNoteParameters("goodReceiveVoidNotes[]"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> transferRequestItemParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix).description("Request item detail"),
                        fieldWithPath(prefix+"itemRefId").description("Id of the item"),
                        fieldWithPath(prefix+"uom").type(JsonFieldType.STRING).description("Code of the unit of measure"),
                        fieldWithPath(prefix+"quantity").description("Quantity of item ")

                )
        );
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
                        fieldWithPath(prefix+"additionalRemark").optional().type(JsonFieldType.STRING).description("Id of clinic inventory")
                )
        );

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
                        fieldWithPath(prefix+"batchNumber").description("Id of clinic inventory"),
                        fieldWithPath(prefix+"expiryDate").optional().type(JsonFieldType.STRING).description("Id of clinic inventory"),
                        fieldWithPath(prefix+"comment").optional().type(JsonFieldType.STRING).description("Comment of the item"),
                        fieldWithPath(prefix+"countInStock").description("System flag for identify count")
                )
        );
        //fieldDescriptors.addAll(UomParameters(prefix,"uom"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> deliveryNoteParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix+"requestClinicId").description("Id of the request clinic"),
                        fieldWithPath(prefix+"senderClinicId").description("Id of the sender clinic" ),
                        fieldWithPath(prefix+"createDate").description("Create date of delivery note"),
                        fieldWithPath(prefix+"deliveryDate").description("Delivery date of the item"),
                        fieldWithPath(prefix+"deliveryNoteNo").description("Delivery note number")
                )
        );
        fieldDescriptors.addAll(transferSendItemParameters(prefix,"transferSendItems[]"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> transferSendItemParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix+"id").ignored().optional().type(JsonFieldType.STRING).description("Id of the transferSendItem"),
                        fieldWithPath(prefix+"itemRefId").description("Id of the item" ),
                        fieldWithPath(prefix+"uom").type(JsonFieldType.STRING).description("Code of the unit of measure"),
                        fieldWithPath(prefix+"quantity").description("Quantity of the item will be sent "),
                        fieldWithPath(prefix+"batchNumber").description("Batch number"),
                        fieldWithPath(prefix+"expiryDate").optional().type(JsonFieldType.STRING).description("Expiry date of the item in format:" + CMSConstant.JSON_DATE_FORMAT),
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
                        //fieldWithPath(prefix).optional().type(JsonFieldType.ARRAY).description("Good Receive Note"),
                        fieldWithPath(prefix+"createDate").description("Date of create good receive note"),
                        fieldWithPath(prefix+"grnVoidDate").description("Date of received items"),
                        fieldWithPath(prefix+"delivererId").description("Id of clinic that send items from" ),
                        fieldWithPath(prefix+"grnVoidNo").description("Number of the GRVN "),
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
                        fieldWithPath(prefix+"id").ignored().optional().type(JsonFieldType.STRING).description("Id of the goodReceivedItem"),
                        fieldWithPath(prefix+"itemRefId").description("Id of the item" ),
                        fieldWithPath(prefix+"uom").type(JsonFieldType.STRING).description("Code of the unit of measure"),
                        fieldWithPath(prefix+"quantity").description("Quantity of the item return "),
                        fieldWithPath(prefix+"batchNumber").description("Batch number of item"),
                        fieldWithPath(prefix+"expiryDate").optional().type(JsonFieldType.STRING).description("Expiry date of the item"),
                        fieldWithPath(prefix+"comment").optional().type(JsonFieldType.STRING).description("Comment of the item"),
                        fieldWithPath(prefix+"countInStock").description("System flag for identify count")
                )
        );
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> deliveryVoidNoteParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix+"requestClinicId").description("Id of the request clinic"),
                        fieldWithPath(prefix+"senderClinicId").description("Id of the sender clinic" ),
                        fieldWithPath(prefix+"createDate").description("Create date of delivery void note"),
                        fieldWithPath(prefix+"receivedDate").description("Delivery date of the item"),
                        fieldWithPath(prefix+"dvnNo").description("Delivery void note number")
                )
        );
        fieldDescriptors.addAll(transferSendVoidItemParameters(prefix,"transferSendVoidItems[]"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> transferSendVoidItemParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix+"id").ignored().optional().type(JsonFieldType.STRING).description("Id of the transferSendItem"),
                        fieldWithPath(prefix+"itemRefId").description("Id of the item" ),
                        fieldWithPath(prefix+"uom").type(JsonFieldType.STRING).description("Code of the unit of measure"),
                        fieldWithPath(prefix+"quantity").description("Quantity of the item will be sent "),
                        fieldWithPath(prefix+"batchNumber").description("Batch number"),
                        fieldWithPath(prefix+"expiryDate").optional().type(JsonFieldType.STRING).description("Expiry date of the item in format:" + CMSConstant.JSON_DATE_FORMAT),
                        fieldWithPath(prefix+"countInStock").description("System flag for identify count")
                )
        );
        return fieldDescriptors;
    }

}
