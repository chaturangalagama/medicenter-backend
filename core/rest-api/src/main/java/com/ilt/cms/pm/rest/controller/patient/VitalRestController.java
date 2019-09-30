package com.ilt.cms.pm.rest.controller.patient;

import com.ilt.cms.api.entity.vital.VitalEntity;
import com.ilt.cms.downstream.patient.VitalApiService;
import com.lippo.cms.exception.CMSException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static com.lippo.cms.util.CMSConstant.JSON_DATE_FORMAT_WITH_HOUR;

@RestController
@RequestMapping("/vital")
//@RolesAllowed("ROLE_VIEW_VITALS")
public class VitalRestController {

    private static final Logger logger = LogManager.getLogger(VitalRestController.class);

    private VitalApiService vitalApiService;

    public VitalRestController(VitalApiService vitalApiService) {
        this.vitalApiService = vitalApiService;
    }

    @RequestMapping("/list/{patientId}/{after}/{before}")
    public ResponseEntity list(Principal principal, @PathVariable("patientId") String patientId,
                               @PathVariable("before") @DateTimeFormat(pattern = JSON_DATE_FORMAT_WITH_HOUR) LocalDateTime before,
                               @PathVariable("after") @DateTimeFormat(pattern = JSON_DATE_FORMAT_WITH_HOUR) LocalDateTime after) {

        logger.info("displaying patient vitals for user [" + patientId + "] by [" + principal.getName() + "] before[" + before + "] after[" + after + "]");
        ResponseEntity responseEntity = vitalApiService.listAll(patientId, before, after);
        return responseEntity;
    }

    @RequestMapping("/update/{vitalId}")
    @RolesAllowed("ROLE_UPDATE_VITALS")
    public ResponseEntity update(Principal principal, @RequestBody VitalEntity vital,
                                 @PathVariable("vitalId") String vitalId) throws CMSException {

        logger.info("updating vital [" + vitalId + "] by [" + principal.getName() + "]");
        return vitalApiService.modify(vitalId, vital);
    }

    @RequestMapping("/update/vital/list")
    @RolesAllowed("ROLE_UPDATE_VITALS")
    public ResponseEntity updateList(Principal principal, @RequestBody List<VitalEntity> vitals) throws CMSException {

        logger.info("updating vital [" + vitals + "] by [" + principal.getName() + "]");
        return vitalApiService.modify(vitals);
    }

    @RequestMapping("/add")
    @RolesAllowed("ROLE_UPDATE_VITALS")
    public ResponseEntity add(Principal principal, @RequestBody VitalEntity vitalEntity) throws CMSException {

        logger.info("adding new vital to [" + vitalEntity + "] by [" + principal.getName() + "]");
        return vitalApiService.addNew(vitalEntity);
    }

    @RequestMapping("/add/vital/list")
    @RolesAllowed("ROLE_UPDATE_VITALS")
    public ResponseEntity addList(Principal principal, @RequestBody List<VitalEntity> vitalEntities) throws CMSException {

        logger.info("adding new vital to [" + vitalEntities + "] by [" + principal.getName() + "]");
        return vitalApiService.addNew(vitalEntities);
    }

    @RequestMapping("/remove/{vitalId}")
    @RolesAllowed("ROLE_UPDATE_VITALS")
    public ResponseEntity remove(Principal principal, @PathVariable("vitalId") String vitalId) throws CMSException {

        logger.info("removing vital [" + vitalId + "] by [" + principal.getName() + "]");
        return vitalApiService.remove(vitalId);

    }

}
