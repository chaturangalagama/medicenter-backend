package com.ilt.cms.pm.rest.controller;

import com.ilt.cms.downstream.ItemDownstream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/item")
//@RolesAllowed("ROLE_VIEW_ITEM")
public class ItemRestController {

    private static final Logger logger = LoggerFactory.getLogger(ItemRestController.class);
    private ItemDownstream itemDownstream;

    public ItemRestController(ItemDownstream itemDownstream) {
        this.itemDownstream = itemDownstream;
    }


    @PostMapping("/list")
    public ResponseEntity listAll(Principal principal) {
        logger.info("Getting all items for user [" + principal.getName() + "]");
        return itemDownstream.searchAllItems();
    }

    @PostMapping("/search/{code}")
    public ResponseEntity searchItemByCode(Principal principal, @PathVariable("code") String code) {
        logger.info("Getting Item by [code]:[" + code + "] for user [" + principal.getName() + "]");
        return itemDownstream.searchItemByCode(code);
    }

    @PostMapping("/search/by-id/{id}")
    public ResponseEntity searchItemById(Principal principal, @PathVariable("id") String id) {
        logger.info("Getting Item by [id]:[" + id + "] for user [" + principal.getName() + "]");
        return itemDownstream.searchItemById(id);
    }

    @PostMapping("/filter/{keyword}")
    public ResponseEntity searchItemByKeyword(Principal principal, @PathVariable("keyword") String keyword) {
        logger.info("Filtering Item by [keyword]:[" + keyword + "] for user [" + principal.getName() + "]");
        return itemDownstream.filterItemByKeyword(keyword);
    }

    @PostMapping("/list/instructions")
    public ResponseEntity listInstructions(Principal principal){
        logger.info("Filtering Item instructions for user [" + principal.getName() + "]");
        return itemDownstream.getInstructions();
    }

    /*filter clinic id in below api*/
    @PostMapping("/list/clinic")
    public ResponseEntity listClinicAll(Principal principal, @RequestBody List<String> clinicIds) {
        logger.info("Getting all items for user [" + principal.getName() + "]");
        return itemDownstream.searchAllClinicItems(clinicIds);
    }

    @PostMapping("/search/clinic/{code}")
    public ResponseEntity searchClinicItemByCode(Principal principal, @PathVariable("code") String code,
                                                 @RequestBody List<String> clinicIds) {
        logger.info("Getting Item by [code]:[" + code + "] for user [" + principal.getName() + "]");
        return itemDownstream.searchClinicItemByCode(code, clinicIds);
    }

    @PostMapping("/search/clinic/by-id/{id}")
    public ResponseEntity searchClinicItemById(Principal principal, @PathVariable("id") String id,
                                               @RequestBody List<String> clinicIds) {
        logger.info("Getting Item by [id]:[" + id + "] for user [" + principal.getName() + "]");
        return itemDownstream.searchClinicItemById(id, clinicIds);
    }

    @PostMapping("/filter/clinic/{keyword}")
    public ResponseEntity searchClinicItemByKeyword(Principal principal, @PathVariable("keyword") String keyword,
                                                    @RequestBody List<String> clinicIds) {
        logger.info("Filtering Item by [keyword]:[" + keyword + "] for user [" + principal.getName() + "]");
        return itemDownstream.filterClinicItemByKeyword(keyword, clinicIds);
    }

    /*filter group name in below api*/
    @PostMapping("/list/group-name")
    public ResponseEntity listAll(Principal principal, @RequestBody List<String> clinicGroupNames) {
        logger.info("Getting all items for user [" + principal.getName() + "]");
        return itemDownstream.searchAllGroupItems(clinicGroupNames);
    }

    @PostMapping("/search/group-name/{code}")
    public ResponseEntity searchItemByCode(Principal principal, @PathVariable("code") String code,
                                           @RequestBody List<String> clinicGroupNames) {
        logger.info("Getting Item by [code]:[" + code + "] for user [" + principal.getName() + "]");
        return itemDownstream.searchGroupItemByCode(code, clinicGroupNames);
    }

    @PostMapping("/search/group-name/by-id/{id}")
    public ResponseEntity searchItemById(Principal principal, @PathVariable("id") String id,
                                         @RequestBody List<String> clinicGroupNames) {
        logger.info("Getting Item by [id]:[" + id + "] for user [" + principal.getName() + "]");
        return itemDownstream.searchGroupItemById(id, clinicGroupNames);
    }

    @PostMapping("/filter/group-name/{keyword}")
    public ResponseEntity searchItemByKeyword(Principal principal, @PathVariable("keyword") String keyword,
                                              @RequestBody List<String> clinicGroupNames) {
        logger.info("Filtering Item by [keyword]:[" + keyword + "] for user [" + principal.getName() + "]");
        return itemDownstream.filterGroupItemByKeyword(keyword, clinicGroupNames);
    }

}
