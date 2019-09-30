package com.ilt.cms.pm.integration.impl.clinic;

import com.ilt.cms.api.entity.allergy.AllergyGroupEntity;
import com.ilt.cms.core.entity.allergy.AllergyGroup;
import com.ilt.cms.downstream.clinic.AllergyApiService;
import com.ilt.cms.pm.business.service.clinic.AllergyGroupService;
import com.ilt.cms.pm.integration.mapper.clinic.AllergyGroupMapper;
import com.lippo.cms.exception.AllergyGroupException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;
@Service
public class DefaultAllergyApiService implements AllergyApiService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultAllergyApiService.class);


    private AllergyGroupService allergyGroupService;

    public DefaultAllergyApiService(AllergyGroupService allergyGroupService){
        this.allergyGroupService = allergyGroupService;
    }

    @Override
    public ResponseEntity<ApiResponse> checkAllergies(String patientId, List<String> drugIds) {
        try {
            List<String> stringList = allergyGroupService.checkAllergy(patientId, drugIds);
            return httpApiResponse(new HttpApiResponse(stringList));
        } catch (AllergyGroupException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listGroups() {
        List<AllergyGroupEntity> allergyGroupEntityList = new ArrayList<AllergyGroupEntity>();

        List<AllergyGroup> allergyGroupList = allergyGroupService.listGroups();
        allergyGroupList.stream().forEach(i -> allergyGroupEntityList.add(AllergyGroupMapper.mapToEntity(i)));

        return httpApiResponse(new HttpApiResponse(allergyGroupEntityList));
    }

    @Override
    public ResponseEntity<ApiResponse> createAllergyGroup(AllergyGroupEntity allergyGroup) {
        try {
            AllergyGroup newAllergyGroup = allergyGroupService.createAllergyGroup(AllergyGroupMapper.mapToCore(allergyGroup));

            return httpApiResponse(new HttpApiResponse(AllergyGroupMapper.mapToEntity(newAllergyGroup)));
        } catch (AllergyGroupException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> modifyAllergyGroup(AllergyGroupEntity allergyGroup, String allergyGroupId) {
        try {
            AllergyGroup modifiedAllergyGroup = allergyGroupService.modifyAllergyGroup(allergyGroupId, AllergyGroupMapper.mapToCore(allergyGroup));
            return httpApiResponse(new HttpApiResponse(AllergyGroupMapper.mapToEntity(modifiedAllergyGroup)));
        } catch (AllergyGroupException e) {
            logger.error(e.getCode() + ":"+e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> deleteAllergyGroup(String allergyGroupId) {
        String code = allergyGroupService.deleteAllergyGroup(allergyGroupId);

        return httpApiResponse(new HttpApiResponse(code));
    }
}
