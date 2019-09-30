package com.ilt.cms.pm.rest.controller.clinic.inventory;

import com.ilt.cms.downstream.clinic.inventory.ItemApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/item")
//@RolesAllowed("ROLE_VIEW_ITEM")
public class ItemRestController {

    private static final Logger logger = LoggerFactory.getLogger(ItemRestController.class);
    private ItemApiService itemApiService;

    public ItemRestController(ItemApiService itemApiService) {
        this.itemApiService = itemApiService;
    }


    @PostMapping("/list")
    public ResponseEntity listAll(Principal principal) {
        logger.info("Getting all items for user [" + principal.getName() + "]");
        return itemApiService.searchAllItems();
    }

    @PostMapping("/search/{code}")
    public ResponseEntity searchItemByCode(Principal principal, @PathVariable("code") String code) {
        logger.info("Getting Item by [code]:[" + code + "] for user [" + principal.getName() + "]");
        return itemApiService.searchItemByCode(code);
    }

    @PostMapping("/search/by-id/{id}")
    public ResponseEntity searchItemById(Principal principal, @PathVariable("id") String id) {
        logger.info("Getting Item by [id]:[" + id + "] for user [" + principal.getName() + "]");
        return itemApiService.searchItemById(id);
    }

    @PostMapping("/filter/{keyword}")
    public ResponseEntity searchItemByKeyword(Principal principal, @PathVariable("keyword") String keyword) {
        logger.info("Filtering Item by [keyword]:[" + keyword + "] for user [" + principal.getName() + "]");
        return itemApiService.filterItemByKeyword(keyword);
    }

    @PostMapping("/list/instructions")
    public ResponseEntity listInstructions(Principal principal){
        logger.info("Filtering Item instructions for user [" + principal.getName() + "]");
        return itemApiService.getInstructions();
    }

    /*filter clinic id in below api*/
    @PostMapping("/list/clinic")
    public ResponseEntity listClinicAll(Principal principal, @RequestBody List<String> clinicIds) {
        logger.info("Getting all items for user [" + principal.getName() + "]");
        return itemApiService.searchAllClinicItems(clinicIds);
    }

    @PostMapping("/search/clinic/{code}")
    public ResponseEntity searchClinicItemByCode(Principal principal, @PathVariable("code") String code,
                                                 @RequestBody List<String> clinicIds) {
        logger.info("Getting Item by [code]:[" + code + "] for user [" + principal.getName() + "]");
        return itemApiService.searchClinicItemByCode(code, clinicIds);
    }

    @PostMapping("/search/clinic/by-id/{id}")
    public ResponseEntity searchClinicItemById(Principal principal, @PathVariable("id") String id,
                                               @RequestBody List<String> clinicIds) {
        logger.info("Getting Item by [id]:[" + id + "] for user [" + principal.getName() + "]");
        return itemApiService.searchClinicItemById(id, clinicIds);
    }

    @PostMapping("/filter/clinic/{keyword}")
    public ResponseEntity searchClinicItemByKeyword(Principal principal, @PathVariable("keyword") String keyword,
                                                    @RequestBody List<String> clinicIds) {
        logger.info("Filtering Item by [keyword]:[" + keyword + "] for user [" + principal.getName() + "]");
        return itemApiService.filterClinicItemByKeyword(keyword, clinicIds);
    }

    /*filter group name in below api*/
    @PostMapping("/list/group-name")
    public ResponseEntity listAll(Principal principal, @RequestBody List<String> clinicGroupNames) {
        logger.info("Getting all items for user [" + principal.getName() + "]");
        return itemApiService.searchAllGroupItems(clinicGroupNames);
    }

    @PostMapping("/search/group-name/{code}")
    public ResponseEntity searchItemByCode(Principal principal, @PathVariable("code") String code,
                                           @RequestBody List<String> clinicGroupNames) {
        logger.info("Getting Item by [code]:[" + code + "] for user [" + principal.getName() + "]");
        return itemApiService.searchGroupItemByCode(code, clinicGroupNames);
    }

    @PostMapping("/search/group-name/by-id/{id}")
    public ResponseEntity searchItemById(Principal principal, @PathVariable("id") String id,
                                         @RequestBody List<String> clinicGroupNames) {
        logger.info("Getting Item by [id]:[" + id + "] for user [" + principal.getName() + "]");
        return itemApiService.searchGroupItemById(id, clinicGroupNames);
    }

    @PostMapping("/filter/group-name/{keyword}")
    public ResponseEntity searchItemByKeyword(Principal principal, @PathVariable("keyword") String keyword,
                                              @RequestBody List<String> clinicGroupNames) {
        logger.info("Filtering Item by [keyword]:[" + keyword + "] for user [" + principal.getName() + "]");
        return itemApiService.filterGroupItemByKeyword(keyword, clinicGroupNames);
    }

}
