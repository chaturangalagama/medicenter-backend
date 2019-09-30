package com.ilt.cms.pm.integration.mapper.clinic.inventory;

import com.ilt.cms.api.entity.item.ItemEntity;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.item.ItemFilter;
import com.ilt.cms.pm.integration.mapper.Mapper;

public class ItemMapper extends Mapper {

    public static ItemEntity mapItemToEntity(Item item) {
        return new ItemEntity(item);
    }

}
