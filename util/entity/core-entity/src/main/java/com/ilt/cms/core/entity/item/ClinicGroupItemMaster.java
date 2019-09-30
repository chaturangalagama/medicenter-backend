package com.ilt.cms.core.entity.item;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.charge.Charge;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * <code>{@link ClinicGroupItemMaster}</code> -
 * Contains Item detail for Clinic Groups.
 * </p>
 *
 * @author prabath.
 */
@CompoundIndexes({
        @CompoundIndex(name = "index_unique", unique = true, def = "{'groupName' : 1, 'clinicGroupItemPrices.itemRefId' : 1}")
})
public class ClinicGroupItemMaster extends PersistedObject {

    private String groupName;
    private List<ClinicGroupItemPrice> clinicGroupItemPrices;

    public static class ClinicGroupItemPrice {
        private String itemRefId;
        private int price;

        public ClinicGroupItemPrice() {
        }

        public ClinicGroupItemPrice(String itemRefId, int price) {
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

    public ClinicGroupItemMaster() {
        clinicGroupItemPrices = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ClinicGroupItemPrice> getClinicGroupItemPrices() {
        return clinicGroupItemPrices;
    }

    public void setClinicGroupItemPrices(List<ClinicGroupItemPrice> clinicGroupItemPrices) {
        this.clinicGroupItemPrices = clinicGroupItemPrices;
    }


    @Override
    public String toString() {
        return "ClinicGroupItemMaster{" +
                "groupName='" + groupName + '\'' +
                ", clinicGroupItemPrices=" + clinicGroupItemPrices +
                '}';
    }
}
