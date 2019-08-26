package com.ilt.cms.inventory.rest.inventory;

import com.ilt.cms.inventory.model.inventory.Inventory;
import com.ilt.cms.inventory.model.inventory.StockCountItem;
import com.ilt.cms.inventory.model.inventory.StockTake;
import com.ilt.cms.inventory.model.inventory.api.AdjustmentStockTake;
import com.ilt.cms.inventory.model.inventory.enums.*;
import com.ilt.cms.inventory.model.purchase.PurchaseRequest;
import com.ilt.cms.inventory.service.inventory.LocationService;
import com.ilt.cms.inventory.service.inventory.StockTakeService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@RestController
@RequestMapping("/stock")
@RolesAllowed("ROLE_INVENTORY")
public class StockTakeRestController {
    private static final Logger logger = LoggerFactory.getLogger(StockTakeRestController.class);

    private static final String SEARCH_STOCK_ALERT = "STOCK_ALERT";

    private static final String SEARCH_SUPPLIER_NAME_REGEX = "SUPPLIER_NAME_REGEX";

    private static final String SEARCH_ITEM_CODE_REGEX = "ITEM_CODE_REGEX";

    private static final String SEARCH_ITEM_NAME_REGEX = "ITEM_NAME_REGEX";

    private StockTakeService stockTakeService;

    public StockTakeRestController(StockTakeService stockTakeService){
        this.stockTakeService = stockTakeService;
    }


    @PostMapping("/list/stock-take/{stockTakeStatus}/{stockTakeApproveStatus}/{size}")
    HttpEntity<ApiResponse> listStockTakeByStockTakeStatusAndApproveStatus(@PathVariable("stockTakeStatus") StockTakeStatus stockTakeStatus,
                                                                           @PathVariable("stockTakeApproveStatus") StockTakeApproveStatus stockTakeApproveStatus,
                                                                           @PathVariable("size") int size, Principal principal){
        logger.info("list stock take list from the system by [" + principal.getName() + "]");
        List<StockTake> stockTakes = stockTakeService.findStockTakesByStockTakeStatusAndApproveStatus(stockTakeStatus,
                stockTakeApproveStatus, size);
        return httpApiResponse(new HttpApiResponse(stockTakes));
    }


    @PostMapping("/search/inventory/{clinicId}/{itemGroup}/{page}/{size}")
    HttpEntity<ApiResponse> searchInventory(@PathVariable("clinicId") String clinicId,
                                          @PathVariable("itemGroup") ItemGroupType itemGroup,
                                          @PathVariable("page") int page,
                                          @PathVariable("size") int size,
                                          @RequestBody Map<String, String> searchRegex, Principal principal){
        logger.info("Search inventory from the system by [" + principal.getName() + "]");
        String stockAlert = searchRegex.get(SEARCH_STOCK_ALERT);
        String supplierNameRegex = searchRegex.get(SEARCH_SUPPLIER_NAME_REGEX);
        String itemCodeRegex = searchRegex.get(SEARCH_ITEM_CODE_REGEX);
        String itemNameRegex = searchRegex.get(SEARCH_ITEM_NAME_REGEX);
        Map<String, Object> inventoriesMap  = stockTakeService.searchInventory(page, size, clinicId, itemGroup, stockAlert,
                itemNameRegex, supplierNameRegex, itemCodeRegex);
        return httpApiResponse(new HttpApiResponse(inventoriesMap));

    }

    @PostMapping("/search/inventory/{clinicId}/{itemCode}/{batchNo}")
    HttpEntity<ApiResponse> searchInventoryByItemCode(@PathVariable("clinicId") String clinicId,
                                                      @PathVariable("itemCode") String itemCode,
                                                      @PathVariable("batchNo") String batchNo){
        try {
            Inventory inventory = stockTakeService.searchInventoryByItemCode(clinicId, itemCode, batchNo);
            return httpApiResponse(new HttpApiResponse(inventory));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }

    }

