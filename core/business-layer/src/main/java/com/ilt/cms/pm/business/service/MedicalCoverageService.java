package com.ilt.cms.pm.business.service;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.coverage.MedicalServiceScheme;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.coverage.MedicalCoverageDatabaseService;
import com.ilt.cms.database.policyholder.PolicyHolderDatabaseService;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.CMSConstant;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalCoverageService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalCoverageService.class);

    private MedicalCoverageDatabaseService medicalCoverageDatabaseService;
    private PolicyHolderDatabaseService policyHolderDatabaseService;
    private ClinicDatabaseService clinicDatabaseService;
    private ItemService itemService;
    private PolicyHolderService policyHolderService;

    private int defaultSearchSize = 20;

    public MedicalCoverageService(MedicalCoverageDatabaseService medicalCoverageDatabaseService,
                                  PolicyHolderDatabaseService policyHolderDatabaseService, ClinicDatabaseService clinicDatabaseService,
                                  ItemService itemService, PolicyHolderService policyHolderService) {
        this.medicalCoverageDatabaseService = medicalCoverageDatabaseService;
        this.policyHolderDatabaseService = policyHolderDatabaseService;
        this.clinicDatabaseService = clinicDatabaseService;
        this.itemService = itemService;
        this.policyHolderService = policyHolderService;

    }

    public MedicalCoverage addANewCoverage(MedicalCoverage coverage) throws CMSException {
        logger.info("adding coverage [" + coverage + "]");
        coverage.prepareFieldsForPersistence();
        boolean nameExists = medicalCoverageDatabaseService.doesCoverageNameExists(coverage.getName());
        if (nameExists) {
            //return new HttpApiResponse(StatusCode.E1004, "Name already exists");
            throw new CMSException(StatusCode.E1004, "Name already exists");
        } else {
            if (coverage.areThePlansValid(clinicList())) {
                boolean codeExists = medicalCoverageDatabaseService
                        .findIfMedicalCoverageCodeExists(coverage.getCode());
                if (codeExists) {
                    logger.warn("Medical Coverage code [" + coverage.getCode() + "] already exists returning error");
                    //return new HttpApiResponse(StatusCode.E1007, "Code already exits");
                    throw new CMSException(StatusCode.E1007, "Code already exits");
                } else {
                    logger.info("Persisting a new Medical Coverage [" + coverage + "]");
                    MedicalCoverage medicalCoverage = medicalCoverageDatabaseService.save(coverage);
                    return medicalCoverage;
                }
            } else {
                throw new CMSException(StatusCode.E1005, "Plan details is not valid");
            }
        }
    }


    private boolean validateScheme(MedicalServiceScheme medicalServiceScheme) {
        return itemService.existsById(medicalServiceScheme.getMedicalServiceItemID());
    }

    public boolean removeCoverage(String medicalCoverageId) throws CMSException {
        boolean medicalServiceIdUsed = policyHolderDatabaseService.isMedicalCoverageUsed(medicalCoverageId);
        if (medicalServiceIdUsed) {
            throw new CMSException(StatusCode.E1006, "Medical Coverage ID is still used by patients");
        } else {
            return medicalCoverageDatabaseService.delete(medicalCoverageId);
        }
    }

    public MedicalCoverage addNewPlan(String medicalCoverageId, CoveragePlan coveragePlan) throws CMSException {

        MedicalCoverage medicalCoverage = medicalCoverageDatabaseService.findOne(medicalCoverageId);

        if (medicalCoverage == null) {
            throw new CMSException(StatusCode.E1006, "No record found for the given medical coverage id");
        } else {
            if (!clinicList().containsAll(coveragePlan.getExcludedClinics())) {
                logger.error("Clinic ID not valid [" + coveragePlan + "]");
                throw new CMSException(StatusCode.E1005, "Clinic ID not valid");
            } else if (!coveragePlan.arePlansValid()) {
                logger.error("Parameters not valid [" + coveragePlan + "]");
                throw new CMSException(StatusCode.E1005, "Parameters not valid");

            } else {
                boolean codeExists = medicalCoverageDatabaseService
                        .findIfMedicalPlanCodeExists(coveragePlan.getCode());
                if (codeExists) {
                    throw new CMSException(StatusCode.E1007, "Code already exits");
                } else {
                    boolean newPlanAdded = medicalCoverage.addNewPlan(coveragePlan);
                    if (!newPlanAdded) {
                        throw new CMSException(StatusCode.E1005, "Plan already assigned");
                    } else {
                        return medicalCoverageDatabaseService.save(medicalCoverage);
                    }
                }
            }
        }
    }

    //I do understand that this can be done with a query but this is just a easier workaround, will look into it later
    private List<String> clinicList() {
        return clinicDatabaseService.listAllByIds()
                .stream().map(Clinic::getId).collect(Collectors.toList());
    }

    public boolean removePlan(String medicalCoverageId, String planId) throws CMSException {
        boolean medicalPlanIdUsed = policyHolderDatabaseService.isPlanUsed(medicalCoverageId, planId);
        if (medicalPlanIdUsed) {
            throw new CMSException(StatusCode.E1006, "Medical Plan ID is still used by patients");
        } else {
            MedicalCoverage medicalCoverage = medicalCoverageDatabaseService.findOne(medicalCoverageId);
            medicalCoverage.removePlan(planId);
            medicalCoverageDatabaseService.save(medicalCoverage);
            return true;
        }
    }

    public MedicalCoverage modifyMedicalCoverage(String medicalCoverageId, MedicalCoverage medicalCoverage) throws CMSException {
        MedicalCoverage coverage = medicalCoverageDatabaseService.findOne(medicalCoverageId);
        if (coverage == null) {
            throw new CMSException(StatusCode.E1006, "Medical Coverage ID not valid");
        } else if (medicalCoverage.getCoveragePlans() != null && !medicalCoverage.getCoveragePlans().isEmpty()) {
            throw new CMSException(StatusCode.E1006, "Not allowed to update plans part of update command");
        }
        logger.debug("Updating with fields [{}]", medicalCoverage);
        coverage.updateFields(medicalCoverage);

        return medicalCoverageDatabaseService.save(coverage);
    }

    public MedicalCoverage updateMedicalPlan(String medicalCoverageId, String planId, CoveragePlan coveragePlan) throws CMSException {
        MedicalCoverage coverage = medicalCoverageDatabaseService.findOne(medicalCoverageId);
        if (coverage == null) {
            throw new CMSException(StatusCode.E1006, "Medical Coverage ID not valid");
        }
        Optional<CoveragePlan> existingPLan = coverage.getCoveragePlans().stream()
                .filter(coveragePlan1 -> coveragePlan1.getId().equals(planId)).findFirst();
        if (!existingPLan.isPresent()) {
            throw new CMSException(StatusCode.E1006, "Plan ID not valid");
        }
        existingPLan.get().updatePlanDetails(coveragePlan);
        return medicalCoverageDatabaseService.save(coverage);
    }

    public boolean removeSchemeFromPlan(String medicalCoverageId, String planId, String schemeId) throws CMSException {
        MedicalCoverage medicalCoverage = medicalCoverageDatabaseService.findOne(medicalCoverageId);
        if (medicalCoverage == null) {
            //return new HttpApiResponse(StatusCode.E1006, "Medical Coverage ID not valid");
            throw new CMSException(StatusCode.E1006, "Medical Coverage ID not valid");

        }
        Optional<CoveragePlan> planOptional = medicalCoverage.getCoveragePlans()
                .stream().filter(coveragePlan -> Objects.equals(coveragePlan.getId(), planId)).findFirst();
        if (!planOptional.isPresent()) {
            //return new HttpApiResponse(StatusCode.E1006, "Plan ID not valid");
            throw new CMSException(StatusCode.E1006, "Plan ID not valid");
        }
        medicalCoverageDatabaseService.save(medicalCoverage);
        return true;
    }

    public List<MedicalCoverage> searchCoverage(String searchValue, Boolean includePolicyHolderCount) {
        List<MedicalCoverage> medicalCoverages = medicalCoverageDatabaseService
                .findByIdOrNameLikeOrCodeLike(searchValue, searchValue,
                        PageRequest.of(0, defaultSearchSize, Sort.Direction.ASC, "name"));

        if (includePolicyHolderCount) {
            policyHolderService.populatePolicyHolderCounts(medicalCoverages);
        }
        return medicalCoverages;

    }

    public HashMap<String, Object> list(int page, int size, Boolean includePolicyHolderCount) {
        Page<MedicalCoverage> medicalCoverages = medicalCoverageDatabaseService
                .findAll(PageRequest.of(page, size, Sort.Direction.ASC, "name"));

        HashMap<String, Object> payload = new HashMap<>();
        List<MedicalCoverage> content = medicalCoverages.getContent();
        if (includePolicyHolderCount) {
            policyHolderService.populatePolicyHolderCounts(content);
        }
        payload.put(CMSConstant.PAYLOAD_KEY_CONTENT, content);
        payload.put(CMSConstant.PAYLOAD_KEY_TOTAL_ELEMENTS, medicalCoverages.getTotalElements());
        payload.put(CMSConstant.PAYLOAD_KEY_TOTAL_PAGES, medicalCoverages.getTotalPages());
        payload.put(CMSConstant.PAYLOAD_KEY_NUMBER, medicalCoverages.getNumber());
        return payload;
    }

    public CoveragePlan findCoveragePlan(String medicalCoverageId, String coveragePlanId) {
        return medicalCoverageDatabaseService.findPlan(medicalCoverageId, coveragePlanId);
    }

    public CoveragePlan findCoverageByPlan(String coveragePlanId) {
        return medicalCoverageDatabaseService.findPlan(coveragePlanId);
    }

    public List<MedicalCoverage> listAll(Boolean includePolicyHolderCount) {
        List<MedicalCoverage> medicalCoverages = medicalCoverageDatabaseService.findAll();
        if (includePolicyHolderCount) {
            policyHolderService.populatePolicyHolderCounts(medicalCoverages);
        }
        return medicalCoverages;
    }

    public List<MedicalCoverage> listByClinic(String clinicId) {
        List<MedicalCoverage> medicalCoverages = medicalCoverageDatabaseService
                .findByStatus(Status.ACTIVE);

        return medicalCoverages.stream()
                .filter(this::filterNoneCoporatePayAtClinic)
                .map(medicalCoverage -> mapPlanByClinics(medicalCoverage, clinicId))
                .filter(medicalCoverage -> medicalCoverage.getCoveragePlans().size() != 0)
                .collect(Collectors.toList());
    }

    private boolean filterNoneCoporatePayAtClinic(MedicalCoverage medicalCoverage) {
        return !medicalCoverage.getType().equals(MedicalCoverage.CoverageType.CORPORATE)
                || medicalCoverage.isPayAtClinic();
    }

    private MedicalCoverage mapPlanByClinics(MedicalCoverage medicalCoverage, String clinicId) {

        List<CoveragePlan> collect = medicalCoverage.getCoveragePlans()
                .stream()
                .filter(coveragePlan -> !coveragePlan.getExcludedClinics().contains(clinicId))
                .collect(Collectors.toList());

        medicalCoverage.setCoveragePlans(collect);
        return medicalCoverage;
    }

    public MedicalCoverage findPlanByMedicalCoverageId(String medicalCoverageId) {
        return medicalCoverageDatabaseService.findPlanByMedicalCoverageId(medicalCoverageId);
    }

    public boolean doesPlanExists(String insuranceId, String planId) {
        return medicalCoverageDatabaseService.doesPlanExists(insuranceId, planId);
    }

    public CoveragePlan findPlanByPlanId(String planId) throws CMSException {
        CoveragePlan activePlan = medicalCoverageDatabaseService.findPlan(planId);
        if (activePlan == null) {
            throw new CMSException(StatusCode.E2000, "Coverage not found of id " + planId);
        }
        return activePlan;
    }
}
