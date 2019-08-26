package com.ilt.cms.inventory.rest.purchase;

import com.ilt.cms.inventory.model.purchase.api.OrderRequest;
import com.ilt.cms.inventory.service.purchase.IntegrationService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@RestController
@RequestMapping("/order-request")
@RolesAllowed({"ROLE_VIEW_ORDER_REQUEST"})
public class OrderRequestRestController {
    private static final Logger logger = LoggerFactory.getLogger(OrderRequestRestController.class);

    private IntegrationService integrationService;

    public OrderRequestRestController(IntegrationService integrationService){
        this.integrationService = integrationService;
    }

    @PostMapping("/list/{clinicId}")
    HttpEntity<ApiResponse> listOrderRequestList(@PathVariable("clinicId") String clinicId,
                                                 @RequestBody Map<OrderRequest.Filter, String> filterMap, Principal principal){
        logger.info("List orderRequest from the system by [" + principal.getName() + "]");
        List<OrderRequest> orderRequests = integrationService.listOrderRequests(clinicId, filterMap);
        return httpApiResponse(new HttpApiResponse(orderRequests));
    }


}
