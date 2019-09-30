package com.ilt.cms.repository.clinic;

import com.ilt.cms.core.entity.item.DrugItem;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.repository.clinic.inventory.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


/**
 * <p>
 * <code>{@link TestMongoRepository}</code> -
 * Contains unit tests for <code>{@link CaseRepository}</code>
 * </p>
 */
@RunWith(SpringRunner.class)
@DataMongoTest(
        includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = {"com.ilt.cms.repository.spring.*"}))
public class TestMongoRepository {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testSaveSubItems() {
        Item consumableItem = itemRepository.save(new Item(false, Item.ItemType.CONSUMABLE));
        DrugItem drugItem = itemRepository.save(new DrugItem());
        Item serviceItem = itemRepository.save(new Item(false, Item.ItemType.SERVICE));


        Optional<Item> savedConsumable = itemRepository.findById(consumableItem.getId());
        Optional<Item> savedDrug = itemRepository.findById(drugItem.getId());
        Optional<Item> savedService = itemRepository.findById(serviceItem.getId());

        System.out.println("============================================");
        System.out.println(savedConsumable.get());
        System.out.println(savedDrug.get());
        System.out.println(savedService.get());
        System.out.println("============================================");
    }

}

