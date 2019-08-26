package com.ilt.cms.inventory.rest.inventory;


import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.inventory.model.purchase.PurchaseRequest;
import com.ilt.cms.inventory.service.inventory.DrugItemService;
import com.ilt.cms.inventory.service.inventory.LocationService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@RestController
@RequestMapping("/inventory")
//@RolesAllowed("ROLE_INVENTORY")
public class InventoryRestController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryRestController.class);

    private DrugItemService drugItemService;

    private LocationService locationService;

    public InventoryRestController(DrugItemService drugItemService, LocationService locationService){
        this.drugItemService = drugItemService;
        this.locationService = locationService;
    }

    @PostMapping("/create/drug-item")
    HttpEntity<ApiResponse> createDrugItem(@RequestBody DrugItem drugItem, Principal principal){
        logger.info("Create drug item from the system by [" + principal.getName() + "]");
        DrugItem newDrugItem = drugItemService.createDrugItem(drugItem);
        return httpApiResponse(new HttpApiResponse(newDrugItem));
    }

    @PostMapping("/search/drug-item/detail/{itemId}")
    HttpEntity<ApiResponse> findDrugItem(@PathVariable("itemId") String drugItemId, Principal principal){
        logger.info("Find drug item from the system by [" + principal.getName() + "]");
        DrugItem drugItem = null;
        try {
            drugItem = drugItemService.findDrugItemById(drugItemId);
            return httpApiResponse(new HttpApiResponse(drugItem));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/list/drug-item/{clinicId}")
    HttpEntity<ApiResponse> listAllDrugItem(@PathVariable("clinicId")String clinicId, Principal principal){
        logger.info("List clinic drug item from the system by ["+principal.getName()+"]");
        List<DrugItem> drugItems = drugItemService.listDrugItemByClinicId(clinicId);
        return httpApiResponse(new HttpApiResponse(drugItems));
    }

    @PostMapping("/find/drug-item/quantity/{clinicId}/{itemCode}/{uom}")
    HttpEntity<ApiResponse> findInventoryQuantityByItemCode(@PathVariable("clinicId") String clinicId,
                                                            @PathVariable("itemCode") String itemCode,
                                                            @PathVariable("uom") String uom,
                                                            Principal principal){
        logger.info("Find inventory item quantity from the system by ["+principal.getName()+"]");
        try {
            double totalQuantity = locationService.findInventoryQuantityByClinicIdAndItemCodeAndUom(clinicId, itemCode, uom);
            return httpApiResponse(new HttpApiResponse(totalQuantity));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }

    }

}
