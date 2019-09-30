package com.ilt.cms.downstream.clinic;

import com.ilt.cms.api.entity.allergy.AllergyGroupEntity;
import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;
public interface AllergyApiService {
    ResponseEntity<ApiResponse> checkAllergies(String patientId,
                                               List<String> drugIds);


    ResponseEntity<ApiResponse>  listGroups();


    ResponseEntity<ApiResponse>  createAllergyGroup(AllergyGroupEntity allergyGroup) ;


    ResponseEntity<ApiResponse>  modifyAllergyGroup(AllergyGroupEntity allergyGroup,
                                                    String allergyGroupId);


    ResponseEntity<ApiResponse>  deleteAllergyGroup(String allergyGroupId) ;
}
