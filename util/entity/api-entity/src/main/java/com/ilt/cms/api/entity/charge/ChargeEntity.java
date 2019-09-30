package com.ilt.cms.api.entity.charge;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ChargeEntity {
    private int price;
    private boolean taxIncluded;
    private List<OverrideDefaultCharge> overRideList;

    public ChargeEntity(int price, boolean taxIncluded) {
        this.price = price;
        this.taxIncluded = taxIncluded;
    }

    public Optional<ChargeEntity> anyOverRiddenCharge(Class<? extends Condition> type, Object obj) {
        if (overRideList != null && overRideList.size() > 0) {
            for (OverrideDefaultCharge charge : overRideList) {
                if (charge.doesConstraintMatch(type, obj)) {
                    return Optional.of(charge.getCharge());
                }
            }
        }
        return Optional.empty();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChargeEntity charge = (ChargeEntity) o;
        return Double.compare(charge.price, price) == 0 &&
                taxIncluded == charge.taxIncluded;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, taxIncluded);
    }

}
