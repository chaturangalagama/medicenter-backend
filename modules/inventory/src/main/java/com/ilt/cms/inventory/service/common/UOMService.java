package com.ilt.cms.inventory.service.common;

import com.ilt.cms.inventory.db.service.interfaces.UOMDatabaseService;
import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.common.UOMMaster;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UOMService {

    private UOMDatabaseService uomDatabaseService;

    public UOMService(UOMDatabaseService uomDatabaseService){
        this.uomDatabaseService = uomDatabaseService;
    }

    public UOM save(UOM uom){
        UOMMaster save = uomDatabaseService.saveUom(new UOMMaster(uom.getCode(), uom.getDisplayName()));
        if(save != null){
            return new UOM(save.getCode(), save.getDisplayName());
        }else{
            return null;
        }
    }

    public Optional<UOM> findUomByCode(String code) {
        Optional<UOMMaster> uomMasterOpt = uomDatabaseService.findUomByCode(code);
        if(uomMasterOpt.isPresent()){
            return Optional.of(new UOM(uomMasterOpt.get().getCode(), uomMasterOpt.get().getDisplayName()));
        }else{
            return Optional.empty();
        }
    }

    public List<UOM> listUOM(){
        List<UOMMaster> uomMasters = uomDatabaseService.findAll();
        return uomMasters.stream()
                .map(uomMaster -> new UOM(uomMaster.getCode(), uomMaster.getDisplayName()))
                .collect(Collectors.toList());
    }
}
