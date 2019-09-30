package com.ilt.cms.core.entity.item;

import com.ilt.cms.core.entity.PersistedObject;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.util.List;

@CompoundIndexes({
        @CompoundIndex(name = "index_unique", unique = true, def = "{'clinicId' : 1, 'itemsForClinic.itemRefId' : 1}")
})
public class ClinicItemMaster extends PersistedObject {

    private String clinicId;
    private List<ClinicItemPrice> itemsForClinic;


    public ClinicItemMaster() {
    }

    public ClinicItemMaster(String clinicId, List<ClinicItemPrice> itemsForClinic) {
        this.clinicId = clinicId;
        this.itemsForClinic = itemsForClinic;
    }

    public static class ClinicItemPrice {
        private String itemRefId;
        private int price;

        public ClinicItemPrice() {
        }

        public ClinicItemPrice(String itemRefId, int price) {
            this.itemRefId = itemRefId;
            this.price = price;
        }

        public String getItemRefId() {
            return itemRefId;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "ClinicGroupItemPrice{" +
                    "itemRefId='" + itemRefId + '\'' +
                    ", price=" + price +
                    '}';
        }
    }

    public String getClinicId() {
        return clinicId;
    }

    public List<ClinicItemPrice> getItemsForClinic() {
        return itemsForClinic;
    }

    @Override
    public String toString() {
        return "ClinicItemMaster{" +
                "clinicId='" + clinicId + '\'' +
                ", itemsForClinic=" + itemsForClinic +
                '}';
    }
}
