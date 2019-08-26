package com.ilt.cms.inventory.mock;

import com.ilt.cms.inventory.model.common.UOMMaster;

public class MockUOMMaster {

    public static UOMMaster mockUOMMaster(String id, String uomCode, String displayName){
        UOMMaster uomMaster = new UOMMaster();
        uomMaster.setId(id);
        uomMaster.setCode(uomCode);
        uomMaster.setDisplayName(displayName);
        return uomMaster;
    }
}
