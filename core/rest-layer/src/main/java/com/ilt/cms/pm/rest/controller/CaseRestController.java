package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.api.entity.casem.CaseEntity;
import com.ilt.cms.api.entity.casem.ItemPriceRequest;
import com.ilt.cms.api.entity.casem.SalesOrderEntity;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.downstream.CaseDownstream;
import com.lippo.cms.container.CaseSearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/case")
//@RolesAllowed("ROLE_VIEW_CASE")
public class CaseRestController {

    private static final Logger logger = LoggerFactory.getLogger(CaseRestController.class);

    private CaseDownstream caseDownstream;

    public CaseRestController(CaseDownstream caseDownstream) {
        this.caseDownstream = caseDownstream;
    }

    @PostMapping("/list/all/{clinicId}")
    public ResponseEntity listAllByClinic(@PathVariable("clinicId") String clinicId,
                                          @RequestBody CaseSearchParams searchParams, Principal principal) {
        logger.info("Request to list cases of clinic id [" + clinicId + "] with [" + searchParams + "]. "+ "user: " + principal.getName());
        return caseDownstream.listAll(clinicId, searchParams);
    }

    @PostMapping("/list/all/{clinicId}/{page}/{size}")
    public ResponseEntity listAllByClinic(@PathVariable("clinicId") String clinicId,
                                          @PathVariable("page") int page,
                                          @PathVariable("size") int size,
                                          @RequestBody CaseSearchParams searchParams, Principal principal) {
        logger.info("Request to list cases of clinic id [" + clinicId + "] with [" + searchParams + "]. "+ "user: " + principal.getName());
        return caseDownstream.listAll(clinicId, page, size, searchParams);
    }

    @PostMapping("/search/{caseId}")
    public ResponseEntity findByCaseId(@PathVariable("caseId") String caseId, Principal principal) {
        logger.info("Request to get case of id [" + caseId + "]. "+ "user: " + principal.getName());
        return caseDownstream.findByCaseId(caseId);
    }

//    @RolesAllowed("ROLE_CREATE_CASE")
    @PostMapping("/create")
    public ResponseEntity saveNewCase(@RequestBody CaseEntity caseEntity, Principal principal) {
        logger.info("Request to save case [" + caseEntity + "]. "+ "user: " + principal.getName());
        return caseDownstream.createCase(caseEntity);
    }

//    @RolesAllowed("ROLE_UPDATE_CASE")
    @PostMapping("/update/{caseId}")
    public ResponseEntity updateCase(@PathVariable("caseId") String caseId, @RequestBody CaseEntity caseEntity, Principal principal) {
        logger.info("Request to update case [" + caseEntity + "]. " + "user: " + principal.getName());
        return caseDownstream.updateCase(caseId, caseEntity);
    }
    @PostMapping("/package/search/{caseId}")
    public ResponseEntity getCasePackage(@PathVariable("caseId") String caseId, Principal principal) {
        logger.info("Request to get package of case id [" + caseId + "]. "+ "user: " + principal.getName());
        return caseDownstream.getCasePackage(caseId);
    }

//    @RolesAllowed("ROLE_UPDATE_CASE")
    @PostMapping("/package/update/{caseId}/{packageItemId}")
    public ResponseEntity updateCasePackage(@PathVariable("caseId") String caseId, @PathVariable("packageItemId") String packageItemId, Principal principal) {
        logger.info("Request to update package of case id [" + caseId + "]. " + "user: " + principal.getName());
        return caseDownstream.updateCasePackage(caseId, packageItemId);
    }

    @PostMapping("/so/search/{caseId}")
    public ResponseEntity getCaseSalesOrder(@PathVariable("caseId") String caseId, Principal principal) {
        logger.info("Request to get sales order of case id [" + caseId + "]. " + "user: " + principal.getName());
        return caseDownstream.getSalesOrder(caseId);
    }

//    @RolesAllowed("ROLE_UPDATE_CASE")
    @PostMapping("/so/update/{caseId}")
    public ResponseEntity updateCaseDispatch(@PathVariable("caseId") String caseId, @RequestBody SalesOrderEntity salesOrderEntity, Principal principal) {
        logger.info("Request to update sales order of case id [" + caseId + "]. " + "user: " + principal.getName());
        return caseDownstream.updateSalesOrder(caseId, salesOrderEntity);
    }

//    @RolesAllowed("ROLE_UPDATE_CASE")
    @PostMapping("/close/{caseId}")
    public ResponseEntity closeCase(@PathVariable("caseId") String caseId, Principal principal) {
        logger.info("Request to close of case id [" + caseId + "]. " + "user: " + principal.getName());
        return caseDownstream.closeCase(caseId);
    }

//    @RolesAllowed("ROLE_UPDATE_CASE")
    @PostMapping("/{caseId}/single-visit/{on}")
    public ResponseEntity setSingleVisitCase(@PathVariable("caseId") String caseId, @PathVariable("on") String state, Principal principal) {
        logger.info("Request to update as single visit [" + caseId + "]. " + "user: " + principal.getName());
        return caseDownstream.updateCaseSingleVisit(caseId, state);
    }

    @PostMapping("/item/prices/{caseId}")
    public ResponseEntity getPriceChange(@PathVariable("caseId") String caseId,
                                         @RequestBody ItemChargeDetail.ItemChargeRequest itemChargeRequest, Principal principal) {
        logger.info("Item temporal charging calculation for case id[" + caseId + "] items[" + itemChargeRequest + "] by [" + principal.getName() + "]");
        return caseDownstream.getItemChangePriceCalculation(caseId, itemChargeRequest);
    }
}
