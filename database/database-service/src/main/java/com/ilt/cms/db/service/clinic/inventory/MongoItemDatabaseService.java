package com.ilt.cms.db.service.clinic.inventory;

import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.item.ItemFilter;
import com.ilt.cms.database.clinic.inventory.ItemDatabaseService;
import com.ilt.cms.repository.clinic.inventory.ItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MongoItemDatabaseService implements ItemDatabaseService {

    private ItemRepository itemRepository;

    public MongoItemDatabaseService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    @Override
    public Optional<Item> findItemByCode(String code) {
        return itemRepository.findByCode(code);
    }

    @Override
    public boolean existsByItemCode(String code) {
        return itemRepository.existsByCode(code);
    }

    @Override
    public boolean existsById(String id) {
        return itemRepository.existsById(id);
    }

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Optional<Item> findItemByItemId(String itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public List<Item> findItem(String itemNameRegex, String itemCodeRegex, List<String> supplierIds) {
        return itemRepository.searchItem(itemNameRegex, itemCodeRegex, supplierIds, new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<Item> findItem(String itemNameRegex, String itemCodeRegex) {
        return itemRepository.searchItem(itemNameRegex, itemCodeRegex, new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<Item> findItem(String clinicId, String itemNameRegex, String itemCodeRegex, List<String> supplierIds) {
        return itemRepository.searchItem(clinicId, itemNameRegex, itemCodeRegex, supplierIds, new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<Item> findItem(String clinicId, String itemNameRegex, String itemCodeRegex) {
        return itemRepository.searchItem(clinicId, itemNameRegex, itemCodeRegex, new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> findItemByClinicId(String clinicId) {
        return itemRepository.findAllByItemFilterClinicIds(clinicId, new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public Optional<Item> findById(String id) {
        return itemRepository.findById(id);

    }

    @Override
    public Optional<Item> findByClinicIdAndCode(String clinicId, String itemCode) {
        return itemRepository.findByItemFilterClinicIdsAndCode(clinicId, itemCode);
    }

    @Override
    public List<Item> findItemsByIds(List<String> ids) {
        ArrayList<Item> items = new ArrayList<>();
        itemRepository.findAllById(ids).forEach(items::add);
        return items;
    }

    @Override
    public List<Item> findItemsByIds(List<String> ids, Item.ItemType itemType) {
        return itemRepository.findByIdInAndItemType(ids, itemType);
    }

    @Override
    public Item findItemByIds(String id, Item.ItemType itemType) {
        return itemRepository.findByIdAndItemType(id, itemType);
    }

    @Override
    public List<Item> findAll(ItemFilter itemFilter) {
        return itemRepository.findAllByItemFilterClinicIdsInOrItemFilterClinicGroupNamesIn(itemFilter.getClinicIds(),
                itemFilter.getClinicGroupNames(), new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public Optional<Item> findItemByCode(String code, ItemFilter itemFilter) {
        return itemRepository.findByCodeAndItemFilterClinicIdsInOrItemFilterClinicGroupNamesIn(code,
                itemFilter.getClinicIds(), itemFilter.getClinicGroupNames(), new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<Item> findItem(String itemNameRegex, String itemCodeRegex, ItemFilter itemFilter) {
        return itemRepository.searchItem(itemNameRegex, itemCodeRegex, itemFilter.getClinicIds(),
                itemFilter.getClinicGroupNames(), new Sort(Sort.Direction.ASC, "name"));
    }

    @Override
    public Optional<Item> findById(String id, ItemFilter itemFilter) {
        return itemRepository.findByIdAndItemFilterClinicIdsInOrItemFilterClinicGroupNamesIn(id, itemFilter.getClinicIds(),
                itemFilter.getClinicGroupNames(), new Sort(Sort.Direction.ASC, "name"));
    }
}
