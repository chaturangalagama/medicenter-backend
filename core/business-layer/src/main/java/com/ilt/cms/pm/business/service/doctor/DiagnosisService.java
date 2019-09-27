package com.ilt.cms.pm.business.service.doctor;

import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.diagnosis.Diagnosis;
import com.ilt.cms.database.coverage.MedicalCoverageDatabaseService;
import com.ilt.cms.database.diagnosis.DiagnosisDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiagnosisService {
    private static final Logger logger = LoggerFactory.getLogger(DiagnosisService.class);
    private DiagnosisDatabaseService diagnosisDatabaseService;
    private MedicalCoverageDatabaseService medicalCoverageDatabaseService;

    public DiagnosisService(DiagnosisDatabaseService diagnosisDatabaseService, MedicalCoverageDatabaseService medicalCoverageDatabaseService) {
        this.diagnosisDatabaseService = diagnosisDatabaseService;
        this.medicalCoverageDatabaseService = medicalCoverageDatabaseService;
    }

    public List<Diagnosis> search(List<String> planIds, String term) {
        logger.info("Searching the system for the term [" + term + "]");
        //List<Diagnosis> diagnosisList = diagnosisDatabaseService.search(term);
        boolean filterDiagnosis = planIds != null && planIds.stream()
                .anyMatch(planId-> {
                    CoveragePlan plan = medicalCoverageDatabaseService.findPlan(planId);
                    if(plan != null){
                        return plan.isFilterDiagnosisCode();
                    }else{
                        return false;
                    }
                });

        List<Diagnosis> diagnosisList;
        if(!filterDiagnosis){
            diagnosisList = diagnosisDatabaseService.search(term);
        }else {
            diagnosisList = diagnosisDatabaseService.searchFilerByPlan(planIds, term);
        }
        logger.info("found [" + diagnosisList.size() + "]");
        return diagnosisList;
    }

    public List<Diagnosis> searchById(List<String> diagnosisIds) {
        logger.info("Searching the system for diagnosisIds [" + diagnosisIds + "]");
        Iterable<Diagnosis> diagnoses = diagnosisDatabaseService.findAll(diagnosisIds);
        List<Diagnosis> target = new ArrayList<>();
        diagnoses.forEach(target::add);
        logger.info("found [" + target.size() + "]");
        return target;
    }

    public boolean checkDiagnosisIdsValidity(List<String> ids) throws CMSException {
        for(String id:ids){
            if(!diagnosisDatabaseService.exists(id)){
                logger.debug("Invalid diagnosis ID found");
                throw new CMSException(StatusCode.E1002,"Invalid diagnosis id present");
            }
        }
        return true;
    }
}
