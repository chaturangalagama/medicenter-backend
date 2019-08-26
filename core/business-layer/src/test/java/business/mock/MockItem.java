package business.mock;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.item.Cost;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.item.SellingPrice;

public class MockItem {

    public static Item mockItem() {
        return mockItem("333333454");
    }

    public static Item mockItem(String itemId) {
        Item item = new Item();
        item.setId(itemId);
        item.setName("Paracetamol");
        item.setCode("PTM001");
        item.setDescription("Medicine used to treat pain and fever");
        item.setBaseUom("TABLET");
        item.setSalesUom("TABLET");
        item.setPurchaseUom("TABLET");
        item.setCategory("APAP");
        item.setCost(new Cost(100, true));
        item.setSellingPrice(new SellingPrice(120, true));
        item.setReorderPoint(100);
        item.setReorderQty(10000);
        item.setMinimumOrderQty(1000);
        item.setMaximumOrderQty(100000);
        item.setSafetyStockQty(1000);
        item.setInventoried(true);
        item.setAllowNegativeInventory(false);
        item.setSubItems(null);
        item.setSupplierId("SUP455752");
        item.setItemType(Item.ItemType.DRUG);
        item.setStatus(Status.ACTIVE);
        return item;
    }
}
