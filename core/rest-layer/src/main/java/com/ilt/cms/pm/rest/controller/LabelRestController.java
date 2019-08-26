package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.api.entity.label.LabelEntity;
import com.ilt.cms.downstream.LabelDownstream;
import com.lippo.commons.web.api.ApiResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/label")
//@RolesAllowed("ROLE_VIEW_LABEL")
public class LabelRestController {

    private static final Logger logger = LoggerFactory.getLogger(LabelRestController.class);

    private LabelDownstream defaultLabelDownstream;

    public LabelRestController(LabelDownstream defaultLabelDownstream) {
        this.defaultLabelDownstream = defaultLabelDownstream;
    }

    @PostMapping("/list/all")
    public HttpEntity<ApiResponse> listAll() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("Listing all labels for user [" + name + "]");

        HttpEntity<ApiResponse> serviceResponse = defaultLabelDownstream.listAll();
        return serviceResponse;
    }

    @PostMapping("/search/{name}")
    public HttpEntity<ApiResponse> searchByName(@PathVariable String name) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String authName = auth.getName(); //get logged in username
//        logger.info("Search for [" + name + "] by [" + authName + "]");

        HttpEntity<ApiResponse> serviceResponse = defaultLabelDownstream.findByName(name);
        return serviceResponse;
    }

    @RolesAllowed("ROLE_MODIFY_LABEL")
    @PostMapping("/add")
    public HttpEntity<ApiResponse> addLabel(@RequestBody LabelEntity label) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("adding a new Label by[" + name + "] label [" + label + "]");
        HttpEntity<ApiResponse> serviceResponse = defaultLabelDownstream.add(label);
        return serviceResponse;
    }

    @RolesAllowed("ROLE_MODIFY_LABEL")
    @PostMapping("/modify/{labelId}")
    public HttpEntity<ApiResponse> modifyLabel(@PathVariable String labelId,
                                      @RequestBody LabelEntity label) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        logger.info("modifying Label [" + labelId + "] by[" + name + "] label [" + label + "]");
        HttpEntity<ApiResponse> serviceResponse = defaultLabelDownstream.modify(labelId, label);
        return serviceResponse;
    }
}
