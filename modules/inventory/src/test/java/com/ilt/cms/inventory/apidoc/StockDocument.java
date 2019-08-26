package com.ilt.cms.inventory.apidoc;

import com.ilt.cms.inventory.model.inventory.enums.*;
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

public class StockDocument extends ApiDocumentation{
    /*public static ResultHandler documentListInventory() {
        return document("list-inventory",
                pathParameters(
                        parameterWithName("page").description("page of the list"),
                        parameterWithName("size").description("size of the list"),
                        parameterWithName("clinicId").description("Id of the clinic"),
                        parameterWithName("itemGroup").description("Item group of the item")
                ), requestFields(
                        fieldWithPath("BRAND_NAME_REGEX").optional().type(JsonFieldType.STRING).description("Regex of the brand name" )
                ),
                responseAPI(true));
    }*/

    public static ResultHandler documentSearchInventory() {
        return document("search-inventory",
                pathParameters(
                        parameterWithName("page").description("page of the list"),
                        parameterWithName("size").description("size of the list"),
                        parameterWithName("clinicId").description("Id of the clinic"),
                        parameterWithName("itemGroup").description("Item group:[" + Arrays.toString(ItemGroupType.values()) + "]")
                ), requestFields(
                        fieldWithPath("STOCK_ALERT").optional().type(JsonFieldType.STRING).description("Find by stock alert[" +Arrays.toString(StockAlertType.values())+ "]" ),
                        fieldWithPath("SUPPLIER_NAME_REGEX").optional().type(JsonFieldType.STRING).description("Find by supplier name regex" ),
                        fieldWithPath("ITEM_NAME_REGEX").optional().type(JsonFieldType.STRING).description("Find by item name regex" ),
                        fieldWithPath("ITEM_CODE_REGEX").optional().type(JsonFieldType.STRING).description("Find by item code regex" )
                ),
                responseAPI(true));
    }

    public static ResultHandler documentListStockTake() {
        return document("list-stock-take",pathParameters(
                parameterWithName("stockTakeStatus").description("Status:["+Arrays.toString(StockTakeStatus.values())+"]"),
                parameterWithName("stockTakeApproveStatus").description("Status:["+Arrays.toString(StockTakeApproveStatus.values())+"]"),
                parameterWithName("size").description("size of the list")
        ), requestFields(), responseAPI(true));
    }

    public static ResultHandler documentSearchInventoryByItemCode() {
        return document("search-inventory-item-code", pathParameters(
                parameterWithName("clinicId").description("Id of the clinic"),
                parameterWithName("itemCode").description("Item code of the item"),
                parameterWithName("batchNo").description("Batch number of the inventory")
        ), requestFields(), responseAPI(true));
    }

    public static ResultHandler documentAdjustmentStockTake() {
        return document("adjustment-stock-take", pathParameters(
                parameterWithName("clinicId").description("Id of the clinic")
        ), requestFields(
                adjustmentStockTake()
        ), responseAPI(true));
    }

    public static ResultHandler documentListStockTakeByClinicId() {
        return document("list-stock-take-clinic", pathParameters(
                parameterWithName("clinicId").description("Id of the clinic"),
                parameterWithName("size").description("size of the list")
        ), requestFields(), responseAPI(true));
    }

    public static ResultHandler documentStartCountStockTake() {
        return document("start-stock-take", pathParameters(
                parameterWithName("clinicId").description("Id of the clinic")
        ), requestFields(
                stockTakeParameters()
        ), responseAPI(true));
    }

    public static ResultHandler documentStopCountStockTake() {
        return document("stop-stock-take", pathParameters(
                parameterWithName("stockTakeId").description("Id of the clinic")
        ), requestFields(
                stockCountItemParameters("[]")
        ), responseAPI(true));
    }

    public static ResultHandler documentApproveStockTake() {
        return document("approve-stock-take", pathParameters(
                parameterWithName("stockTakeId").description("Id of the stock take")
        ), requestFields(), responseAPI(false));
    }

    public static ResultHandler documentRejectStockTake()
    {
        return document("reject-stock-take", pathParameters(
                parameterWithName("stockTakeId").description("Id of the stock take")
        ), requestFields(), responseAPI(false));
    }

