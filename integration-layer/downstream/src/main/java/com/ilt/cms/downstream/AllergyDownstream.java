package com.ilt.cms.downstream;

import com.ilt.cms.api.entity.allergy.AllergyGroupEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;
public interface AllergyDownstream {
    ResponseEntity<ApiResponse> checkAllergies(String patientId,
                                               List<String> drugIds);


    ResponseEntity<ApiResponse>  listGroups();


    ResponseEntity<ApiResponse>  createAllergyGroup(AllergyGroupEntity allergyGroup) ;


    ResponseEntity<ApiResponse>  modifyAllergyGroup(AllergyGroupEntity allergyGroup,
                                                    String allergyGroupId);


    ResponseEntity<ApiResponse>  deleteAllergyGroup(String allergyGroupId) ;
}