    @PostMapping("/adjustment/stock-take/{clinicId}")
    HttpEntity<ApiResponse> adjustStockLevel(@PathVariable("clinicId") String clinicId,
                                             @RequestBody AdjustmentStockTake adjustmentStockTake, Principal principal){
        logger.info("Adjust stock level that clinic["+clinicId+"] from the system by [" + principal.getName() + "]");

        try {
            StockTake stockTake = stockTakeService.adjustStockLevel(clinicId, principal.getName(),
                    adjustmentStockTake.getItemCode(), adjustmentStockTake.getBatchNo(), adjustmentStockTake.getUom(),
                    adjustmentStockTake.getExpiryDate(), adjustmentStockTake.getNewStockLevel(),
                    adjustmentStockTake.getPurposeOfAdjustment(), adjustmentStockTake.getRemark());
            return httpApiResponse(new HttpApiResponse(stockTake));
        } catch (CMSException e) {
            logger.error(e.getMessage(),e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/list/stock-take/{clinicId}/{size}")
    HttpEntity<ApiResponse> listStocktakeByClinicId(@PathVariable("clinicId") String clinicId,@PathVariable("size") int size, Principal principal){
        logger.info("List stock take of clinic["+clinicId+"] from the system by [" + principal.getName() + "]");
        List<StockTake> stockTakes = stockTakeService.listAllStockTake(clinicId, size, "createDate", Sort.Direction.DESC);
        return httpApiResponse(new HttpApiResponse(stockTakes));
    }

    @PostMapping("/start/stock-take/{clinicId}")
    HttpEntity<ApiResponse> startStockTake(@PathVariable("clinicId") String clinicId,
                                           @RequestBody StockTake stockTake,
                                          Principal principal){
        logger.info("Start stock take from the system by [" + principal.getName() + "]");
        try {
            StockTake newStockTake = stockTakeService.createStockTake(clinicId, stockTake.getStartDate(),
                    stockTake.getStartTime(), stockTake.getCountName(), stockTake.getCountType(), ItemRange.valueOf(stockTake.getItemRange()));
            return httpApiResponse(new HttpApiResponse(newStockTake));
        } catch (CMSException e) {
            logger.error(e.getMessage(),e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }

    }

    @PostMapping("/stop/stock-take/{stockTakeId}")
    HttpEntity<ApiResponse> stopCountStockTake(@PathVariable("stockTakeId") String stockTakeId,
                                               @RequestBody List<StockCountItem> stockCountItems,
                                               Principal principal){
        logger.info("Stop stock take from the system by [" + principal.getName() + "]");
        try {
            StockTake stockTake = stockTakeService.stopCountStockTake(stockTakeId, stockCountItems);
            return httpApiResponse(new HttpApiResponse(stockTake));
        } catch (CMSException e) {
            logger.error(e.getMessage(),e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/submit/stock-take/{stockTakeId}")
    HttpEntity<ApiResponse> submitStockTakeForReview(@PathVariable("stockTakeId") String stockTakeId,
                                               Principal principal){
        logger.info("Stop stock take from the system by [" + principal.getName() + "]");
        try {
            boolean isSuccess = stockTakeService.submitStockTakeForReview(stockTakeId);
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        } catch (CMSException e) {
            logger.error(e.getMessage(),e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/approve/stock-take/{stockTakeId}")
    HttpEntity<ApiResponse> approveStockTake(@PathVariable("stockTakeId") String stockTakeId, Principal principal){
        logger.info("Approve stock take from the system by [" + principal.getName() + "]");
        try {
            boolean isSuccess = stockTakeService.approveStockTake(stockTakeId);
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        } catch (CMSException e) {
            logger.error(e.getMessage(),e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/reject/stock-take/{stockTakeId}")
    HttpEntity<ApiResponse> rejectStockTake(@PathVariable("stockTakeId") String stockTakeId, Principal principal){
        logger.info("Reject stock take from the system by [" + principal.getName() + "]");
        try {
            stockTakeService.rejectStockTake(stockTakeId);
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        } catch (CMSException e) {
            logger.error(e.getMessage(),e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }


}