    public static ResultHandler documentSubmitStockTake() {
        return document("submit-stock-take", pathParameters(
                parameterWithName("stockTakeId").description("Id of the stock take")
        ), requestFields(), responseAPI(false));
    }

    private static List<FieldDescriptor> adjustmentStockTake(){
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("itemCode").description("Code of the item" ),
                        fieldWithPath("batchNo").description("Batch no of the item"),
                        fieldWithPath("uom").description("UOM of the item"),
                        fieldWithPath("expiryDate").description("Expiry date of the item"),
                        fieldWithPath("newStockLevel").description("New stock level of the item"),
                        fieldWithPath("purposeOfAdjustment").description("Purpose of the adjustment" ),
                        fieldWithPath("remark").optional().type(JsonFieldType.STRING).description("Remark of the adjustment"),
                        fieldWithPath("new").ignored().optional().type(JsonFieldType. BOOLEAN).description("???")

                )
        );
        return fieldDescriptors;
    }


    private static List<FieldDescriptor> stockTakeParameters(){

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("id").ignored().optional().type(JsonFieldType.STRING).description("Id of the stock take" ),
                        fieldWithPath("startDate").description("start date in format:" + CMSConstant.JSON_DATE_FORMAT),
                        fieldWithPath("startTime").description("start time in format:" + CMSConstant.JSON_TIME_FORMAT),
                        fieldWithPath("clinicId").ignored().optional().type(JsonFieldType.STRING).description("Id of the clinic"),
                        fieldWithPath("countType").description("Count type:" + Arrays.toString(StockCountType.values())),
                        fieldWithPath("stockTakeStatus").description("Status type:" + Arrays.toString(StockTakeStatus.values())),
                        fieldWithPath("itemRange").description("Item range for this count"),
                        fieldWithPath("countName").description("Count name"),
                        fieldWithPath("new").ignored().optional().type(JsonFieldType. BOOLEAN).description("???")

                )
        );
        //fieldDescriptors.addAll(stockCountItemParameters("stockCountItems[]"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> stockCountItemParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix+"id").description("Id of the stock count item" ),
                        fieldWithPath(prefix+"inventoryId").description("Id of the inventory" ),
                        fieldWithPath(prefix+"itemRefId").description("Id of the item" ),
                        fieldWithPath(prefix+"itemCode").description("Code of the item" ),
                        fieldWithPath(prefix+"itemName").description("Name of the item" ),
                        //fieldWithPath(prefix+"itemType").description("Type of the item" ),
                        fieldWithPath(prefix+"curBatchNumber").description("Existing batch number of the inventory" ),
                        fieldWithPath(prefix+"curUom").description("Existing uom of the item" ),
                        fieldWithPath(prefix+"curExpiryDate").description("Existing expiry date of the item" ),
                        fieldWithPath(prefix+"batchNumber").description("Batch number of the item"),
                        fieldWithPath(prefix+"baseUom").description("Base uom of the item"),
                        fieldWithPath(prefix+"expiryDate").description("Expiry date of the item"),
                        fieldWithPath(prefix+"availableCount").description("Available count of the item requested"),
                        fieldWithPath(prefix+"firstQuantity").optional().type(JsonFieldType.NUMBER).description("First count of quantity of the item" ),
                        fieldWithPath(prefix+"secondQuantity").optional().type(JsonFieldType.NUMBER).description("Second count of quantity of the item"),
                        fieldWithPath(prefix+"reason").optional().type(JsonFieldType.STRING).description("Reason of the different"))
        );
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> inventoryParameters(){

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("itemRefId").description("Id of the item" ),
                        fieldWithPath("batchNumber").description("Batch number of the item"),
                        fieldWithPath("baseUom").description("Base uom of the item"),
                        fieldWithPath("expiryDate").description("Expiry date of the item"),
                        fieldWithPath("availableCount").description("Available count of the item requested"))
        );
        //fieldDescriptors.addAll(UomParameters( "baseUom"));
        return fieldDescriptors;
    }

}
