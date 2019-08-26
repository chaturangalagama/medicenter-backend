package com.ilt.cms.pm.business.service;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import com.ilt.cms.core.entity.coverage.RelationshipMapping;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.database.coverage.MedicalCoverageDatabaseService;
import com.ilt.cms.database.policyholder.PolicyHolderDatabaseService;
import com.lippo.cms.exception.PolicyHolderException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;


@Service
public class PolicyHolderService {

    private static final Logger logger = LoggerFactory.getLogger(PolicyHolderService.class);
    private PolicyHolderDatabaseService policyHolderDatabaseService;

    private MedicalCoverageDatabaseService medicalCoverageDatabaseService;

    public PolicyHolderService(PolicyHolderDatabaseService policyHolderDatabaseService, MedicalCoverageDatabaseService medicalCoverageDatabaseService) {
        this.policyHolderDatabaseService = policyHolderDatabaseService;
        this.medicalCoverageDatabaseService = medicalCoverageDatabaseService;
    }

    /**
     * Returns back the same list with the policy holder counts populated
     *
     * @param medicalCoverages
     * @return
     */
    public List<MedicalCoverage> populatePolicyHolderCounts(List<MedicalCoverage> medicalCoverages) {
        for (MedicalCoverage medicalCoverage : medicalCoverages) {
            switch (medicalCoverage.getType()) {

                case CORPORATE: {
                    int count = policyHolderDatabaseService.getCorporateDatabaseService()
                            .countAllByMedicalCoverageIdAndStatus(medicalCoverage.getId(), Status.ACTIVE);
                    medicalCoverage.setPolicyHolderCount(count);
                    break;
                }
                case INSURANCE: {
                    int count = policyHolderDatabaseService.getInsuranceDatabaseService()
                            .countAllByMedicalCoverageIdAndStatus(medicalCoverage.getId(), Status.ACTIVE);
                    medicalCoverage.setPolicyHolderCount(count);
                    break;
                }
                case MEDISAVE: {
                    int count = policyHolderDatabaseService.getMediSaveDatabaseService()
                            .countAllByMedicalCoverageIdAndStatus(medicalCoverage.getId(), Status.ACTIVE);
                    medicalCoverage.setPolicyHolderCount(count);
                    break;
                }
                case CHAS: {
                    int count = policyHolderDatabaseService.getChasDatabaseService()
                            .countAllByMedicalCoverageIdAndStatus(medicalCoverage.getId(), Status.ACTIVE);
                    medicalCoverage.setPolicyHolderCount(count);
                    break;
                }
                default: {
                    logger.error("This message should not be printed, if its been printed most likely a new " +
                            "coverage type has been added but the developer forgot to change this logic");
                }
            }
        }
        return medicalCoverages;
    }

