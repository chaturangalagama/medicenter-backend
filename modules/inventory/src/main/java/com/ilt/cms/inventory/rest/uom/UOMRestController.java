package com.ilt.cms.inventory.rest.uom;

import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.service.common.UOMService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@RestController
@RequestMapping("/uom")
@RolesAllowed("ROLE_INVENTORY")
public class UOMRestController {
    private static final Logger logger = LoggerFactory.getLogger(UOMRestController.class);

    private UOMService uomService;

    public UOMRestController(UOMService uomService) {
        this.uomService = uomService;
    }

    @PostMapping("/search/uom/{uomCode}")
    HttpEntity<ApiResponse> findUomByCode(@PathVariable("uomCode") String uomCode){

        Optional<UOM> uomOpt = uomService.findUomByCode(uomCode);
        if(uomOpt.isPresent()){
            return httpApiResponse(new HttpApiResponse(uomOpt.get()));
        }
        return httpApiResponse(new HttpApiResponse(StatusCode.E2000, "UOM not found"));
    }

    @PostMapping("/create/uom")
    HttpEntity<ApiResponse> createUom(@RequestBody UOM uom){

        UOM newUom = uomService.save(uom);

        return httpApiResponse(new HttpApiResponse(newUom));
    }

    @PostMapping("/list/all")
    HttpEntity<ApiResponse> listUom(){
        List<UOM> uoms = uomService.listUOM();
        return httpApiResponse(new HttpApiResponse(uoms));
    }
}
