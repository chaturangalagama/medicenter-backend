package com.ilt.cms.inventory.apidoc;

import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;
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

public class InventoryDocument extends ApiDocumentation{
    public static ResultHandler documentCreateDrugItem() {
        return document("create-drug-item",
                pathParameters(), requestFields(drugItemParameters()),
                responseAPI(true));
    }

    public static ResultHandler documentSearchDrugItem() {
        return document("search-drug-item",
                pathParameters(
                        parameterWithName("itemId").description("Id of the item")
                ), requestFields(),
                responseAPI(true));
    }

    public static ResultHandler documentListAllDrugItem() {
        return document("list-drug-item",
                pathParameters(
                        parameterWithName("clinicId").description("Id of the clinic")
                ), requestFields(),
                responseAPI(true));
    }

    public static ResultHandler documentFindDrugItemQuantity() {
        return document("find-drug-item-quantity",
                pathParameters(
                        parameterWithName("clinicId").description("Id of the clinic"),
                        parameterWithName("itemCode").description("Item code of the item"),
                        parameterWithName("uom").description("Uom of the item return")
                ), requestFields(),
                responseAPI(true));
    }

    private static List<FieldDescriptor> drugItemParameters(){

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath("id").optional().type(JsonFieldType.STRING).description("Id of the item" ),
                        fieldWithPath("name").description("name of the item" ),
                        fieldWithPath("code").description("code of the item" ),
                        fieldWithPath("itemType").description("Item type[" + Arrays.toString(Item.ItemType.values())+"]"),
                        fieldWithPath("description").description("Description of the item"),
                        fieldWithPath("baseUom").description("Base uom of the item"),
                        fieldWithPath("purchaseUom").description("Purchase uom of the item"),
                        //fieldWithPath("type").description("Type of the item"),
                        fieldWithPath("category").description("Category of the item"),
                        fieldWithPath("inventoried").description("Is inventoried"),
                        fieldWithPath("reorderPoint").description("Re-order point"),
                        fieldWithPath("reorderQty").description("Re-order quantity"),
                        fieldWithPath("minimumOrderQty").description("Minimum order quantity"),
                        fieldWithPath("maximumOrderQty").description("Maximum order quantity"),
                        fieldWithPath("safetyStockQty").description("Safety stock quantity"),
                        fieldWithPath("allowNegativeInventory").description("All negative inventory indicator"),
                        fieldWithPath("subItems").optional().type(JsonFieldType.OBJECT).description(""),
                        fieldWithPath("supplierId").description("Id of the supplier"),
                        fieldWithPath("dosageUom").description("Dosage uom"),
                        fieldWithPath("brandName").description("Brand name"),
                        fieldWithPath("brandNameChinese").optional().type(JsonFieldType.STRING).description("Chinese brand name of the item"),
                        fieldWithPath("brandNameJapanese").optional().type(JsonFieldType.STRING).description("Japanese brand name of the item"),
                        fieldWithPath("genericName").description("Generic name"),
                        fieldWithPath("genericNameChinese").optional().type(JsonFieldType.STRING).description("Chinese generic name of the item"),
                        fieldWithPath("genericNameJapanese").optional().type(JsonFieldType.STRING).description("Japanese generic name of the item"),
                        fieldWithPath("printDrugLabel").description("Can print drug label"),
                        fieldWithPath("remarks").description("Remark of the item"),
                        fieldWithPath("mimsClassification").description("Mims classification"),
                        fieldWithPath("drugType").description("Drug type"),
                        fieldWithPath("drugBrandedType").description("Drug branded type"),
                        fieldWithPath("modeOfAdministration").description("Mode of administration"),
                        fieldWithPath("dosageQty").description("Dosage quantity"),
                        fieldWithPath("frequency").description("frequency"),
                        fieldWithPath("frequencyCode").description("frequency code"),
                        fieldWithPath("precautionaryInstructions").description("Precautionary instruction"),
                        fieldWithPath("indications").description("Indications"),
                        fieldWithPath("controlledDrug").description("controlled drug indicator"),
                        fieldWithPath("dispensingMultiples").description("Dispensing multiples"),
                        fieldWithPath("dosageInstructionCode").description("Default dosage instruction code"),
                        fieldWithPath("new").optional().type(JsonFieldType.BOOLEAN).description("???")

                )
        );
        fieldDescriptors.addAll(costParameters("cost"));
        fieldDescriptors.addAll(sellingPriceParameters("sellingPrice"));
        fieldDescriptors.addAll(itemFilterParameters("itemFilter"));
        return fieldDescriptors;
    }

    private static List<FieldDescriptor> itemFilterParameters(String... variableNames){
        String prefix = "";
        if(variableNames != null && variableNames.length > 0) {
            prefix = Arrays.asList(variableNames).stream().collect(Collectors.joining("."));
            prefix +=".";
        }

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>(
                Arrays.asList(
                        fieldWithPath(prefix+"clinicIds[]").optional().type(JsonFieldType.ARRAY).description("List of clinic id that item belong to"),
                        fieldWithPath(prefix+"clinicGroupNames[]").optional().type(JsonFieldType.ARRAY).description("List of group name that item belong to")

                )
        );

        return fieldDescriptors;
    }

}