    public PolicyHolder addPolicyHolderToCoverage(String coverageType, PolicyHolder policyHolder) throws PolicyHolderException {

        MedicalCoverage.CoverageType type = MedicalCoverage.CoverageType.valueOf(coverageType);

        logger.debug("Holder info [{}]", policyHolder);
        boolean planExists = medicalCoverageDatabaseService.doesPlanExists(policyHolder.getMedicalCoverageId(), policyHolder.getPlanId());
        if (!planExists) {
            logger.error("Medical Coverage or Plan does not exists");
            //return new HttpApiResponse(StatusCode.E1006, "Medical Coverage or Plan does not exists");
            throw new PolicyHolderException(StatusCode.E1006, "Medical Coverage or Plan does not exists");
        } else if (!policyHolder.parametersValid()) {
            logger.error("Policy holder information not valid");
            //return new HttpApiResponse(StatusCode.E1006, "Policy holder information not valid");
            throw new PolicyHolderException(StatusCode.E1006, "Policy holder information not valid");
        }

        PolicyHolder savedVersion = null;
        RelationshipMapping policyHolderRelationship = policyHolder.getRelationship();
        switch (type) {
            case CHAS: {
                logger.info("handling CHAS coverage type [" + policyHolder.getIdentificationNumber() + "]");

                if (policyHolderRelationship != null) {
                    logger.error("Relationship only supported corporates and private coverages [" + policyHolderRelationship + "]");
                    //return new HttpApiResponse(StatusCode.E1006, "Relationship only supported corporates and private coverages");
                    throw new PolicyHolderException(StatusCode.E1006, "Relationship only supported corporates and private coverages");

                }
                List<PolicyHolder.PolicyHolderChas> policyHolderChas = policyHolderDatabaseService.getChasDatabaseService()
                        .findByIdentificationNumber(policyHolder.getIdentificationNumber());

                if (policyHolderChas == null || policyHolderChas.size() == 0) {
                    savedVersion = policyHolderDatabaseService.getChasDatabaseService().save(policyHolder.copy(PolicyHolder.PolicyHolderChas.class));
                } else {
                    logger.info("User already exists, retuning error to API call");
                    //return new HttpApiResponse(StatusCode.E1006, "User already exists");
                    throw new PolicyHolderException(StatusCode.E1006, "User already exists");
                }
                break;
            }
            case MEDISAVE: {

                logger.info("handling MEDISAVE coverage type [" + policyHolder.getIdentificationNumber() + "]");

                if (policyHolderRelationship != null) {
                    logger.error("Relationship only supported corporates and private coverages [" + policyHolderRelationship + "]");
                    //return new HttpApiResponse(StatusCode.E1006, "Relationship only supported corporates and private coverages");
                    throw new PolicyHolderException(StatusCode.E1006, "Relationship only supported corporates and private coverages");

                }
                List<PolicyHolder.PolicyHolderMediSave> policyHolderMediSave = policyHolderDatabaseService.getMediSaveDatabaseService()
                        .findByIdentificationNumber(policyHolder.getIdentificationNumber());
                if (policyHolderMediSave == null || policyHolderMediSave.size() == 0) {
                    savedVersion = policyHolderDatabaseService.getMediSaveDatabaseService()
                            .save(policyHolder.copy(PolicyHolder.PolicyHolderMediSave.class));
                } else {
                    logger.info("User already exists, retuning error to API call");
                    //return new HttpApiResponse(StatusCode.E1006, "Policy holder already exists");
                    throw new PolicyHolderException(StatusCode.E1006, "Policy holder already exists");
                }
                break;
            }
            case CORPORATE: {
                logger.info("handling CORPORATE coverage type [" + policyHolder.getIdentificationNumber() + "]");

                boolean isValidateRelationship = validateRelationship(policyHolder,
                        () -> policyHolderDatabaseService.getCorporateDatabaseService()
                                .findIfUserHasCoverage(policyHolderRelationship.getHolderId(), policyHolderRelationship.getPlanId()));


                PolicyHolder.PolicyHolderCorporate policyHolderCorporate = policyHolderDatabaseService.getCorporateDatabaseService()
                        .findByIdentificationNumberAndRelationshipIsNull(policyHolder.getIdentificationNumber());


                if (policyHolderCorporate == null) {
                    savedVersion = policyHolderDatabaseService.getCorporateDatabaseService()
                            .save(policyHolder.copy(PolicyHolder.PolicyHolderCorporate.class));
                } else {
                    logger.info("User already exists, retuning error to API call");
                    //return new HttpApiResponse(StatusCode.E1006, "Policy holder already exists");
                    throw new PolicyHolderException(StatusCode.E1006, "Policy holder already exists");
                }
                break;
            }
            case INSURANCE: {
                logger.info("handling INSURANCE coverage type [" + policyHolder.getIdentificationNumber() + "]");

                boolean isValidateRelationship = validateRelationship(policyHolder, () -> policyHolderDatabaseService.getInsuranceDatabaseService()
                        .findIfUserHasCoverage(policyHolderRelationship.getHolderId(), policyHolderRelationship.getPlanId()));


                boolean insuranceExists = policyHolderDatabaseService.getInsuranceDatabaseService()
                        .checkIfUserHasCoverage(policyHolder.getIdentificationNumber(), policyHolder.getMedicalCoverageId(), policyHolder.getPlanId());
                if (!insuranceExists) {
                    savedVersion = policyHolderDatabaseService.getInsuranceDatabaseService().save(policyHolder.copy(PolicyHolder.PolicyHolderInsurance.class));
                } else {
                    //return new HttpApiResponse(StatusCode.E1006, "Policy holder already exists");
                    throw new PolicyHolderException(StatusCode.E1006, "Policy holder already exists");
                }
                break;
            }
        }
        return savedVersion;
    }

