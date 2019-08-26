package com.ilt.cms.inventory.service.common;

import com.ilt.cms.inventory.db.service.interfaces.UOMMatrixDatabaseService;
import com.ilt.cms.inventory.model.common.UomMatrix;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UOMMatrixService {
    private static final Logger logger = LoggerFactory.getLogger(UOMMatrixService.class);

    private UOMMatrixDatabaseService uomMatrixDatabaseService;

    public UOMMatrixService(UOMMatrixDatabaseService uomMatrixDatabaseService){
        this.uomMatrixDatabaseService = uomMatrixDatabaseService;
    }

    public double findRatio(String sourceUomCode, String distinctionUomCode) throws CMSException {
        if(sourceUomCode.equals(distinctionUomCode)){
            return 1;
        }

        Optional<UomMatrix> uomMatrixOpt = uomMatrixDatabaseService.findUOMatrixByUomCode(sourceUomCode);
        if(!uomMatrixOpt.isPresent()){
            logger.warn("UomMatrix not found, code["+sourceUomCode+"]");
            Map<String, BigDecimal> exchangeRaito = new HashMap<>();
            exchangeRaito.put(sourceUomCode, new BigDecimal(1));
            UomMatrix uomMatrix = new UomMatrix(sourceUomCode, exchangeRaito);
            uomMatrix = uomMatrixDatabaseService.saveUOMatrix(uomMatrix);
            uomMatrixOpt = Optional.of(uomMatrix);
        }
        UomMatrix uomMatrix = uomMatrixOpt.get();
        BigDecimal ratio = uomMatrix.getExchangeRatio().get(distinctionUomCode);
        if(ratio == null){
            throw new CMSException(StatusCode.E2000, "Cannot found ratio from["+sourceUomCode+"] to ["+distinctionUomCode+"]");
        }
        return ratio.doubleValue();
    }

    public UomMatrix saveUOMMatrix(UomMatrix uomMatrix) throws CMSException {
        Optional<UomMatrix> uomMatrixOpt = uomMatrixDatabaseService.findUOMatrixByUomCode(uomMatrix.getUomCode());
        UomMatrix curUomMatrix;
        if(!uomMatrixOpt.isPresent()){
            curUomMatrix = new UomMatrix();
        }
        curUomMatrix = uomMatrixOpt.get();
        curUomMatrix.setExchangeRatio(uomMatrix.getExchangeRatio());
        return uomMatrixDatabaseService.saveUOMatrix(curUomMatrix);
    }

    public UomMatrix findUOMMatrixByUomCode(String uomCode) throws CMSException {
        Optional<UomMatrix> uomMatrixOpt = uomMatrixDatabaseService.findUOMatrixByUomCode(uomCode);
        if(!uomMatrixOpt.isPresent()){
            throw new CMSException(StatusCode.E2000);
        }
        return uomMatrixOpt.get();
    }
}
