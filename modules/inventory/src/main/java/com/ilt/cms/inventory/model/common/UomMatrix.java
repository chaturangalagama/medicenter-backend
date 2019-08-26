package com.ilt.cms.inventory.model.common;

import com.ilt.cms.core.entity.PersistedObject;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class UomMatrix extends PersistedObject {

    public UomMatrix() {
        exchangeRatio = new HashMap<>();
    }

    public UomMatrix(String uomCode, Map<String, BigDecimal> exchangeRatio) {
        this.uomCode = uomCode;
        this.exchangeRatio = exchangeRatio;
    }

    @Indexed(unique = true)
    private String uomCode;

    private Map<String, BigDecimal> exchangeRatio;

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public Map<String, BigDecimal> getExchangeRatio() {
        return exchangeRatio;
    }

    public void setExchangeRatio(Map<String, BigDecimal> exchangeRatio) {
        this.exchangeRatio = exchangeRatio;
    }
}