    private boolean validateRelationship(PolicyHolder policyHolder, Supplier<PolicyHolder> holderSupplier) throws PolicyHolderException {
        if (policyHolder.getRelationship() != null) {
            PolicyHolder parentHolder = holderSupplier.get();

            if (parentHolder == null) {
                String message = "Main holder details not found [" + policyHolder.getRelationship().getHolderId() + "]";
                logger.error(message);
                //return new HttpApiResponse(StatusCode.E1006, message);
                throw new PolicyHolderException(StatusCode.E1006, message);
            } else {
                CoveragePlan coveragePlan = medicalCoverageDatabaseService.findPlan(parentHolder.getMedicalCoverageId(), parentHolder.getPlanId());
                boolean allowedRelationship = coveragePlan.getAllowedRelationship().stream()
                        .anyMatch(relationship -> relationship == policyHolder.getRelationship().getRelationship());
                if (!allowedRelationship) {
                    logger.error("Policy holder [" + parentHolder + "] not allowed to have the given depended : [" + policyHolder.getRelationship() + "]");
                    //return new HttpApiResponse(StatusCode.E1006, "Main Holder coverage does not allow the given relationship coverage");
                    throw new PolicyHolderException(StatusCode.E1006, "Main Holder coverage does not allow the given relationship coverage");
                }
            }

        }
        return true;
    }

    public boolean removePolicyHolderToCoverage(String holderId, String coverageType, String medicalCoverageId,
                                                String planId) {

        MedicalCoverage.CoverageType type = MedicalCoverage.CoverageType.valueOf(coverageType);
        logger.info("removing policy holder [" + holderId + "] form coverage [" + type
                + "] medicalCoverageId[" + medicalCoverageId + "] planId[" + planId + "]");
        switch (type) {
            case INSURANCE: {
                policyHolderDatabaseService.getInsuranceDatabaseService()
                        .deleteByIdAndMedicalCoverageIdAndPlanId(holderId, medicalCoverageId, planId);
                break;
            }
            case CORPORATE: {
                policyHolderDatabaseService.getCorporateDatabaseService().delete(holderId);
                break;
            }
            case MEDISAVE: {
                policyHolderDatabaseService.getMediSaveDatabaseService().delete(holderId);
                break;
            }
            case CHAS: {
                policyHolderDatabaseService.getChasDatabaseService().delete(holderId);
                break;
            }
        }
        logger.info("Removal successful");
        return true;
    }

    public PolicyHolder searchPolicyHolder(String holderId, String coverageType) throws PolicyHolderException {

        Optional<PolicyHolder> policyHolder = searchPolicyHolderById(holderId, coverageType);
        if (policyHolder.isPresent()) {
            logger.info("policy holder found [" + policyHolder.get() + "]");
            return policyHolder.orElse(null);
        } else {
            //return new HttpApiResponse(StatusCode.E2000, "Policy holder not found");
            throw new PolicyHolderException(StatusCode.E2000, "Policy holder not found");
        }
    }

