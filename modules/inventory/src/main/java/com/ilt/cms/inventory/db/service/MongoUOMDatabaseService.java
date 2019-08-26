package com.ilt.cms.inventory.db.service;

import com.ilt.cms.inventory.db.repository.spring.common.UOMRepository;
import com.ilt.cms.inventory.db.service.interfaces.UOMDatabaseService;
import com.ilt.cms.inventory.model.common.UOM;
import com.ilt.cms.inventory.model.common.UOMMaster;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MongoUOMDatabaseService implements UOMDatabaseService {

    private UOMRepository uomRepository;

    public MongoUOMDatabaseService(UOMRepository uomRepository){
        this.uomRepository = uomRepository;
    }

    @Override
    public UOMMaster saveUom(UOMMaster uomMaster) {
         return uomRepository.save(uomMaster);
    }

    @Override
    public Optional<UOMMaster> findUomByCode(String code) {
        return uomRepository.findByCode(code);

    }

    @Override
    public List<UOMMaster> findAll() {
        return uomRepository.findAll();
    }
}
