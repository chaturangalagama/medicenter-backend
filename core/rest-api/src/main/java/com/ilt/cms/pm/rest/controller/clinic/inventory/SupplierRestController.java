package com.ilt.cms.pm.rest.controller.clinic.inventory;

import com.ilt.cms.downstream.clinic.inventory.SupplierApiService;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/supplier")
//@RolesAllowed("ROLE_VIEW_SUPPLIER")
public class SupplierRestController {

    private static final Logger logger = LoggerFactory.getLogger(SupplierRestController.class);
    private SupplierApiService supplierApiService;

    public SupplierRestController(SupplierApiService supplierApiService) {
        this.supplierApiService = supplierApiService;
    }

    @PostMapping("/list/all")
    public HttpEntity<ApiResponse> listAll() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing all suppliers for user [" + name + "]");

        ResponseEntity<ApiResponse> serviceResponse = supplierApiService.listAll();
        return serviceResponse;
    }
}
