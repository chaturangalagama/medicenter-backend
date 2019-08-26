package com.ilt.cms.inventory.mock;

import com.ilt.cms.inventory.model.common.UomMatrix;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MockUOMMatrix {

    public static UomMatrix mockUOMMatrix(){

        Map<String, BigDecimal> exchangeRatioMap = new HashMap<>();
        exchangeRatioMap.put("TAB", new BigDecimal(100));
        exchangeRatioMap.put("PCS", new BigDecimal(500));
        return mockUOMMatrix("BOX100", exchangeRatioMap);
    }

    public static UomMatrix mockUOMMatrix(String uomCode, Map<String, BigDecimal> exchangeRatioMap){
        UomMatrix uomMatrix = new UomMatrix();
        uomMatrix.setUomCode(uomCode);
        uomMatrix.setExchangeRatio(exchangeRatioMap);
        return uomMatrix;
    }
}
