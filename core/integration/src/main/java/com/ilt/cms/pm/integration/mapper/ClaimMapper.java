package com.ilt.cms.pm.integration.mapper;

import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.api.JMapperAPI;
import com.googlecode.jmapper.api.enums.MappingType;
import com.ilt.cms.api.entity.claim.ClaimViewEntity;
import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.claim.ClaimViewCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.googlecode.jmapper.api.JMapperAPI.*;

/**
 * <p>
 * <code>{@link ClaimMapper}</code> -
 * API to/from Core entity mapper utility class.
 * </p>
 *
 * @author prabath.
 */
@Service("claimMapperService")
public class ClaimMapper {

    private static final Logger logger = LoggerFactory.getLogger(ClaimMapper.class);

    private JMapper<ClaimViewEntity, ClaimViewCore> coreToApiMapper;

    private JMapper<ClaimViewCore, ClaimViewEntity> apiToCoreMapper;

    @PostConstruct
    public void init() {
        JMapperAPI coreToApiMapperConfiguration = new JMapperAPI()
                .add(mappedClass(ClaimViewEntity.class)
                        .add(attribute("claim").value("claim"))
                        .add(attribute("billDate").value("billDate"))
                        .add(attribute("clinicId").value("clinicId"))
                        .add(attribute("clinicHeCode").value("clinicHeCode"))
                        .add(attribute("hospitalCode").value("hospitalCode"))
                        .add(attribute("claimDoctorId").value("claimDoctorId"))
                        .add(attribute("diagnosisCodes").value("diagnosisCodes"))
                        .add(attribute("payersName").value("payersName"))
                        .add(attribute("payersNric").value("payersNric"))
                        .add(attribute("patientsName").value("patientsName"))
                        .add(attribute("patientsNric").value("patientsNric"))
                        .add(attribute("expectedClaimAmount").value("expectedClaimAmount"))
                        .add(attribute("claimedAmount").value("claimedAmount"))
                        .add(attribute("claimRefNo").value("claimRefNo"))
                        .add(attribute("claimStatus").value("claimStatus"))
                        .add(attribute("claimRemarks").value("claimRemarks"))
                        .add(attribute("billReceiptId").value("billReceiptId"))
                        .add(attribute("documentName").value("documentName"))
                        .add(attribute("totalAmount").value("totalAmount"))
                )
                .add(mappedClass(Claim.class).add(global().targetClasses(Claim.class)));

        this.coreToApiMapper = new JMapper<>(ClaimViewEntity.class, ClaimViewCore.class, coreToApiMapperConfiguration);

        JMapperAPI apiToCoreMapperConfiguration = new JMapperAPI()
                .add(mappedClass(ClaimViewCore.class)
                        .add(attribute("claim").value("claim"))
                        .add(attribute("billDate").value("billDate"))
                        .add(attribute("clinicId").value("clinicId"))
                        .add(attribute("clinicHeCode").value("clinicHeCode"))
                        .add(attribute("hospitalCode").value("hospitalCode"))
                        .add(attribute("claimDoctorId").value("claimDoctorId"))
                        .add(attribute("diagnosisCodes").value("diagnosisCodes"))
                        .add(attribute("payersName").value("payersName"))
                        .add(attribute("payersNric").value("payersNric"))
                        .add(attribute("patientsName").value("patientsName"))
                        .add(attribute("patientsNric").value("patientsNric"))
                        .add(attribute("expectedClaimAmount").value("expectedClaimAmount"))
                        .add(attribute("claimedAmount").value("claimedAmount"))
                        .add(attribute("claimRefNo").value("claimRefNo"))
                        .add(attribute("claimStatus").value("claimStatus"))
                        .add(attribute("claimRemarks").value("claimRemarks"))
                        .add(attribute("billReceiptId").value("billReceiptId"))
                        .add(attribute("documentName").value("documentName"))
                        .add(attribute("totalAmount").value("totalAmount"))
                );


        this.apiToCoreMapper = new JMapper<>(ClaimViewCore.class, ClaimViewEntity.class, apiToCoreMapperConfiguration);
    }

    public ClaimViewEntity mapToApiEntity(ClaimViewCore claim) {
        logger.debug("Mapping claim [{}] to ClaimViewEntity", claim);
        return this.coreToApiMapper.getDestination(claim,MappingType.ALL_FIELDS);
    }

    public ClaimViewCore mapToCore(ClaimViewEntity claimViewEntity) {
        logger.debug("Mapping ClaimViewEntity [{}] to Claim", claimViewEntity);
        return this.apiToCoreMapper.getDestination(claimViewEntity,MappingType.ALL_FIELDS);
    }
}
