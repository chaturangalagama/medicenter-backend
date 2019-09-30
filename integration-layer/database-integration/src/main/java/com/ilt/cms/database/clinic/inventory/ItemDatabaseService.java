package com.ilt.cms.database.clinic.inventory;

import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.item.ItemFilter;

import java.util.List;
import java.util.Optional;

public interface ItemDatabaseService {

    Optional<Item> findItemByCode(String code);

    boolean existsByItemCode(String code);
    boolean existsById(String id);

    Item saveItem(Item item);

    Optional<Item> findItemByItemId(String itemId);

    List<Item> findItem(String itemNameRegex, String itemCodeRegex, List<String> supplierIds);

    List<Item> findItem(String itemNameRegex, String itemCodeRegex);

    List<Item> findItem(String clinicId, String itemNameRegex, String itemCodeRegex, List<String> supplierIds);

    List<Item> findItem(String clinicId, String itemNameRegex, String itemCodeRegex);

    List<Item> findAll();

    List<Item> findItemByClinicId(String clinicId);

    Optional<Item> findById(String id);

    Optional<Item> findByClinicIdAndCode(String clinicId, String itemCode);

    List<Item> findItemsByIds(List<String> ids);

    List<Item> findItemsByIds(List<String> ids, Item.ItemType itemType);

    Item findItemByIds(String id, Item.ItemType itemType);

    List<Item> findAll(ItemFilter itemFilter);

    Optional<Item> findItemByCode(String code, ItemFilter itemFilter);

    List<Item> findItem(String itemNameRegex, String itemCodeRegex, ItemFilter itemFilter);

    Optional<Item> findById(String id, ItemFilter itemFilter);

}
