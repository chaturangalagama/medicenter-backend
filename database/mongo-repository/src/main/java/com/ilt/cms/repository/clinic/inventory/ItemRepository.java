package com.ilt.cms.repository.clinic.inventory;

import com.ilt.cms.core.entity.item.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

    Optional<Item> findByCode(String code);

    boolean existsByCode(String code);

    @Query(value = "{'name' : {$regex : ?0, $options : 'i'}}")
    List<Item> findAllByNameRegex(String regexString, Sort sort);

    @Query(value = "{'brandName' : {$regex : ?0, $options : 'i'}}")
    List<Item> findAllByDrugBrandNameRegex(String brandNameRegex, Sort sort);

    @Query(value = "{$or : [{'name' : {$regex : ?0, $options : 'i'}}, {'code' : {$regex : ?1, $options : 'i'}}, {'supplierId' : {$in : ?2}}]}")
    List<Item> searchItem(String itemNameRegex, String itemCodeRegex, List<String> supplierIds, Sort sort);

    @Query(value = "{$or : [{'name' : {$regex : ?0, $options : 'i'}}, {'code' : {$regex : ?1, $options : 'i'}}]}")
    List<Item> searchItem(String itemNameRegex, String itemCodeRegex, Sort sort);

    @Query(value = "{ $and : [{'clinicIds' : ?0}, " +
            "{$or : [{'name' : {$regex : ?1, $options : 'i'}}, {'code' : {$regex : ?2, $options : 'i'}}, {'supplierId' : {$in : ?3}}]}]}")
    List<Item> searchItem(String clinicId, String itemNameRegex, String itemCodeRegex, List<String> supplierIds, Sort sort);

    @Query(value = "{ $and : [{'clinicIds' : ?0}, " +
            "{$or : [{'name' : {$regex : ?1, $options : 'i'}}, {'code' : {$regex : ?2, $options : 'i'}}]}]}")
    List<Item> searchItem(String clinicId, String itemNameRegex, String itemCodeRegex, Sort sort);

    Optional<Item> findByItemFilterClinicIdsAndCode(String clinicId, String code);

    List<Item> findAllByItemFilterClinicIds(String clinicId, Sort sort);

    Item findByIdAndItemType(String id, Item.ItemType type);

    List<Item> findByIdInAndItemType(Collection<String> id, Item.ItemType type);

    List<Item> findAllByItemFilterClinicIdsInOrItemFilterClinicGroupNamesIn(List<String> clinicIds, List<String> clinicGroupNames, Sort sort);

    Optional<Item> findByCodeAndItemFilterClinicIdsInOrItemFilterClinicGroupNamesIn(String code, List<String> clinicIds, List<String> clinicGroupNames, Sort sort);

    @Query(value = "{ $and :[" +
            "{$or : [{'name' : {$regex : ?0, $options : 'i'}}, {'code' : {$regex : ?1, $options : 'i'}}]}, " +
            "{ $or : [{'itemFilter.clinicIds' : {$in : ?2}}, " +
            "{'itemFilter.clinicGroupNames' : { $in : ?3}}" +
            "]}" +
            "]}")
    List<Item> searchItem(String itemNameRegex, String itemCodeRegex, List<String> clinicIds, List<String> groupNames, Sort sort);

    Optional<Item> findByIdAndItemFilterClinicIdsInOrItemFilterClinicGroupNamesIn(String id, List<String> clinicIds, List<String> clinicGroupNames, Sort sort);
}
