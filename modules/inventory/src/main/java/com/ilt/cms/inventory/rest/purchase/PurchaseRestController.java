package com.ilt.cms.inventory.rest.purchase;

import com.ilt.cms.inventory.model.purchase.*;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;
import com.ilt.cms.inventory.service.purchase.PurchaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRange;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@RestController
@RequestMapping("/purchase")
@RolesAllowed("ROLE_PURCHASE")
public class PurchaseRestController {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseRestController.class);

    private PurchaseService purchaseService;

    public PurchaseRestController(PurchaseService purchaseService){
        this.purchaseService = purchaseService;
    }

    @PostMapping("/list/request/{clinicId}")
    HttpEntity<ApiResponse> listRequest(@PathVariable("clinicId") String clinicId, Principal principal){
        logger.info("Find request from the system by ["+principal.getName()+"]");
        return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
    }

    @PostMapping("/create/request")
    HttpEntity<ApiResponse> createRequest(@RequestBody PurchaseRequest request, Principal principal){
        logger.info("Create request from the system by [" + principal.getName() + "]");
        try {
            PurchaseRequest purchaseRequest = purchaseService.createPurchaseRequest(request);
            return httpApiResponse(new HttpApiResponse(purchaseRequest));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }
    @PostMapping("/update/request/{requestId}")
    HttpEntity<ApiResponse> modifyRequest(@PathVariable("requestId") String requestId,
                                          @RequestBody PurchaseRequest purchaseRequest, Principal principal){
        logger.info("Modify request from the system by [" + principal.getName() + "]");
        try {
            PurchaseRequest modifiedRequest = purchaseService.modifyPurchaseRequest(requestId, purchaseRequest);
            return httpApiResponse(new HttpApiResponse(modifiedRequest));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/approve/request/{requestId}")
    HttpEntity<ApiResponse> approvePurchaseRequest(@PathVariable("requestId") String requestId, Principal principal){
        logger.info("Approve request["+requestId+"] from the system by [" + principal.getName() + "]");
        try {
            purchaseService.approvePurchaseRequest(requestId);
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/reject/request/{requestId}")
    HttpEntity<ApiResponse> rejectPurchaseRequest(@PathVariable("requestId") String requestId, Principal principal){
        logger.info("Reject request["+requestId+"] from the system by [" + principal.getName() + "]");
        try {
            purchaseService.rejectPurchaseRequest(requestId);
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/add/grn/{orderId}")
    HttpEntity<ApiResponse> addGoodRecivedNote(@PathVariable("orderId") String orderId,
                                               @RequestBody GoodReceiveNote goodReceiveNote){
        try {
            GoodReceiveNote savedReceiveNote = purchaseService.addGoodReceiveNote(orderId, goodReceiveNote);
            return httpApiResponse(new HttpApiResponse(savedReceiveNote));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }


    @PostMapping("/add/grvn/{orderId}")
    HttpEntity<ApiResponse> addGoodRecivedVoidNote(@PathVariable("orderId") String orderId,
                                                   @RequestBody GoodReceiveVoidNote goodReceiveVoidNote){
        try {
            GoodReceiveVoidNote savedReceiveVoidNote = purchaseService.addGoodReceiveVoidNote(orderId, goodReceiveVoidNote);
            return httpApiResponse(new HttpApiResponse(savedReceiveVoidNote));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }


}
