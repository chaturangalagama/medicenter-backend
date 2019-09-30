package com.ilt.cms.pm.rest.controller.clinic.system;

import com.ilt.cms.downstream.clinic.system.TemporaryStoreApiService;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temp-store")
//@RolesAllowed("ROLE_TEMP_STORE_ACCESS")
public class TemporaryStoreRestController {
    private static final Logger logger = LoggerFactory.getLogger(TemporaryStoreRestController.class);
    private TemporaryStoreApiService storeDownstream;

    public TemporaryStoreRestController(TemporaryStoreApiService storeDownstream)
    {
        this.storeDownstream = storeDownstream;
    }

    @PostMapping("/store/{key}")
    public HttpEntity<ApiResponse> store(@PathVariable("key") String key, @RequestBody String body) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.trace("Storing new value [{}] by[{}]", key, name);

        ResponseEntity<ApiResponse> serviceResponse = storeDownstream.store(key, body);
        return serviceResponse;
    }

    @PostMapping("/retrieve/{key}")
    public HttpEntity<ApiResponse> retrieve(@PathVariable("key") String key) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Retrieve value [" + key + "] by[" + name + "]");
        ResponseEntity<ApiResponse> serviceResponse = storeDownstream.retrieve(key);
        return serviceResponse;
    }
}
