package com.ilt.cms.pm.integration.impl.clinic.inventory;

import com.ilt.cms.api.entity.item.ItemEntity;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.system.SystemStore;
import com.ilt.cms.downstream.clinic.inventory.ItemApiService;
import com.ilt.cms.pm.business.service.clinic.inventory.ItemService;
import com.ilt.cms.pm.integration.mapper.clinic.inventory.ItemMapper;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

@Service
public class DefaultItemApiService implements ItemApiService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultItemApiService.class);
    private ItemService itemService;

    public DefaultItemApiService(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public ResponseEntity<ApiResponse> searchAllItems() {
        try {
            List<Item> allItems = itemService.listAllItems();
            List<ItemEntity> itemEntities = new ArrayList<>();
            for (Item item : allItems) {
                itemEntities.add(ItemMapper.mapItemToEntity(item));
            }
            logger.info("Found all Items count [" + itemEntities.size() + "]");
            return httpApiResponse(new HttpApiResponse(itemEntities));
        } catch (Exception e) {
            logger.error("All item searching error: " + e);
            return httpApiResponse(new HttpApiResponse(e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchItemByCode(String code) {
        try {
            Item item = itemService.searchItemByCode(code);
           return httpApiResponse(new HttpApiResponse(ItemMapper.mapItemToEntity(item)));
        } catch (CMSException e) {
            logger.error("Item searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchItemById(String id) {
        try {
            Item item = itemService.searchItemById(id);
            return httpApiResponse(new HttpApiResponse(ItemMapper.mapItemToEntity(item)));
        } catch (CMSException e) {
            logger.error("Item searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> filterItemByKeyword(String keyword) {
        try {
            List<Item> items = itemService.searchItemByRegex(keyword);
            logger.info("Items found for filter keyword: [" + keyword + "]");
            List<ItemEntity> itemEntities = new ArrayList<>();
            for (Item item : items) {
                itemEntities.add(ItemMapper.mapItemToEntity(item));
            }
            return httpApiResponse(new HttpApiResponse(itemEntities));
        } catch (CMSException e) {
            logger.error("Item searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getInstructions() {
        logger.info("Loading drug instructions from system store...");
        List<SystemStore> allInstructions = itemService.findAllInstructions();
        Map<String, Object> map = new HashMap<>();
        allInstructions.forEach(systemStore -> {
            map.put(systemStore.getKey(), systemStore.getValues());
        });
        return httpApiResponse(new HttpApiResponse(map));
    }

    public ResponseEntity<ApiResponse> searchAllClinicItems(List<String> clinicIds) {
        try {
            List<Item> allItems = itemService.listAllItems(clinicIds, null);
            List<ItemEntity> itemEntities = new ArrayList<>();
            for (Item item : allItems) {
                itemEntities.add(ItemMapper.mapItemToEntity(item));
            }
            logger.info("Found all Items count [" + itemEntities.size() + "]");
            return httpApiResponse(new HttpApiResponse(itemEntities));
        } catch (CMSException e) {
            logger.error("All item searching error: " + e);
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchClinicItemByCode(String code, List<String> clinicIds) {
        try {
            Item item = itemService.searchItemByCode(code, clinicIds, null);
            return httpApiResponse(new HttpApiResponse(ItemMapper.mapItemToEntity(item)));
        } catch (CMSException e) {
            logger.error("Item searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchClinicItemById(String id, List<String> clinicIds) {
        try {
            Item item = itemService.searchItemById(id, clinicIds, null);
            return httpApiResponse(new HttpApiResponse(ItemMapper.mapItemToEntity(item)));
        } catch (CMSException e) {
            logger.error("Item searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> filterClinicItemByKeyword(String keyword, List<String> clinicIds) {
        try {
            List<Item> items = itemService.searchItemByRegex(keyword, clinicIds, null);
            logger.info("Items found for filter keyword: [" + keyword + "]");
            List<ItemEntity> itemEntities = new ArrayList<>();
            for (Item item : items) {
                itemEntities.add(ItemMapper.mapItemToEntity(item));
            }
            return httpApiResponse(new HttpApiResponse(itemEntities));
        } catch (CMSException e) {
            logger.error("Item searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    public ResponseEntity<ApiResponse> searchAllGroupItems(List<String> clinicGroupNames) {
        try {
            List<Item> allItems = itemService.listAllItems(null, clinicGroupNames);
            List<ItemEntity> itemEntities = new ArrayList<>();
            for (Item item : allItems) {
                itemEntities.add(ItemMapper.mapItemToEntity(item));
            }
            logger.info("Found all Items count [" + itemEntities.size() + "]");
            return httpApiResponse(new HttpApiResponse(itemEntities));
        } catch (CMSException e) {
            logger.error("All item searching error: " + e);
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchGroupItemByCode(String code, List<String> clinicGroupNames) {
        try {
            Item item = itemService.searchItemByCode(code, null, clinicGroupNames);
            return httpApiResponse(new HttpApiResponse(ItemMapper.mapItemToEntity(item)));
        } catch (CMSException e) {
            logger.error("Item searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> searchGroupItemById(String id, List<String> clinicGroupNames) {
        try {
            Item item = itemService.searchItemById(id, null, clinicGroupNames);
            return httpApiResponse(new HttpApiResponse(ItemMapper.mapItemToEntity(item)));
        } catch (CMSException e) {
            logger.error("Item searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> filterGroupItemByKeyword(String keyword, List<String> clinicGroupNames) {
        try {
            List<Item> items = itemService.searchItemByRegex(keyword, null, clinicGroupNames);
            logger.info("Items found for filter keyword: [" + keyword + "]");
            List<ItemEntity> itemEntities = new ArrayList<>();
            for (Item item : items) {
                itemEntities.add(ItemMapper.mapItemToEntity(item));
            }
            return httpApiResponse(new HttpApiResponse(itemEntities));
        } catch (CMSException e) {
            logger.error("Item searching error [" + e.getStatusCode() + "]:" + e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getCode(), e.getMessage()));
        }
    }

}
