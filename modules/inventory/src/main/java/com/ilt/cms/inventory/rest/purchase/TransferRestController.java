package com.ilt.cms.inventory.rest.purchase;

import com.ilt.cms.inventory.model.purchase.*;
import com.ilt.cms.inventory.model.purchase.enums.RequestStatus;
import com.ilt.cms.inventory.service.purchase.TransferService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@RestController
@RequestMapping("/transfer")
@RolesAllowed("ROLE_TRANSFER")
public class TransferRestController {
    private static final Logger logger = LoggerFactory.getLogger(TransferRestController.class);

    private TransferService transferService;

    public TransferRestController(TransferService transferService){
        this.transferService = transferService;
    }

    @PostMapping("/list/request/{clinicId}")
    HttpEntity<ApiResponse> listRequest(@PathVariable("clinicId") String clinicId, Principal principal){
        logger.info("Find request from the system by ["+principal.getName()+"]");
        return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
    }

    @PostMapping("/create/request")
    HttpEntity<ApiResponse> createRequest(@RequestBody TransferRequest request, Principal principal){
        logger.info("Create request from the system by [" + principal.getName() + "]");
        try {
            TransferRequest transferRequest = transferService.createTransferRequest(request);
            return httpApiResponse(new HttpApiResponse(transferRequest));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/update/request/{requestId}")
    HttpEntity<ApiResponse> modifyRequest(@PathVariable("requestId") String requestId,
                                          @RequestBody TransferRequest transferRequest, Principal principal){
        logger.info("Modify request from the system by [" + principal.getName() + "]");
        try {
            TransferRequest modifiedRequest = transferService.modifyTransferRequest(requestId, transferRequest);
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
            boolean isSuccess = transferService.approveTransferRequest(requestId);
            return httpApiResponse(new HttpApiResponse(StatusCode.S0000));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/reject/request/{requestId}")
    HttpEntity<ApiResponse> rejectTransferRequest(@PathVariable("requestId") String requestId, Principal principal){
        logger.info("Reject transfer request["+requestId+"] from the system by [" + principal.getName() + "]");
        try {
            boolean isSuccess = transferService.rejectTransferRequest(requestId);
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
            GoodReceiveNote savedReceiveNote = transferService.addGoodReceiveNote(orderId, goodReceiveNote);
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
            GoodReceiveVoidNote savedReceiveVoidNote = transferService.addGoodReceiveVoidNote(orderId, goodReceiveVoidNote);
            return httpApiResponse(new HttpApiResponse(savedReceiveVoidNote));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/add/dn/{orderId}")
    HttpEntity<ApiResponse> addDeliveryNote(@PathVariable("orderId") String orderId,
                                            @RequestBody DeliveryNote deliveryNote){
        try {
            DeliveryNote savedDeliveryNote = transferService.addDeliveryNote(orderId, deliveryNote);
            return httpApiResponse(new HttpApiResponse(savedDeliveryNote));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/add/dvn/{orderId}")
    HttpEntity<ApiResponse> addDeliveryVoidNote(@PathVariable("orderId") String orderId,
                                            @RequestBody DeliveryVoidNote deliveryVoidNote){
        try {
            DeliveryVoidNote savedDeliveryVoidNote = transferService.addDeliveryVoidNote(orderId, deliveryVoidNote);
            return httpApiResponse(new HttpApiResponse(savedDeliveryVoidNote));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }
}
