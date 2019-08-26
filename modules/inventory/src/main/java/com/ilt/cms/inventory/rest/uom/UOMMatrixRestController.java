package com.ilt.cms.inventory.rest.uom;

import com.ilt.cms.inventory.model.common.UomMatrix;
import com.ilt.cms.inventory.rest.purchase.OrderRequestRestController;
import com.ilt.cms.inventory.service.common.UOMMatrixService;
import com.lippo.cms.exception.CMSException;
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
@RequestMapping("/uom-matrix")
@RolesAllowed("ROLE_UOM_MATRIX")
public class UOMMatrixRestController {
    private static final Logger logger = LoggerFactory.getLogger(UOMMatrixRestController.class);

    private UOMMatrixService uomMatrixService;

    public UOMMatrixRestController(UOMMatrixService uomMatrixService) {
        this.uomMatrixService = uomMatrixService;

    }

    @PostMapping("/search/ratio/{sourceUom}/{destinationUom}")
    HttpEntity<ApiResponse> findRatio(@PathVariable("sourceUom")String sourceUom,
                                      @PathVariable("destinationUom") String destinationUom,
                                      Principal principal){
        try {
            double ratio = uomMatrixService.findRatio(sourceUom, destinationUom);
            return httpApiResponse(new HttpApiResponse(ratio));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @PostMapping("/create/uom-matrix")
    HttpEntity<ApiResponse> createUOMMatrix(@RequestBody UomMatrix uomMatrix, Principal principal){
        try {
            UomMatrix newUOMatrix = uomMatrixService.saveUOMMatrix(uomMatrix);
            return httpApiResponse(new HttpApiResponse(newUOMatrix));
        } catch (CMSException e) {
            logger.error(e.getMessage(), e);
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

}
