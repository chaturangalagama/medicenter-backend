package com.ilt.cms.downstream.clinic.inventory;

import com.lippo.commons.web.api.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ItemApiService {

    ResponseEntity<ApiResponse> searchAllItems();

    ResponseEntity<ApiResponse> searchItemByCode(String code);

    ResponseEntity<ApiResponse> searchItemById(String id);

    ResponseEntity<ApiResponse> filterItemByKeyword(String keyword);

    ResponseEntity<ApiResponse> getInstructions();

    ResponseEntity<ApiResponse> searchAllClinicItems(List<String> clinicIds);

    ResponseEntity<ApiResponse> searchClinicItemByCode(String code, List<String> clinicIds);

    ResponseEntity<ApiResponse> searchClinicItemById(String id, List<String> clinicIds);

    ResponseEntity<ApiResponse> filterClinicItemByKeyword(String keyword, List<String> clinicIds);

    ResponseEntity<ApiResponse> searchAllGroupItems(List<String> clinicGroupNames);

    ResponseEntity<ApiResponse> searchGroupItemByCode(String code, List<String> clinicGroupNames);

    ResponseEntity<ApiResponse> searchGroupItemById(String id, List<String> clinicGroupNames);

    ResponseEntity<ApiResponse> filterGroupItemByKeyword(String keyword, List<String> clinicGroupNames);
}
