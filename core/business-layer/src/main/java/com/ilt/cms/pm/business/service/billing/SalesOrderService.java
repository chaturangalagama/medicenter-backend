package com.ilt.cms.pm.business.service.billing;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.casem.SalesItem;
import com.ilt.cms.core.entity.casem.SalesOrder;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.pm.business.service.claim.ClaimService;
import com.ilt.cms.repository.spring.ItemRepository;
import com.ilt.cms.repository.spring.SalesOrderRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalesOrderService {

    private static final Logger logger = LoggerFactory.getLogger(SalesOrderService.class);
    private SalesOrderRepository salesOrderRepository;
    private RunningNumberService runningNumberService;
    private ItemRepository itemRepository;
    private ClaimService claimService;

    public SalesOrderService(SalesOrderRepository salesOrderRepository, RunningNumberService runningNumberService,
                             ItemRepository itemRepository, ClaimService claimService) {
        this.salesOrderRepository = salesOrderRepository;
        this.runningNumberService = runningNumberService;
        this.itemRepository = itemRepository;
        this.claimService = claimService;
    }

    /**
     * Creates and persists a new Sales Order
     *
     * @param gstValue
     * @return
     */
    public SalesOrder generateNewSalesOrder(int gstValue) {
        SalesOrder salesOrder = SalesOrder.newSalesOrder(gstValue, runningNumberService.generateSalesOrderNumber());
        logger.info("persisting new sales order [" + salesOrder + "]");
        salesOrder = salesOrderRepository.save(salesOrder);
        return salesOrder;
    }

    public SalesOrder updateSalesOrder(SalesOrder salesOrder) throws CMSException {
        logger.debug("Updating sales order [{}]", salesOrder);
        salesOrder = claimService.populateClaims(salesOrder);
        return salesOrderRepository.save(salesOrder);
    }

    /**
     * Removes the existing sales items and adds the new list
     *
     * @param salesOrderId
     * @param salesItemList
     * @return
     * @throws CMSException
     */
    public SalesOrder updateSalesItems(String salesOrderId, List<SalesItem> salesItemList) throws CMSException {
        logger.info("Updating sales order [" + salesOrderId + "]");
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> {
                    logger.error("No Sales order found for id [" + salesOrderId + "]");
                    return new CMSException(StatusCode.E2000, "Invalid sales order id");
                });
        List<String> itemList = salesItemList.stream()
                .map(SalesItem::getItemRefId)
                .collect(Collectors.toList());

        Map<String, Item> itemMap = new HashMap<>(itemList.size() * 2);

        for (Item item : itemRepository.findAllById(itemList)) {
            itemMap.put(item.getId(), item);
            if (item.getStatus() != Status.ACTIVE) {
                logger.error("There is none active item in the sales order list [" + item + "]");
                throw new CMSException(StatusCode.E2000, "One more items are not active");
            }
        }
        if (itemList.size() != itemMap.size()) {
            logger.error("not all items from the sales order request can be loaded from db [" + itemList
                    + "] mapped list [" + itemMap.keySet() + "]");
        }
        logger.info("populating sales ourder items");
        List<SalesItem> populatesSalesItemList = salesItemList.stream()
                .map(salesItem -> new SalesItem(itemMap.get(salesItem.getItemRefId()), salesItem))
                .collect(Collectors.toList());

        salesOrder.updatePurchaseItems(populatesSalesItemList);
        logger.debug("persisting sales order [{}]", salesOrder);
        return salesOrderRepository.save(salesOrder);
    }
}