    public Optional<PolicyHolder> searchPolicyHolderById(String holderId, String coverageType) {
        MedicalCoverage.CoverageType type = MedicalCoverage.CoverageType.valueOf(coverageType);
        logger.info("finding policy holder [" + holderId + "] type[" + type + "]");
        Optional<PolicyHolder> policyHolder = null;
        switch (type) {
            case INSURANCE: {
                policyHolder = Optional.ofNullable(policyHolderDatabaseService.getInsuranceDatabaseService()
                        .findOne(holderId));
                break;
            }
            case CORPORATE: {
                policyHolder = Optional.ofNullable(policyHolderDatabaseService.getCorporateDatabaseService().findOne(holderId));
                break;
            }
            case MEDISAVE: {
                policyHolder = Optional.ofNullable(policyHolderDatabaseService.getMediSaveDatabaseService().findOne(holderId));
                break;
            }
            case CHAS: {
                policyHolder = Optional.ofNullable(policyHolderDatabaseService.getChasDatabaseService().findOne(holderId));
                break;
            }
        }
        return policyHolder;
    }


    public Map<MedicalCoverage.CoverageType, List<? extends PolicyHolder>> searchPolicyHolder(UserId userId) throws PolicyHolderException {
        logger.info("finding policy holder by userid [" + userId + "]");
        boolean validateParameters = userId.areParametersValid();
        if (!validateParameters) {
            //return new HttpApiResponse(StatusCode.E1006, "Given values are not valid");
            throw new PolicyHolderException(StatusCode.E1006, "Given values are not valid");
        }
        List<PolicyHolder.PolicyHolderInsurance> insurances = policyHolderDatabaseService.getInsuranceDatabaseService()
                .findAllByIdentificationNumberAndStatus(userId, Status.ACTIVE);

        LocalDate localDate = LocalDate.now();
        List<PolicyHolder.PolicyHolderCorporate> corporate = policyHolderDatabaseService.getCorporateDatabaseService()
                .findActivePolicyHolder(userId, Status.ACTIVE, localDate, localDate);

        List<PolicyHolder.PolicyHolderMediSave> mediSave = policyHolderDatabaseService.getMediSaveDatabaseService()
                .findAllByIdentificationNumberAndStatus(userId, Status.ACTIVE);

        List<PolicyHolder.PolicyHolderChas> chas = policyHolderDatabaseService.getChasDatabaseService()
                .findAllByIdentificationNumberAndStatus(userId, Status.ACTIVE);

        Map<MedicalCoverage.CoverageType, List<? extends PolicyHolder>> payload = new HashMap<>();


        //no need to do null check because spring is configured to not to include null values on rest messages
        payload.put(MedicalCoverage.CoverageType.INSURANCE,
                insurances);

//        payload.put(MedicalCoverage.CoverageType.INSURANCE,
//                insurances.stream().map(this::findCoveragePlan).collect(Collectors.toList()));

        payload.put(MedicalCoverage.CoverageType.CORPORATE,
                corporate);


        if (mediSave != null) {
            payload.put(MedicalCoverage.CoverageType.MEDISAVE,
                    mediSave);
        }
        if (chas != null) {
            payload.put(MedicalCoverage.CoverageType.CHAS,
                    chas);

        }
        return payload;
    }

    public MedicalCoverage findCoveragePlan(PolicyHolder policyHolder) {
        return medicalCoverageDatabaseService.findPlanByMedicalCoverageId(policyHolder.getMedicalCoverageId());
        //return new PolicyHolderPayload(policyHolder, medicalCoverage.getName(), medicalCoverage.getAddress(), medicalCoverage.findPlan(policyHolder.getPlanId()), medicalCoverage.getContacts());
    }

    public boolean areCoveragesValid(UserId userId, List<AttachedMedicalCoverage> medicalCoverages) {
        return medicalCoverages.stream()
                .allMatch(attachedMedicalCoverages -> {
                    String planId = attachedMedicalCoverages.getPlanId();
                    List<MedicalCoverage> coverages = medicalCoverageDatabaseService.findMedicalCoveragesByPlanId(planId, Status.ACTIVE);
                    if (planId.equals("0")) {
                        return true;
                    }
                    boolean isValid = false;
                    outer:
                    for (MedicalCoverage coverage : coverages) {
                        if (policyHolderDatabaseService.isUserAssociatedWithPlan(userId, coverage.getId(), planId)) {
                            isValid = true;
                            break outer;
                        }
                    }
                    return isValid;
                });
    }

}
