package com.ilt.cms.pm.business.service.clinic;

import com.ilt.cms.core.entity.label.Label;
import com.ilt.cms.database.clinic.LabelDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    private static final Logger logger = LoggerFactory.getLogger(LabelService.class);

    private LabelDatabaseService labelDatabaseService;

    public LabelService(LabelDatabaseService labelDatabaseService){
        this.labelDatabaseService = labelDatabaseService;
    }

    public List<Label> listAll() {
        logger.info("listing all labels");
        List<Label> labels = labelDatabaseService.findAll();
        logger.info("found [" + labels.size() + "] labels");
        return labels;
    }

    public Label findByName(String name) {
        logger.info("finding by name [" + name + "]");
        Label label = labelDatabaseService.findByName(name);
        return label;
    }

    public Label add(Label label) throws CMSException {
        logger.info("adding a new label [" + label + "]");
        if (labelDatabaseService.checkNameExists(label.getName())) {
            logger.error("Given name already exists");
            //return new CmsServiceResponse<>(StatusCode.E1004);
            throw new CMSException(StatusCode.E1004);
        } else {
            boolean parametersValid = label.areParametersValid();
            if (!parametersValid) {
                logger.error("Parameters are not valid [" + label + "]");
                //return new CmsServiceResponse<>(StatusCode.E1006);
                throw new CMSException(StatusCode.E1006);
            }
            logger.info("saving label");
            Label savedVersion = labelDatabaseService.save(label);
            return savedVersion;
        }
    }

    public Label modify(String labelId, Label updateLabel) throws CMSException {
        logger.info("modifying label [" + labelId + "]");
        Label label = labelDatabaseService.findOne(labelId);
        if (label == null) {
            logger.error("No record found for [" + labelId + "]");
            //return new CmsServiceResponse<>(StatusCode.E2000);
            throw new CMSException(StatusCode.E2000);
        } else {
            boolean parametersValid = updateLabel.areParametersValid();
            if (!parametersValid) {
                logger.error("Parameters are not valid [" + updateLabel + "]");
                //return new CmsServiceResponse<>(StatusCode.E1006);
                throw new CMSException(StatusCode.E1006);
            } else {
                label.copy(updateLabel);
                Label savedVersion = labelDatabaseService.save(label);
                return savedVersion;
            }
        }
    }
}
