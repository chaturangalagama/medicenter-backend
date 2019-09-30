package com.ilt.cms.pm.business.service.clinic.inventory;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.item.ItemFilter;
import com.ilt.cms.core.entity.system.SystemStore;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.clinic.inventory.ItemDatabaseService;
import com.ilt.cms.database.clinic.system.SystemStoreDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.CommonUtils;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    private ItemDatabaseService itemDatabaseService;
    private SystemStoreDatabaseService systemStoreDatabaseService;
    private ClinicDatabaseService clinicDatabaseService;

    public ItemService(ItemDatabaseService itemDatabaseService, SystemStoreDatabaseService systemStoreDatabaseService,
                       ClinicDatabaseService clinicDatabaseService) {
        this.itemDatabaseService = itemDatabaseService;
        this.systemStoreDatabaseService = systemStoreDatabaseService;
        this.clinicDatabaseService = clinicDatabaseService;
    }

    public boolean existsById(String id) {
        return itemDatabaseService.existsById(id);
    }

    public boolean existsByItemCode(String id) {
        return itemDatabaseService.existsByItemCode(id);
    }

    public Item searchItemByCode(String code) throws CMSException {
        Optional<Item> itemOpt = itemDatabaseService.findItemByCode(code);
        if(!itemOpt.isPresent()){
            logger.debug("Item not found for [code]:[{}]", code);
            throw new CMSException(StatusCode.E1002, "Item not exists for given code");
        }
        return itemOpt.get();
    }

    public List<Item> searchItemByRegex(String keyword) throws CMSException {
        if (!CommonUtils.isStringValid(keyword)) {
            logger.debug("Search keyword not in correct format");
            throw new CMSException(StatusCode.E1002, "Search parameter not valid");
        }
        List<Item> items = itemDatabaseService.findItem(keyword, keyword);
        logger.debug("Items [{}] found for keyword : [{}]", items.size(), keyword);
        return items;
    }

    public List<Item> listAllItems() {
        List<Item> items = itemDatabaseService.findAll();
        logger.debug("Search all Items found : [{}]", items.size());
        return items;
    }

    public Item searchItemById(String id) throws CMSException {
        Optional<Item> itemOpt = itemDatabaseService.findById(id);
        if (!itemOpt.isPresent()) {
            logger.debug("Item not found for [id]:[{}]", id);
            throw new CMSException(StatusCode.E1002, "Item not exists for given id");
        }
        logger.debug("Item found for [id]:[{}]", id);
        return itemOpt.get();
    }

    public List<Item> searchItemByIds(List<String> ids) {
        return itemDatabaseService.findItemsByIds(ids);
    }

    public List<SystemStore> findAllInstructions() {
        return systemStoreDatabaseService.findAll();
    }

    /*check by item filter*/
    public List<Item> listAllItems(List<String> clinicIds, List<String> clinicGroupNames) throws CMSException {

        if((clinicIds == null && clinicGroupNames == null)
                || (clinicIds != null && clinicIds.size() == 0 && clinicGroupNames == null)
                || (clinicGroupNames != null && clinicGroupNames.size() == 0 && clinicIds == null)
                || (clinicIds != null && clinicIds.size() == 0 && clinicGroupNames != null && clinicGroupNames.size() == 0)){
                throw new CMSException(StatusCode.E1002, "List is empty");
        }
        ItemFilter itemFilter = new ItemFilter();
        if(clinicIds != null){
            List<Clinic> clinics = clinicDatabaseService.listAllByIds(clinicIds);
            List<String> groupNames = clinics.stream()
                    .map(clinic -> clinic.getGroupName())
                    .collect(Collectors.toList());
            itemFilter.addClinicIds(clinicIds);
            itemFilter.addGroupNames(groupNames);
        }
        if(clinicGroupNames != null){
            itemFilter.addGroupNames(clinicGroupNames);
        }

        List<Item> items = itemDatabaseService.findAll(itemFilter);
        logger.debug("Search all Items found : [{}], itemFilter : [{}]", items.size(), itemFilter);
        return items;
    }

    public Item searchItemByCode(String code, List<String> clinicIds, List<String> clinicGroupNames) throws CMSException {
        if((clinicIds == null && clinicGroupNames == null)
                || (clinicIds != null && clinicIds.size() == 0 && clinicGroupNames == null)
                || (clinicGroupNames != null && clinicGroupNames.size() == 0 && clinicIds == null)
                || (clinicIds != null && clinicIds.size() == 0 && clinicGroupNames != null && clinicGroupNames.size() == 0)){
            throw new CMSException(StatusCode.E1002, "List is empty");
        }
        ItemFilter itemFilter = new ItemFilter();
        if(clinicIds != null){
            List<Clinic> clinics = clinicDatabaseService.listAllByIds(clinicIds);
            List<String> groupNames = clinics.stream()
                    .map(clinic -> clinic.getGroupName())
                    .collect(Collectors.toList());
            itemFilter.addClinicIds(clinicIds);
            itemFilter.addGroupNames(groupNames);
        }
        if(clinicGroupNames != null){
            itemFilter.addGroupNames(clinicGroupNames);
        }
        Optional<Item> itemOpt = itemDatabaseService.findItemByCode(code, itemFilter);
        if(!itemOpt.isPresent()){
            logger.debug("Item not found for [code]:[{}], [itemFilter]:[{}]", code, itemFilter);
            throw new CMSException(StatusCode.E1002, "Item not exists for given code");
        }
        logger.debug("Item found for [code]:[{}]", code);
        return itemOpt.get();
    }

    public Item searchItemById(String id, List<String> clinicIds, List<String> clinicGroupNames) throws CMSException {
        if((clinicIds == null && clinicGroupNames == null)
                || (clinicIds != null && clinicIds.size() == 0 && clinicGroupNames == null)
                || (clinicGroupNames != null && clinicGroupNames.size() == 0 && clinicIds == null)
                || (clinicIds != null && clinicIds.size() == 0 && clinicGroupNames != null && clinicGroupNames.size() == 0)){
            throw new CMSException(StatusCode.E1002, "List is empty");
        }
        ItemFilter itemFilter = new ItemFilter();
        if(clinicIds != null){
            List<Clinic> clinics = clinicDatabaseService.listAllByIds(clinicIds);
            List<String> groupNames = clinics.stream()
                    .map(clinic -> clinic.getGroupName())
                    .collect(Collectors.toList());
            itemFilter.addClinicIds(clinicIds);
            itemFilter.addGroupNames(groupNames);
        }
        if(clinicGroupNames != null){
            itemFilter.addGroupNames(clinicGroupNames);
        }
        Optional<Item> itemOpt = itemDatabaseService.findById(id, itemFilter);
        if (!itemOpt.isPresent()) {
            logger.debug("Item not found for [id]:[{}], [itemFilter]:[{}]", id, itemFilter);
            throw new CMSException(StatusCode.E1002, "Item not exists for given id");
        }
        logger.debug("Item found for [id]:[{}]", id);
        return itemOpt.get();
    }

    public List<Item> searchItemByRegex(String keyword, List<String> clinicIds, List<String> clinicGroupNames) throws CMSException {
        if (!CommonUtils.isStringValid(keyword)) {
            logger.debug("Search keyword not in correct format");
            throw new CMSException(StatusCode.E1002, "Search parameter not valid");
        }
        if((clinicIds == null && clinicGroupNames == null)
                || (clinicIds != null && clinicIds.size() == 0 && clinicGroupNames == null)
                || (clinicGroupNames != null && clinicGroupNames.size() == 0 && clinicIds == null)
                || (clinicIds != null && clinicIds.size() == 0 && clinicGroupNames != null && clinicGroupNames.size() == 0)){
            throw new CMSException(StatusCode.E1002, "List is empty");
        }
        ItemFilter itemFilter = new ItemFilter();
        if(clinicIds != null){
            List<Clinic> clinics = clinicDatabaseService.listAllByIds(clinicIds);
            List<String> groupNames = clinics.stream()
                    .map(clinic -> clinic.getGroupName())
                    .collect(Collectors.toList());
            itemFilter.addClinicIds(clinicIds);
            itemFilter.addGroupNames(groupNames);
        }
        if(clinicGroupNames != null){
            itemFilter.addGroupNames(clinicGroupNames);
        }
        List<Item> items = itemDatabaseService.findItem(keyword, keyword, itemFilter);
        logger.debug("Items [{}] found for keyword : [{}], itemFilter:[{}]", items.size(), keyword, itemFilter);
        return items;
    }

}
