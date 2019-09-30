package com.ilt.cms.pm.business.service.patient.patientVisit;

import com.ilt.cms.core.entity.visit.VisitPurpose;
import com.ilt.cms.database.patient.patientVisit.VisitPurposeDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitPurposeService {

    private static final Logger logger = LoggerFactory.getLogger(VisitPurposeService.class);

    private VisitPurposeDatabaseService visitPurposeDatabaseService;

    public VisitPurposeService(VisitPurposeDatabaseService visitPurposeDatabaseService) {
        this.visitPurposeDatabaseService = visitPurposeDatabaseService;
    }

    public List<VisitPurpose> listAll() {
        logger.info("Finding all visit purpose");
        List<VisitPurpose> all = visitPurposeDatabaseService.findAll();
        logger.info("Found [" + all.size() + "] purposes");
        return all;
    }

    public VisitPurpose addNew(VisitPurpose visitPurpose) throws CMSException {
        if (!visitPurpose.areParametersValid()) {
            logger.warn("Invalid parameters [" + visitPurpose + "]");
            //return new CmsServiceResponse<>(StatusCode.E1002);
            throw new CMSException(StatusCode.E1002);
        } else {
            boolean nameExists = visitPurposeDatabaseService.checkIfNameExists(visitPurpose.getName());
            if (nameExists) {
                logger.warn("Name already exists, [" + visitPurpose.getName() + "]");
                //return new CmsServiceResponse<>(StatusCode.E1007);
                throw new CMSException(StatusCode.E1007);
            }
            VisitPurpose savedVersion = visitPurposeDatabaseService.save(visitPurpose);
            return savedVersion;
        }
    }

    public VisitPurpose modify(String visitPurposeId, VisitPurpose visitPurpose) throws CMSException {
        logger.info("modifying visit purpose [" + visitPurposeId + "]");
        VisitPurpose current = visitPurposeDatabaseService.findOne(visitPurposeId);
        if (current == null) {
            //return new CmsServiceResponse<>(StatusCode.E2000);
            throw new CMSException(StatusCode.E2000);
        } else {
            logger.debug("visit purpose found");
            if (!visitPurpose.areParametersValid()) {
                logger.error("Given new parameters not valid");
                //return new CmsServiceResponse<>(StatusCode.E2000);
                throw new CMSException(StatusCode.E2000);
            } else if (visitPurposeDatabaseService.checkIfNameExistsNotTheSameId(visitPurpose.getName(), visitPurposeId)) {
                logger.warn("there is another name available with the given [" + visitPurpose + "]");
                //return new CmsServiceResponse<>(StatusCode.E2000);
                throw new CMSException(StatusCode.E2000);
            }
            current.copy(visitPurpose);
            logger.debug("Validation success, persisting the visit purpose [" + current + "] id [" + current.getId() + "]");
            VisitPurpose savedVersion = visitPurposeDatabaseService.save(current);
            return savedVersion;
        }
    }

    public boolean remove(String visitPurposeId) {
        return visitPurposeDatabaseService.delete(visitPurposeId);
        //return new CmsServiceResponse<>(StatusCode.S0000);
    }
}
