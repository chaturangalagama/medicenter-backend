package com.ilt.cms.pm.business.service.claim;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.casem.*;
import com.ilt.cms.core.entity.claim.ClaimViewCore;
import com.ilt.cms.core.entity.claim.ClaimsBalance;
import com.ilt.cms.core.entity.consultation.Consultation;
import com.ilt.cms.core.entity.coverage.CapLimiter;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.diagnosis.Diagnosis;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.patient.PatientPayable;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.database.casem.CaseDatabaseService;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.repository.CustomMedicalCoverageRepository;
import com.ilt.cms.repository.spring.*;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import com.ilt.cms.repository.spring.system.SystemStoreRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.util.CommonUtils;
import com.lippo.commons.util.StatusCode;
//import com.lippo.connector.mhcp.MHCPManager;
//import com.lippo.connector.mhcp.MHCPResult;
//import com.lippo.connector.mhcp.entity.request.chas.CHASAnnualBalanceRequestEntity;
//import com.lippo.connector.mhcp.entity.request.chas.CHASClaimStatusQueryRequestEntity;
//import com.lippo.connector.mhcp.entity.request.chas.CHASClaimSubmissionRequestEntity;
//import com.lippo.connector.mhcp.entity.response.chas.CHASAnnualBalanceResponseEntity;
//import com.lippo.connector.mhcp.entity.response.chas.CHASClaimStatusQueryResponseEntity;
//import com.lippo.connector.mhcp.entity.response.chas.CHASClaimSubmissionResponseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.cms.CMSRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * <code>{@link DefaultClaimService}</code> -
 * The default implementation of the <code>{@link ClaimService}</code> interface.
 * </p>
 *
 * @author prabath.
 */
@Service
public class DefaultClaimService implements ClaimService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultClaimService.class);

    private static final String SHOW_ALL_CLAIMS = "ALL";

    private static final String APPROVED_MHCP_STATUS = "A";
    private static final String PAID_MHCP_STATUS = "P";
    private static final String REJECTED_MHCP_STATUS = "R";

    private static final String SUBMIT_OK = "OK";
    private static final String GROUP_DR_ID = "main-group-doctor-id";
    private static final String mhcpSourceId = "HEALTHWAY";

    private DiagnosisRepository diagnosisRepository;
    private CaseRepository caseRepository;
    private CustomMedicalCoverageRepository customMedicalCoverageRepository;
    private Map<String, PlanDetails> planDetailsMap;
    private ClinicRepository clinicRepository;
    private PatientRepository patientRepository;
    private ItemRepository itemRepository;

    private SystemStoreRepository systemStoreRepository;
    private ClinicDatabaseService clinicDatabaseService;

//    private MHCPManager mhcpManager;
    private DoctorRepository doctorRepository;

    private RunningNumberService runningNumberService;
    private PatientVisitRegistryRepository visitRegistryRepository;
    private CaseDatabaseService caseDatabaseService;
    private MedicalCoverageRepository medicalCoverageRepository;

    //todo : add 'orElseThrow' when fetching a value from DB
    //todo : when clinic is not found in DB throw error with code E2002


    public DefaultClaimService(DiagnosisRepository diagnosisRepository, CaseRepository caseRepository,
                               CustomMedicalCoverageRepository customMedicalCoverageRepository, ClinicRepository clinicRepository,
                               PatientRepository patientRepository, ItemRepository itemRepository, SystemStoreRepository systemStoreRepository,
                               ClinicDatabaseService clinicDatabaseService, //MHCPManager mhcpManager,
                               DoctorRepository doctorRepository,
                               RunningNumberService runningNumberService, PatientVisitRegistryRepository visitRegistryRepository,
                               CaseDatabaseService caseDatabaseService, MedicalCoverageRepository medicalCoverageRepository) throws Exception {

        this.diagnosisRepository = diagnosisRepository;
        this.caseRepository = caseRepository;
        this.customMedicalCoverageRepository = customMedicalCoverageRepository;
        this.clinicRepository = clinicRepository;
        this.patientRepository = patientRepository;
        this.itemRepository = itemRepository;
        this.systemStoreRepository = systemStoreRepository;
        this.clinicDatabaseService = clinicDatabaseService;
//        this.mhcpManager = mhcpManager;
        this.doctorRepository = doctorRepository;
        this.runningNumberService = runningNumberService;
        this.visitRegistryRepository = visitRegistryRepository;
        this.caseDatabaseService = caseDatabaseService;
        this.medicalCoverageRepository = medicalCoverageRepository;

        try (InputStream resourceAsStream = DefaultClaimService.class.getResourceAsStream("/configuration.json")) {
            planDetailsMap = new ObjectMapper().readValue(resourceAsStream, new TypeReference<Map<String, PlanDetails>>() {
            });
        } catch (Exception e) {
            logger.error("Error occurred while getting data from configuration.json.", e);
            throw e;
        }
    }

    @Override
    public SalesOrder populateClaims(SalesOrder salesOrder) throws CMSException {
        logger.info("Start populating claims for new credit invoices of SalesOrder [" + salesOrder + "]");
        List<SalesItem> salesItems = salesOrder.getPurchaseItems().stream()
                .peek(SalesItem::populateSoldPrice)
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));

        if (salesOrder.getInvoices() != null) {
            salesOrder.getInvoices().stream()
                    .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT
                            && invoice.getClaim() == null)
                    .forEach(claimableInvoiceWithoutClaim -> {
                        try {
                            logger.debug("Populating the claim for invoice of Plan [" + claimableInvoiceWithoutClaim.getPlanId() + "]");

                            final PatientVisitRegistry patientVisitRegistry = visitRegistryRepository
                                    .findById(claimableInvoiceWithoutClaim.getVisitId())
                                    .orElseThrow(() -> {
                                        logger.error("There is no visit for the visit Id [" + claimableInvoiceWithoutClaim.getVisitId() + "]");
                                        return new CMSException(StatusCode.E2000, "No visit found with given visitId");
                                    });
                            Consultation consultation = patientVisitRegistry.getMedicalReference().getConsultation();
                            Claim claim = new Claim();
                            claim.setClaimId(CommonUtils.idGenerator());
                            claim.setClaimRefNo(runningNumberService.generateClaimRefNumber());
                            claim.setClaimStatus(Claim.ClaimStatus.PENDING);
                            claim.setManuallyUpdated(checkIfManuallyUpdatingClaim(claimableInvoiceWithoutClaim.getPlanId()));
                            claim.setClaimExpectedAmt(claimableInvoiceWithoutClaim.getPayableAmount());
                            claim.setGstAmount(claimableInvoiceWithoutClaim.getTaxAmount());
                            claim.setAttendingDoctorId(consultation.getDoctorId());
                            // set payer details as patient details
                            Patient patient = patientRepository.findById(patientVisitRegistry.getPatientId())
                                    .orElseThrow(() -> {
                                        logger.error("There is no patient for the patient Id [" + patientVisitRegistry.getPatientId() + "]");
                                        return new CMSException(StatusCode.E2000, "No patient found with given patientId");
                                    });
                            claim.setPayersName(patient.getName());
                            claim.setPayersNric(patient.getUserId().getNumber());

                            Doctor consultationDoctor = doctorRepository.findById(consultation.getDoctorId())
                                    .orElseThrow(() -> {
                                        logger.error("No doctor record found for doctorId [" + consultation.getDoctorId() + "]");
                                        return new CMSException(StatusCode.E2000,
                                                "No doctor record found for doctorId");
                                    });

                            Clinic clinic = clinicDatabaseService.findOne(patientVisitRegistry.getClinicId())
                                    .orElseThrow(() -> {
                                        logger.error("No Clinic found for clinicId [" + patientVisitRegistry.getClinicId() + "]");
                                        return new RuntimeException(
                                                new CMSException(StatusCode.E2002, "No Clinic record found for clinicId"));
                                    });

                            Doctor anchorDoctor = anchorDoctor(clinic, consultationDoctor);
                            if (anchorDoctor == null) {
                                claim.setClaimDoctorId(consultation.getDoctorId());
                            } else {
                                claim.setClaimDoctorId(anchorDoctor.getId());
                            }

                            List<Diagnosis> diagnoses = new ArrayList<>();
                            diagnosisRepository.findAllById(patientVisitRegistry.getMedicalReference().getDiagnosisIds())
                                    .forEach(diagnoses::add);
                            for (Diagnosis diagnosis : diagnoses) {
                                claim.getDiagnosisCodes()
                                        .add(diagnosis.getIcd10Code().replace(".", ""));

                            }

                            final AtomicInteger consultationAmt = new AtomicInteger(0);
                            final AtomicInteger medicationAmt = new AtomicInteger(0);
                            final AtomicInteger medicalTestAmt = new AtomicInteger(0);
                            final AtomicInteger otherAmt = new AtomicInteger(0);
                            final AtomicBoolean isTheInvoiceAmountFull = new AtomicBoolean(false);

                            calculateClaimAmountBreakdown(salesItems, claimableInvoiceWithoutClaim, consultationAmt,
                                    medicationAmt, medicalTestAmt, otherAmt, isTheInvoiceAmountFull);

                            claim.setConsultationAmt(consultationAmt.get());
                            claim.setMedicationAmt(medicationAmt.get());
                            claim.setMedicalTestAmt(medicalTestAmt.get());
                            claim.setOtherAmt(otherAmt.get());


                            claimableInvoiceWithoutClaim.setClaim(claim);
                            claimableInvoiceWithoutClaim.setInvoiceType(Invoice.InvoiceType.CREDIT);
                        } catch (CMSException e) {
                            throw new RuntimeException(e);
                        }

                    });

            // recalculate the sold price for all purchased items
            salesOrder.getPurchaseItems().forEach(SalesItem::populateSoldPrice);
        } else {
            logger.info("No claims were populated as there were no invoices in the SalesOrder");
        }

        return salesOrder;
    }

    private void calculateClaimAmountBreakdown(List<SalesItem> salesItems, Invoice claimableInvoiceWithoutClaim, AtomicInteger consultationAmt, AtomicInteger medicationAmt, AtomicInteger medicalTestAmt, AtomicInteger otherAmt, AtomicBoolean isTheInvoiceAmountFull) {
        salesItems.stream()
                .filter(Objects::nonNull)
                .filter(salesItem -> (salesItem.getExcludedCoveragePlanIds().isEmpty())
                        || !salesItem.getExcludedCoveragePlanIds()
                        .contains(claimableInvoiceWithoutClaim.getPlanId()))
                .filter(salesItem -> !isTheInvoiceAmountFull.get())
                .forEach(salesItem -> {

                    int currentTotal = consultationAmt.get() + medicationAmt.get() + medicalTestAmt.get()
                            + otherAmt.get();
                    int payableAmount = claimableInvoiceWithoutClaim.getPayableAmount();

                    if (currentTotal < payableAmount) {

                        int effectiveItemPrice = salesItem.getSoldPrice(); // since sold price is already populated we can use this

                        int remainingAmount = currentTotal + effectiveItemPrice > payableAmount ?
                                (currentTotal + effectiveItemPrice) - payableAmount : 0;

                        if (remainingAmount == 0) {
                            salesItems.remove(salesItem);
                            logger.debug("Removing the item [" + salesItem.getItemRefId()
                                    + "] from the temp list as the values are added to claim");
                        } else {
                            salesItem.setSoldPrice(remainingAmount);
                        }
                        updateAmountInfo(consultationAmt, medicationAmt, medicalTestAmt, otherAmt,
                                effectiveItemPrice, salesItem.getItemType(), remainingAmount);
                    } else {
                        isTheInvoiceAmountFull.set(true);
                    }

                });
    }

    private void updateAmountInfo(AtomicInteger consultationAmt, AtomicInteger medicationAmt,
                                  AtomicInteger medicalTestAmt, AtomicInteger otherAmt,
                                  int soldPrice, Item.ItemType itemType, int remainingAmount) {
        if (itemType == Item.ItemType.CONSULTATION) {
            consultationAmt.addAndGet(soldPrice - remainingAmount);
        } else if (itemType == Item.ItemType.DRUG) {
            medicationAmt.addAndGet(soldPrice - remainingAmount);
        } else if (itemType == Item.ItemType.LABORATORY) {
            medicalTestAmt.addAndGet(soldPrice - remainingAmount);
        } else {
            otherAmt.addAndGet(soldPrice - remainingAmount);
        }
    }

    // only allow CHAS of MEDISAVE claims to be submitted in automated flow
    private boolean checkIfManuallyUpdatingClaim(String planId) {
        MedicalCoverage medicalCoverage = medicalCoverageRepository.findMedicalCoverageByPlanId(planId);
        return !(MedicalCoverage.CoverageType.MEDISAVE.equals(medicalCoverage.getType())
                || MedicalCoverage.CoverageType.CHAS.equals(medicalCoverage.getType()));
    }


    private Doctor anchorDoctor(Clinic clinic, Doctor consultationDoctor) {
        Doctor anchorDoctor = null;
        if (consultationDoctor.getDoctorGroup() != Doctor.DoctorGroup.ANCHOR) {
            List<Doctor> clinicDocList = doctorRepository.findListOfDoctorsById(clinic.getAttendingDoctorId());
            for (Doctor doctor : clinicDocList) {
                if (doctor.getDoctorGroup() == Doctor.DoctorGroup.ANCHOR) {
                    anchorDoctor = doctor;
                    break;
                }
            }
            if (anchorDoctor == null) {
                String doc = systemStoreRepository.findByKey(GROUP_DR_ID).getValues().get(0).toString();
                anchorDoctor = doctorRepository.findById(doc).orElse(null);
            }
        }
        return anchorDoctor;
    }

    @Override
    public ClaimsBalance checkBalanceForNric(String clinicId, String planId, String nric) throws CMSException {
        logger.info("Checking balance for NRIC[" + nric + "] plan[" + planId + "]");
        MedicalCoverage medicalCoverage = findMedicalCoverage(planId);
        CoveragePlan plan = medicalCoverage.findPlan(planId);
        PlanDetails planDetails = planDetailsMap.get(planId);
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> {
                    logger.error("No clinic found for clinicId [" + clinicId + "]");
                    return new CMSException(StatusCode.E2000, "No clinic found with given clinicId");
                });
        logger.debug("Plan Details [{}]", planDetails);

        if (planDetails == null) {
            logger.warn("No plan found for config setting default value [" + plan.getCapPerVisit().getLimit() + "]");
            return new ClaimsBalance(plan.getCapPerVisit().getLimit());
        }
        if (planDetails.acute) {
            Patient patient = patientRepository.findByUserId(nric);
            List<Case> casesByPatientId = caseRepository.findCasesByPatientId(patient.getId());

            boolean alreadyUsed = casesByPatientId.stream()
                    .map(Case::getSalesOrder)
                    .map(SalesOrder::getInvoices)
                    .flatMap(Collection::stream)
                    .anyMatch(invoice -> invoice.getPlanId().equals(planId));

            ClaimsBalance claimsBalance;
            if (!alreadyUsed) {
                claimsBalance = new ClaimsBalance(plan.getCapPerVisit().getLimit());
            } else {
                claimsBalance = new ClaimsBalance(StatusCode.E1010, -1);
            }
            logger.info("given plan is acute setting default amount of [" + claimsBalance.getAvailableBalance() + "]");
            return claimsBalance;
        }
        switch (medicalCoverage.getType()) {
//            case CHAS: {
//                CHASAnnualBalanceRequestEntity.BillType billTypeEnum;
//                if (planDetails.getBillType() != null && !planDetails.getBillType().isEmpty()) {
//                    billTypeEnum = CHASAnnualBalanceRequestEntity.BillType.valueOf(planDetails.getBillType());
//                } else {
//                    billTypeEnum = null;
//                }
//                CHASAnnualBalanceRequestEntity requestEntity = new CHASAnnualBalanceRequestEntity(nric, LocalDate.now().getYear(), billTypeEnum);
//                requestEntity.setUserId(clinic.getHeCode() + "_ADMIN");
//                requestEntity.setDomain(clinic.getDomain());
//
//                MHCPResult balanceResult = null;
//                try {
//                    balanceResult = mhcpManager.request(requestEntity);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw new CMSException(e);
//                }
//                CHASAnnualBalanceResponseEntity mhcpResponse = (CHASAnnualBalanceResponseEntity) balanceResult.getEntity();
//
//                if (balanceResult.getStatus() != com.lippo.connector.mhcp.util.StatusCode.S0000) {
//                    logger.error("Error connecting to MHCP server [" + balanceResult.getStatus() + "] [" + balanceResult + "]");
//                    int availableBalance = plan.getCapPerVisit().getLimit();
//                    logger.info("Returning default value from the plan [" + availableBalance + "]");
//                    return new ClaimsBalance(StatusCode.I5000, availableBalance);
//                }
//                BigDecimal subsidyBalance;
//                switch (planDetails.getTier()) {
//                    case "1":
//                        logger.info("using tier 1 balance");
//                        BigDecimal tier1SubsidyBalance = mhcpResponse.getResponse().getTier1SubsidyBalance();
//                        if (tier1SubsidyBalance != null) {
//                            subsidyBalance = tier1SubsidyBalance;
//                        } else {
//                            subsidyBalance = mhcpResponse.getResponse().getSubsidyBalance();
//                        }
//                        break;
//                    case "2":
//                        logger.info("using tier 2 balance");
//                        BigDecimal tier2SubsidyBalance = mhcpResponse.getResponse().getTier2SubsidyBalance();
//                        if (tier2SubsidyBalance != null) {
//                            subsidyBalance = tier2SubsidyBalance;
//                        } else {
//                            subsidyBalance = mhcpResponse.getResponse().getSubsidyBalance();
//                        }
//                        break;
//                    default:
//                        logger.info("using subsidy balance");
//                        subsidyBalance = mhcpResponse.getResponse().getSubsidyBalance();
//                }
//                CapLimiter capPerVisit = plan.getCapPerVisit();
//                long bal = (long) (subsidyBalance.doubleValue() * 100);
//                if (capPerVisit.getLimit() <= bal) {
//                    return new ClaimsBalance(capPerVisit.getLimit());
//                } else {
//                    int availableBalance = doubleToInt(subsidyBalance.doubleValue());
//                    if (availableBalance == 0) {
//                        return new ClaimsBalance(-1);
//                    } else {
//                        return new ClaimsBalance(availableBalance);
//                    }
//                }
//            }
            case MEDISAVE: {
                return null;
            }
            default: {
                throw new RuntimeException("Only CHAS and MEDISAVE is supported");
            }
        }
    }

    @Override
    public List<ClaimViewCore> listClaimsByClinic(String name, String medicalCoverageId, String patientNric,
                                                  String status, LocalDate startDate, LocalDate endDate, String clinicId) throws CMSException {
        validateAccessPermission(name, clinicId, name != null);
        Claim.ClaimStatus claimStatus = status.equalsIgnoreCase(SHOW_ALL_CLAIMS) ? null : Claim.ClaimStatus.valueOf(status);
        LocalDateTime searchEndDate = endDate.plusDays(1).atStartOfDay();
        LocalDateTime searchStartDate = startDate.atStartOfDay();

        List<String> clinicIdList = clinicId != null ? Arrays.asList(clinicId) : null;

        MedicalCoverage medicalCoverage = customMedicalCoverageRepository.findPlanByMedicalCoverageId(medicalCoverageId);
        List<String> planIds = medicalCoverage.getCoveragePlans().stream().map(CoveragePlan::getId).collect(Collectors.toList());
        List<Claim> claimList = findPaymentDetails(patientNric, planIds, clinicIdList, claimStatus, searchEndDate, searchStartDate);

        return createClaimViewCoreStream(claimStatus, medicalCoverageId, claimList, medicalCoverage)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClaimViewCore> listClaimsByClinicByType(String clinicId, String name, MedicalCoverage.CoverageType coverageType, String patientNric, String status, LocalDate startDate, LocalDate endDate) throws CMSException {
        validateAccessPermission(name, clinicId, clinicId != null && name != null);
        List<String> clinicIdList = clinicId != null ? Arrays.asList(clinicId) : null;

        return retrieveClaimViewCoreList(clinicIdList, coverageType, patientNric, status, startDate, endDate);
    }

    @Override
    public List<ClaimViewCore> listClaimsByTypeForClinicList(List<String> clinicIdList, String name,
                                                             MedicalCoverage.CoverageType coverageType, String patientNric, String status, LocalDate startDate, LocalDate endDate) throws CMSException {
        clinicIdList.forEach(clinicId -> {
            try {
                validateAccessPermission(name, clinicId, clinicId != null && name != null);
            } catch (CMSException e) {
                throw new RuntimeException(e);
            }
        });

        return retrieveClaimViewCoreList(clinicIdList, coverageType, patientNric, status, startDate, endDate);
    }

    private List<ClaimViewCore> retrieveClaimViewCoreList(List<String> clinicIdList, MedicalCoverage.CoverageType coverageType, String patientNric, String status, LocalDate startDate, LocalDate endDate) {
        Claim.ClaimStatus claimStatus = status.equalsIgnoreCase(SHOW_ALL_CLAIMS) ? null : Claim.ClaimStatus.valueOf(status);
        LocalDateTime searchEndDate = endDate.plusDays(1).atStartOfDay();
        LocalDateTime searchStartDate = startDate.atStartOfDay();

        Map<String, MedicalCoverage> medicalCoverageMap = customMedicalCoverageRepository.getMedicalCoverageRepository().findByType(coverageType)
                .stream()
                .collect(Collectors.toMap(PersistedObject::getId, medicalCoverage -> medicalCoverage));


        return medicalCoverageMap.keySet().stream()
                .map(medicalCoverageId -> {
                    List<Claim> claimList;
                    List<String> planIds = medicalCoverageMap.get(medicalCoverageId).getCoveragePlans()
                            .stream().map(CoveragePlan::getId).collect(Collectors.toList());
                    claimList = findPaymentDetails(patientNric, planIds,
                            clinicIdList, claimStatus, searchEndDate, searchStartDate);
                    return createClaimViewCoreStream(claimStatus, medicalCoverageId, claimList, medicalCoverageMap.get(medicalCoverageId));
                })
                .flatMap(Function.identity())
                .collect(Collectors.toList());
    }

    private void validateAccessPermission(String name, String clinicId, boolean shouldCheck) throws CMSException {
        if (shouldCheck) {
            Clinic clinic = clinicRepository.findById(clinicId).orElseThrow(() -> {
                logger.error("Clinic not found for clinicId [" + clinicId + "]");
                return new CMSException(StatusCode.E2000, "No clinic found with given clinicId");
            });
            if (clinic.getClinicStaffUsernames().stream().noneMatch(s -> s.equals(name))) {
                logger.error("Given staff [" + name + "] has no access to clinic [" + clinicId + "]");
                throw new CMSException(StatusCode.E1005, "Staff [" + name + "] does not have access to clinic records");
            }
        }
    }

    @Override
    public ClaimViewCore submitClaim(String claimId, ClaimViewCore claimRequest) throws CMSException {
//        return submitClaim(claimId, claimRequest, claimRequest != null);
        return null;
    }

    @Override
    public ClaimViewCore saveClaim(String claimId, ClaimViewCore claimRequest) throws CMSException {
        logger.info("Updating claim request [" + claimRequest + "] for claimId [" + claimId + "]");

        Case caseForClaim = retrieveCaseForClaimByClaimId(claimId);

        Invoice claimableInvoice = getClaimableInvoiceForClaim(claimId, caseForClaim);
        Claim claim = claimableInvoice.getClaim();

        // for manual claims there is no restriction on the claim status other than REJECTED_PERMANENT
        if ((claim.isManuallyUpdated() && claim.getClaimStatus() == Claim.ClaimStatus.REJECTED_PERMANENT)
                || (!claim.isManuallyUpdated() && (claim.getClaimStatus() != Claim.ClaimStatus.PENDING
                && claim.getClaimStatus() != Claim.ClaimStatus.REJECTED && claim.getClaimStatus() != Claim.ClaimStatus.FAILED))) {
            throw new CMSException(StatusCode.E1011, "Only ["
                    + Claim.ClaimStatus.PENDING + "," + Claim.ClaimStatus.REJECTED + "," + Claim.ClaimStatus.FAILED + "] states can be updated");
        }
        // update invoice payed amount if the claim status is PAID or APPROVED
        Claim.ClaimStatus claimStatus = claimRequest.getClaimStatus();
        if (claim.isManuallyUpdated() && (claimStatus == Claim.ClaimStatus.PAID || claimStatus == Claim.ClaimStatus.APPROVED)) {
            claimableInvoice.setPaidAmount(claimRequest.getClaimedAmount());
        }
        updateClaimWithRequest(claimRequest, claim, false);
        return updateDBandExtractClaimViewCore(caseForClaim, claimableInvoice, claim);

    }

    private Case retrieveCaseForClaimByClaimId(String claimId) throws CMSException {
        return Optional.ofNullable(caseRepository.findCaseByClaimId(claimId))
                .orElseThrow(() -> {
                    logger.error("No Case found with given claim id [" + claimId + "]");
                    return new CMSException(StatusCode.E2002, "No Case found with the given claim id");
                });
    }

    // this should update both invoice and claim as needed
    private ClaimViewCore updateDBandExtractClaimViewCore(Case caseForClaim, Invoice claimableInvoice, Claim claim) {
        claimableInvoice.setClaim(claim);
        caseRepository.updateInvoiceBySalesOrderIdAndInvoiceNumber(caseForClaim.getSalesOrder().getId()
                , claimableInvoice.getInvoiceNumber(), claimableInvoice);
        caseRepository.save(caseForClaim);
        logger.info("Claim request updated [" + claim + "]");
        return createClaimViewCoreBuilder(claim, caseForClaim, claimableInvoice).build();
    }

    @Override
    public ClaimViewCore rejectClaim(String claimId, Claim.ClaimStatus rejectAs) throws CMSException {
        logger.info("finding claim for [" + claimId + "] to update as [" + rejectAs + "]");
        Case caseForClaim = retrieveCaseForClaimByClaimId(claimId);

        Invoice claimableInvoice = getClaimableInvoiceForClaim(claimId, caseForClaim);
        Clinic clinic = clinicRepository.findById(caseForClaim.getClinicId())
                .orElseThrow(() -> {
                    logger.error("No clinic found for clinicId [" + caseForClaim.getClinicId() + "]");
                    return new CMSException(StatusCode.E2002, "No clinic found with given clinicId");
                });

        Claim claim = claimableInvoice.getClaim();

        if (claim.getClaimStatus() != Claim.ClaimStatus.PENDING
                && claim.getClaimStatus() != Claim.ClaimStatus.REJECTED
                && claim.getClaimStatus() != Claim.ClaimStatus.FAILED) {
            throw new CMSException(StatusCode.E1011, "Only ["
                    + Claim.ClaimStatus.REJECTED + "," + Claim.ClaimStatus.PENDING + ","
                    + Claim.ClaimStatus.FAILED + "] states can be rejected permanently");

        }
        Patient patient = patientRepository.findById(caseForClaim.getPatientId()).get();
        if (claim.getClaimResult() != null) {
            patient.addPatientPayable(new PatientPayable(claimableInvoice.getInvoiceNumber(), claimableInvoice.getInvoiceNumber(),
                    claimableInvoice.getInvoiceTime(), claimableInvoice.getPayableAmount(),
                    claim.getClaimResult().getRemark(), false));

        } else {
            patient.addPatientPayable(new PatientPayable(claimableInvoice.getInvoiceNumber(), claimableInvoice.getInvoiceNumber(),
                    claimableInvoice.getInvoiceTime(), claimableInvoice.getPayableAmount(),
                    "Not submitted", false));

        }
        claim.setClaimStatus(rejectAs);
        caseRepository.updateClaimByInvoiceIdAndClaimId(claimableInvoice.getInvoiceNumber(), claim);
        patientRepository.save(patient);
        caseRepository.save(caseForClaim);
        logger.info("claim updated as  [" + rejectAs + "]");
        return createClaimViewCoreBuilder(claim, caseForClaim, claimableInvoice, patient, clinic).build();
    }

    @Override
    public List<ClaimViewCore> checkClaimStatus(String clinicId, List<String> claimIdList) throws CMSException {
//        List<CHASClaimStatusQueryRequestEntity.Record> statusCheckList = new ArrayList<>();
//        Clinic clinic = clinicRepository.findById(clinicId)
//                .orElseThrow(() -> {
//                    logger.error("No clinic found for clinicId [" + clinicId + "]");
//                    return new CMSException(StatusCode.E2002, "No clinic found with given clinicId");
//                });
//
//        for (String claimId : claimIdList) {
//            Case caseForClaim = caseRepository.findCaseByClaimId(claimId);
//            Invoice claimableInvoice = getClaimableInvoiceForClaim(claimId, caseForClaim);
//            Claim claim = claimableInvoice.getClaim();
//
//            Doctor doctor = doctorRepository.findById(claim.getClaimDoctorId())
//                    .orElseThrow(() -> new CMSException(StatusCode.E2000, "Doctor not found for the given Id"));
//
//            statusCheckList.add(new CHASClaimStatusQueryRequestEntity.Record(claim.getSubmissionResult().getClaimNo(),
//                    clinic.getHeCode(), clinic.getUen(), doctor.getMcr()));
//        }
//        CHASClaimStatusQueryRequestEntity queryRequestEntity =
//                new CHASClaimStatusQueryRequestEntity(CHASClaimStatusQueryRequestEntity.QueryType.CLAIM,
//                        null, mhcpSourceId, statusCheckList);
//        queryRequestEntity.setUserId(clinic.getHeCode() + "_ADMIN");
//        queryRequestEntity.setDomain(clinic.getDomain());
//
//        try {
//            MHCPResult result = mhcpManager.request(queryRequestEntity);
//            logger.info("Claim status check response [" + result + "]");
//            CHASClaimStatusQueryResponseEntity queryResponseEntity = (CHASClaimStatusQueryResponseEntity) result.getEntity();
//
//            return extractClaimViewFromCHASClaimStatusQueryResponseEntity(queryResponseEntity);
//        } catch (Exception e) {
//            logger.error("Error occurred while checking status with mhcp.", e);
//            throw new CMSException(StatusCode.I5000, "Error while checking claim status");
//        }
        return null;
    }

//    private List<ClaimViewCore> extractClaimViewFromCHASClaimStatusQueryResponseEntity(CHASClaimStatusQueryResponseEntity queryResponseEntity) {
//        return queryResponseEntity.getRecord().stream()
//                .map(record -> {
//                    CHASClaimStatusQueryResponseEntity.Record.RecordStatus recordStatus = record.getRecordStatus();
//                    try {
//                        return updateClaimResultStatus(recordStatus);
//                    } catch (CMSException e) {
//                        throw new CMSRuntimeException("Error occurred while updating", e);
//                    }
//                })
//                .map(claim -> {
//                    Case caseForClaim = caseRepository.findCaseByClaimId(claim.getClaimId());
//                    Invoice claimableInvoice = getClaimableInvoiceForClaim(claim.getClaimId(), caseForClaim);
//                    return createClaimViewCoreBuilder(claim, caseForClaim, claimableInvoice).build();
//                })
//                .collect(Collectors.toList());
//    }

    @Override
    public List<ClaimViewCore> checkClaimStatusForClinics(List<String> clinicIdList) throws CMSException {
//        List<CHASClaimStatusQueryRequestEntity.Record> statusCheckList = new ArrayList<>();
//        List<ClaimViewCore> claimViewCoreList = new ArrayList<>();
//        clinicIdList.forEach(clinicId -> {
//            try {
//                final Clinic clinic = clinicRepository.findById(clinicId)
//                        .orElseThrow(() -> {
//                            logger.error("No clinic found for clinicId [" + clinicId + "]");
//                            return new CMSException(StatusCode.E2002, "No clinic found with given clinicId");
//                        });
//                caseRepository.findByClinicId(clinicId).parallelStream()
//                        .filter(aCase -> (aCase.getSalesOrder() != null && aCase.getSalesOrder().getInvoices() != null
//                                && aCase.getSalesOrder().getInvoices().size() > 0))
//                        .flatMap(aCase -> aCase.getSalesOrder().getInvoices().stream())
//                        .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
//                        .forEach(claimableInvoice -> {
//                            Claim claim = claimableInvoice.getClaim();
//
//                            Doctor doctor;
//                            try {
//                                doctor = doctorRepository.findById(claim.getClaimDoctorId())
//                                        .orElseThrow(() -> new CMSException(StatusCode.E2000, "Doctor not found for the given Id"));
//                            } catch (CMSException e) {
//                                logger.error("Doctor not found for the given Id :[" + claim.getClaimDoctorId() + "]");
//                                throw new RuntimeException(e);
//                            }
//
//                            statusCheckList.add(new CHASClaimStatusQueryRequestEntity.Record(claim.getSubmissionResult().getClaimNo(),
//                                    clinic.getHeCode(), clinic.getUen(), doctor.getMcr()));
//                        });
//                CHASClaimStatusQueryRequestEntity queryRequestEntity =
//                        new CHASClaimStatusQueryRequestEntity(CHASClaimStatusQueryRequestEntity.QueryType.CLAIM,
//                                null, mhcpSourceId, statusCheckList);
//                queryRequestEntity.setUserId(clinic.getHeCode() + "_ADMIN");
//                queryRequestEntity.setDomain(clinic.getDomain());
//
//                try {
//                    MHCPResult result = mhcpManager.request(queryRequestEntity);
//                    logger.info("Claim status check response [" + result + "]");
//                    CHASClaimStatusQueryResponseEntity queryResponseEntity = (CHASClaimStatusQueryResponseEntity) result.getEntity();
//
//                    List<ClaimViewCore> resultsList = extractClaimViewFromCHASClaimStatusQueryResponseEntity(queryResponseEntity);
//                    claimViewCoreList.addAll(resultsList);
//                } catch (Exception e) {
//                    logger.error("Error occurred while checking status with mhcp.", e);
//                    throw new RuntimeException(new CMSException(StatusCode.I5000, "Error while checking claim status"));
//                }
//            } catch (CMSException e) {
//                logger.error("No clinic data found for the given clinic Id.[" + clinicId + "]");
//                throw new RuntimeException(e);
//            }
//
//        });
//        return claimViewCoreList;
        return null;
    }

    @Override
    public ClaimViewCore updateStatus(String claimId, Claim.ClaimStatus updateStatusTo, ClaimViewCore claimRequest) throws CMSException {
        logger.info("Updating status of  claim to [" + updateStatusTo + "] for claimId [" + claimId + "]");

        Case caseForClaim = caseRepository.findCaseByClaimId(claimId);
        Invoice claimableInvoice = getClaimableInvoiceForClaim(claimId, caseForClaim);

        Claim claim = claimableInvoice.getClaim();

        if (claim.getClaimStatus() == Claim.ClaimStatus.PENDING) {
            throw new CMSException(StatusCode.E1011, "Can not update the status of a claim in PENDING state");
        }
        // update invoice payed amount if the claim status is PAID or APPROVED
        if (updateStatusTo == Claim.ClaimStatus.PAID || updateStatusTo == Claim.ClaimStatus.APPROVED) {
            claimableInvoice.setPaidAmount(claimRequest.getClaimedAmount());
        }

        if (claimRequest.getClaimStatus() != null && !claim.getClaimStatus().equals(updateStatusTo)) {
            claim.setClaimStatus(updateStatusTo);
        }
        if (claimRequest.getClaimedAmount() > 0)
            claim.setClaimedAmount(claimRequest.getClaimedAmount());
        if (claimRequest.getClaimRefNo() != null && !claimRequest.getClaimRefNo().isEmpty()
                && !claimRequest.getClaimRefNo().equalsIgnoreCase(claim.getClaimRefNo()))
            claim.setClaimRefNo(claimRequest.getClaimRefNo());
        if (claimRequest.getClaimRemarks() != null && !claimRequest.getClaimRemarks().equalsIgnoreCase(claim.getRemark()))
            claim.setRemark(claimRequest.getClaimRemarks());

        return updateDBandExtractClaimViewCore(caseForClaim, claimableInvoice, claim);
    }

//    private Claim updateClaimResultStatus(CHASClaimStatusQueryResponseEntity.Record.RecordStatus recordStatus) throws CMSException {
//        Case caseForClaim = caseRepository.findCaseByClaimNo(recordStatus.getRecordID());
//        Invoice claimableInvoice = getClaimableInvoiceByClaimSubmitNumber(recordStatus.getRecordID(), caseForClaim);
//        Claim claim = claimableInvoice.getClaim();
//        switch (recordStatus.getStatus()) {
//            case "AP":
//            case APPROVED_MHCP_STATUS: {
//
//                updateClaim(recordStatus, claim, Claim.ClaimStatus.APPROVED);
//                claim.setClaimStatus(Claim.ClaimStatus.APPROVED);
//                break;
//            }
//            case "PD":
//            case PAID_MHCP_STATUS: {
//                return processClaimPayment(recordStatus, caseForClaim, claimableInvoice, claim);
//            }
//            case "RJ":
//            case REJECTED_MHCP_STATUS: {
//                updateClaim(recordStatus, claim, Claim.ClaimStatus.REJECTED);
//                break;
//            }
//            case "AE": {
//                updateClaim(recordStatus, claim, Claim.ClaimStatus.APPEALED);
//                break;
//            }
//            //todo need to check if the following statuses need to be handled. Its not there in the claim at the moment
////            case "DF": {
////                updateClaim(recordStatus, claimableInvoice.getClaim(), Claim.ClaimStatus.DRAFT);
////                caseRepository.save(caseForClaim);
////                break;
////            }
////            case "OH": {
////                updateClaim(recordStatus, claimableInvoice.getClaim(), Claim.ClaimStatus.ON_HOLD);
////                caseRepository.save(caseForClaim);
////                break;
////            }
//            default: {
//                logger.debug("Ignoring all other status [" + recordStatus.getStatus() + "] [" + recordStatus + "]");
//                return claim;
//            }
//        }
//        caseRepository.updateClaimByInvoiceIdAndClaimId(claimableInvoice.getInvoiceNumber(), claim);
//        return claim;
//    }
//
//    private Claim processClaimPayment(CHASClaimStatusQueryResponseEntity.Record.RecordStatus recordStatus, Case caseForClaim, Invoice claimableInvoice, Claim claim) {
//        logger.info("Payment received for the claim [" + claim.getClaimId() + "]");
//        updateClaim(recordStatus, claim, Claim.ClaimStatus.PAID);
//        int claimedAmount = doubleToInt(recordStatus.getPaidAmount().doubleValue());
//        claim.setClaimedAmount(claimedAmount);
//
//        claimableInvoice.setPaidAmount(claimedAmount);
//        claimableInvoice.setInvoiceState(Invoice.InvoiceState.PAID);
//
//        if (claimedAmount < claimableInvoice.getPayableAmount()) {
//            logger.info("The received claim amount is less than the expected amount, Creating a separate direct invoice for this due amount");
//            // when the full expected amount was not paid for the claim create a new DIRECT invoice for current visit with due amount
//            Invoice dueClaimInvoice = new Invoice(runningNumberService.generateInvoiceNumber(),
//                    Invoice.InvoiceType.DIRECT, claimableInvoice.getPlanId());
//            dueClaimInvoice.setPayableAmount(Math.abs(claimableInvoice.getPayableAmount() - claimedAmount));
//            dueClaimInvoice.setVisitId(claimableInvoice.getVisitId());
//            caseRepository.addNewInvoiceToSalesOrderWithId(caseForClaim.getSalesOrder().getId(), dueClaimInvoice);
//            logger.info("Added the invoice [" + dueClaimInvoice.getInvoiceNumber() + "] created for the due amount of claim [" + claim.getClaimId() + "] to sales order");
//        }
//
//        caseRepository.updateInvoiceBySalesOrderIdAndInvoiceNumber(caseForClaim.getSalesOrder().getId()
//                , claimableInvoice.getInvoiceNumber(), claimableInvoice);
//        return claim;
//    }
//
//    private void updateClaim(CHASClaimStatusQueryResponseEntity.Record.RecordStatus recordStatus, Claim claim, Claim.ClaimStatus claimStatus) {
//        BigDecimal paidAmount = recordStatus.getPaidAmount();
//        if (paidAmount == null) {
//            paidAmount = new BigDecimal(0);
//        }
//        XMLGregorianCalendar paidDate = recordStatus.getPaidDate();
//        LocalDateTime resultDateTime = null;
//        if (paidDate != null) {
//            resultDateTime = xmlGregorianCalendarToDateTime(paidDate);
//        }
//        claim.setClaimResult(new Claim.ClaimResult(recordStatus.getRecordID(), resultDateTime, doubleToInt(paidAmount.doubleValue()),
//                recordStatus.getErrorCode(), recordStatus.getStatusDescription()));
//        claim.setClaimStatus(claimStatus);
//    }

    private LocalDateTime xmlGregorianCalendarToDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(xmlGregorianCalendar.toGregorianCalendar().getTimeInMillis()), ZoneId.systemDefault());
    }


//    private ClaimViewCore submitClaim(String claimId, ClaimViewCore claimRequest,
//                                      boolean updateClaimDetails) throws CMSException {
//
//        Case caseForClaim = retrieveCaseForClaimByClaimId(claimId);
//        Invoice claimableInvoice = getClaimableInvoiceForClaim(claimId, caseForClaim);
//
//        Claim claim = claimableInvoice.getClaim();
//        Clinic clinic = clinicRepository.findById(caseForClaim.getClinicId())
//                .orElseThrow(() -> {
//                    logger.error("No clinic found for clinicId [" + caseForClaim.getClinicId() + "]");
//                    return new CMSException(StatusCode.E2002, "No clinic found with given clinicId");
//                });
//
//        if (claim.getClaimStatus() == Claim.ClaimStatus.PENDING
//                || claim.getClaimStatus() == Claim.ClaimStatus.REJECTED
//                || claim.getClaimStatus() == Claim.ClaimStatus.FAILED) {
//            if (updateClaimDetails) {
//                updateClaimWithRequest(claimRequest, claim, true);
//            }
//            claim.setSubmissionDateTime(LocalDateTime.now());
//            claim.setManuallyUpdated(false);
//
//            String planId = claimableInvoice.getPlanId();
//            PlanDetails planDetails = planDetailsMap.get(planId);
//
//            CHASClaimSubmissionRequestEntity submitEntity;
//            try {
//                submitEntity = convertToCHASEntity(caseForClaim, claimableInvoice, claim, planDetails);
//            } catch (DatatypeConfigurationException e) {
//                throw new CMSException(e);
//            }
//            submitEntity.setUserId(clinic.getHeCode() + "_ADMIN");
//            submitEntity.setDomain(clinic.getDomain());
//            MHCPResult submitResponse;
//            try {
//                submitResponse = mhcpManager.request(submitEntity);
//            } catch (Exception e) {
//                throw new CMSException(e);
//            }
//            CHASClaimSubmissionResponseEntity responseEntity = (CHASClaimSubmissionResponseEntity) submitResponse.getEntity();
//
//            if (submitResponse.getStatus() == com.lippo.connector.mhcp.util.StatusCode.S0000) {
//                String claimNo = responseEntity.getClaimNo();
//                String statusCode = responseEntity.getClaimStatus().getStatusCode();
//                String statusDescription = responseEntity.getClaimStatus().getStatusDescription();
//                claim.setSubmissionResult(new Claim.SubmissionResult(claimNo, statusCode, statusDescription));
//                if (responseEntity.getClaimStatus().getStatusCode().equalsIgnoreCase(SUBMIT_OK)) {
//                    claim.setClaimStatus(Claim.ClaimStatus.SUBMITTED);
//                } else {
//                    claim.setClaimStatus(Claim.ClaimStatus.REJECTED);
//                }
//            } else {
//                String claimNo;
//                String statusCode;
//                String statusDescription;
//                if (responseEntity != null && responseEntity.getClaimStatus() != null) {
//                    claimNo = responseEntity.getClaimNo();
//                    statusCode = responseEntity.getClaimStatus().getStatusCode();
//                    statusDescription = responseEntity.getClaimStatus().getStatusDescription();
//                } else {
//                    claimNo = "-1";
//                    statusCode = submitResponse.getStatus().name();
//                    statusDescription = submitResponse.getStatus().description();
//                }
//                claim.setSubmissionResult(new Claim.SubmissionResult(claimNo, statusCode, statusDescription));
//                claim.setClaimStatus(Claim.ClaimStatus.FAILED);
//            }
//
//            caseRepository.updateClaimByInvoiceIdAndClaimId(claimableInvoice.getInvoiceNumber(), claim);
//            return createClaimViewCoreBuilder(claim, caseForClaim, claimableInvoice).build();
//        } else {
//            throw new CMSException(StatusCode.E1011, "Only ["
//                    + Claim.ClaimStatus.PENDING + "," + Claim.ClaimStatus.REJECTED + "," + Claim.ClaimStatus.FAILED + "] states can be claimed");
//        }
//    }

    private Invoice getClaimableInvoiceForClaim(String claimId, Case caseForClaim) {
        return caseForClaim.getSalesOrder().getInvoices()
                .stream().filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
                .filter(claimableInvoice -> claimableInvoice.getClaim() != null)
                .filter(invoice -> invoice.getClaim().getClaimId().equals(claimId))
                .findFirst().get();
    }

    private Invoice getClaimableInvoiceByClaimSubmitNumber(String claimNumber, Case caseForClaim) throws CMSException {
        return caseForClaim.getSalesOrder().getInvoices()
                .stream().filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
                .filter(claimableInvoice -> claimableInvoice.getClaim() != null)
                .filter(invoice -> invoice.getClaim().getSubmissionResult() != null)
                .filter(invoice -> claimNumber.equals(invoice.getClaim().getSubmissionResult().getClaimNo()))
                .findFirst().orElseThrow(() -> new CMSException(StatusCode.E2000, "submitted claim not found"));
    }

//    private CHASClaimSubmissionRequestEntity convertToCHASEntity(Case caseForClaim, Invoice claimableInvoice, Claim claim,
//                                                                 PlanDetails planDetails) throws DatatypeConfigurationException {
//
//        Patient patient = patientRepository.findById(caseForClaim.getPatientId()).get();
//        Doctor doctor = doctorRepository.findById(claim.getClaimDoctorId()).get();
//        Clinic clinic = clinicRepository.findById(caseForClaim.getClinicId()).get();
//
//        CHASClaimSubmissionRequestEntity submitEntity = new CHASClaimSubmissionRequestEntity();
//        CHASClaimSubmissionRequestEntity.Claim submitClaim = new CHASClaimSubmissionRequestEntity.Claim();
//        submitClaim.setReferenceNo(claim.getClaimId());
//        submitClaim.setCategory(CHASClaimSubmissionRequestEntity.Claim.Category.valueOf(planDetails.category));
//        if (planDetails.getTier().equals("1")) {
//            submitClaim.setTier(CHASClaimSubmissionRequestEntity.Claim.Tier.LESS_SEVERE);
//        } else {
//            submitClaim.setTier(CHASClaimSubmissionRequestEntity.Claim.Tier.MORE_SEVERE);
//        }
//        submitClaim.setPatientType(CHASClaimSubmissionRequestEntity.Claim.PatientType.valueOf(planDetails.patientType));
//        submitClaim.setPatientName(patient.getName());
//        submitClaim.setSelfDeclaration(CHASClaimSubmissionRequestEntity.Claim.SelfDeclaration.Y);
//        submitClaim.setPatientConsentForm("");
//        submitClaim.setGpNricNo(doctor.getNric());
//        submitClaim.setMcrNo(doctor.getMcr());
//        submitClaim.setHeCode(clinic.getHeCode());
//        submitClaim.setUEN(clinic.getUen());
//        submitClaim.setReceiptNo(claimableInvoice.getInvoiceNumber());
//
//        ZonedDateTime zdt = claimableInvoice.getInvoiceTime().atZone(ZoneId.systemDefault());
//        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
//        XMLGregorianCalendar calendar = datatypeFactory.newXMLGregorianCalendar(GregorianCalendar.from(zdt));
//        submitClaim.setVisitDate(calendar);
//        submitClaim.setVisitTime(calendar);
//
//        submitClaim.setPatientNricNo(patient.getUserId().getNumber());
//        CHASClaimSubmissionRequestEntity.Claim.Diagnoses diagnoses = new CHASClaimSubmissionRequestEntity.Claim.Diagnoses();
//
//        claim.getDiagnosisCodes()
//                .stream()
//                .map(diag -> {
//                    CHASClaimSubmissionRequestEntity.Claim.Diagnoses.Diagnosis diagnosis = new CHASClaimSubmissionRequestEntity.Claim.Diagnoses.Diagnosis();
//                    diagnosis.setDiagnosisCode(diag);
//                    return diagnosis;
//                })
//                .forEach(diagnosis -> diagnoses.getDiagnosis().add(diagnosis));
//        submitClaim.setDiagnoses(diagnoses);
////            submitClaim.setReferralToExternal("");
////            submitClaim.setReferralReason("");
//
//        submitClaim.setSubventionAmount(new BigDecimal(claim.getClaimExpectedAmt() / 100D).setScale(2, BigDecimal.ROUND_HALF_DOWN));
//        submitClaim.setTotalVisitAmount(new BigDecimal(claimableInvoice.getPayableAmount()).setScale(2, BigDecimal.ROUND_HALF_DOWN));
//        submitClaim.setConsultationAmount(new BigDecimal(claim.getConsultationAmt() / 100D).setScale(2, BigDecimal.ROUND_HALF_DOWN));
//        submitClaim.setPrescriptionAmount(new BigDecimal(claim.getMedicationAmt() / 100D).setScale(2, BigDecimal.ROUND_HALF_DOWN));
//        submitClaim.setInvestigationAmount(new BigDecimal(claim.getMedicalTestAmt() / 100D).setScale(2, BigDecimal.ROUND_HALF_DOWN));
//        submitClaim.setOthersAmount(new BigDecimal(claim.getOtherAmt() / 100D).setScale(2, BigDecimal.ROUND_HALF_DOWN));
//
////            submitClaim.setLimitRemarkcode("");
////            submitClaim.setClaimLimitRemark("");
////            submitClaim.setLimitJustifications(new LimitJustifications());
////            submitClaim.setClusterID("");
//        submitClaim.setSourceID(mhcpSourceId); //todo check the source id
////            submitClaim.setTestOrder("");
////            submitClaim.setScreeningType("");
////            submitClaim.setScreenTypeSubCode("");
////            submitClaim.setSFLClaim(new SFLClaim());
////            submitClaim.setFollowUpType(new FollowUpType());
////            submitClaim.setSFLConsultAMT(new BigDecimal("0"));
////        submitClaim.setPatientPayAmount(new BigDecimal(billPayment.getDirectPayments().getAmount()).setScale(2, BigDecimal.ROUND_HALF_DOWN));
//        submitClaim.setGSTAMT(new BigDecimal(claim.getGstAmount() / 100D).setScale(2, BigDecimal.ROUND_HALF_DOWN));
////            submitClaim.setScreenDate(new XMLGregorianCalendar());
////            submitClaim.setSFLReferenceNo("");
////            submitClaim.setTotalXMLNo(0);
//
//        submitEntity.setClaim(submitClaim);
//        return submitEntity;
//    }

    private boolean isValidField(String value, String existingValue) {
        return (value != null && !value.isEmpty() && !value.equals(existingValue));
    }

    private void updateClaimWithRequest(ClaimViewCore claimRequest, Claim claim, boolean isInSubmitFlow) {
        if (isValidField(claimRequest.getClaimDoctorId(), claim.getClaimDoctorId()))
            claim.setClaimDoctorId(claimRequest.getClaimDoctorId());

        if (isValidField(claimRequest.getPayersName(), claim.getPayersName()))
            claim.setPayersName(claimRequest.getPayersName());
        if (isValidField(claimRequest.getPayersNric(), claim.getPayersNric()))
            claim.setPayersNric(claimRequest.getPayersNric());

        if (claimRequest.getDiagnosisCodes() != null && claimRequest.getDiagnosisCodes().size() > 0)
            claim.setDiagnosisCodes(claimRequest.getDiagnosisCodes()
                    .stream()
                    .map(s -> s.replace(".", ""))
                    .collect(Collectors.toList()));

        if (!isInSubmitFlow && claimRequest.getExpectedClaimAmount() != claim.getClaimExpectedAmt())
            claim.setClaimExpectedAmt(claimRequest.getExpectedClaimAmount());

        if (!isInSubmitFlow
                && claimRequest.getClaimStatus() != null
                && !claim.getClaimStatus().equals(claimRequest.getClaimStatus())) {
            claim.setClaimStatus(claimRequest.getClaimStatus());
        }

        if (!isInSubmitFlow && claimRequest.getClaimedAmount() > 0 && claimRequest.getClaimedAmount() != claim.getClaimedAmount()) {
            claim.setClaimedAmount(claimRequest.getClaimedAmount());
        }
        if (isValidField(claimRequest.getClaimRefNo(), claim.getClaimRefNo()))
            claim.setClaimRefNo(claimRequest.getClaimRefNo());
        if (isValidField(claimRequest.getClaimRemarks(), claim.getRemark()))
            claim.setRemark(claimRequest.getClaimRemarks());
    }


    private int doubleToInt(double value) {
        return (int) Math.round((value * 100 / 100D) * 100);
    }

    private MedicalCoverage findMedicalCoverage(String planId) {
        MedicalCoverage coveragePlan = customMedicalCoverageRepository.findMedicalCoverageByPlan(planId);
        if (coveragePlan == null || coveragePlan.getStatus() != Status.ACTIVE) {
            String msg = "Plan Not available : [{}]";
            logger.error(msg, planId);
            throw new RuntimeException(msg);
        }
        return coveragePlan;
    }


    //todo refactor the repositories
    private List<Claim> findPaymentDetails(String patientNric, List<String> planIdList, List<String> clinicIdList, Claim.ClaimStatus claimStatus,
                                           LocalDateTime searchEndDate, LocalDateTime searchStartDate) {
        List<Claim> claimList;
        AtomicReference<String> patientId = new AtomicReference<>("");
        if (patientNric != null && !patientNric.isEmpty()) {
            Patient patient = patientRepository.findByUserId(patientNric);
            patientId.set(patient.getId());
        }
        if (claimStatus == null && clinicIdList != null && clinicIdList.size() == 1) {
            claimList = fetchClaimsInGivenPeriod(planIdList, clinicIdList, patientId.get(), searchEndDate, searchStartDate)
                    .collect(Collectors.toList());
        } else if (claimStatus == null) {
            claimList = caseRepository.findCasesByMedicalCoveragePlanIdsIn(planIdList,searchStartDate,searchEndDate)
                    .stream()
                    .filter(aCase -> (patientId.get() == null || patientId.get().isEmpty()) || patientId.get().equals(aCase.getPatientId()))
                    .filter(aCase -> aCase.getSalesOrder() != null)
                    .map(Case::getSalesOrder)
                    .filter(salesOrder -> salesOrder.getInvoices() != null)
                    .flatMap(salesOrder -> salesOrder.getInvoices().stream())
                    .filter(invoice -> invoice.getInvoiceType() != null && invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
                    .filter(invoice -> invoice.getInvoiceTime() != null
                            && invoice.getInvoiceTime().isAfter(searchStartDate)
                            && invoice.getInvoiceTime().isBefore(searchEndDate))
                    .filter(invoice -> invoice.getClaim() != null)
                    .map(Invoice::getClaim)
                    .collect(Collectors.toList());
        } else if (clinicIdList != null && clinicIdList.size() == 1) {
            claimList = fetchClaimsInGivenPeriod(planIdList, clinicIdList, patientId.get(), searchEndDate, searchStartDate)
                    .filter(claim -> claim.getClaimStatus().equals(claimStatus))
                    .collect(Collectors.toList());
        } else {
            claimList = caseRepository.findCasesByMedicalCoveragePlanIdsIn(planIdList,searchStartDate,searchEndDate)
                    .stream()
                    .filter(aCase -> {
                        if (clinicIdList != null && clinicIdList.size() > 1) {
                            return clinicIdList.contains(aCase.getClinicId());
                        }
                        return true;
                    })
                    .filter(aCase -> (patientId.get() == null || patientId.get().isEmpty()) || patientId.get().equals(aCase.getPatientId()))
                    .filter(aCase -> aCase.getSalesOrder() != null)
                    .map(Case::getSalesOrder)
                    .filter(salesOrder -> salesOrder.getInvoices() != null)
                    .map(SalesOrder::getInvoices).flatMap(Collection::stream)
                    .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
                    .filter(invoice -> invoice.getInvoiceTime() != null
                            && invoice.getInvoiceTime().isAfter(searchStartDate)
                            && invoice.getInvoiceTime().isBefore(searchEndDate))
                    .filter(invoice -> invoice.getClaim() != null)
                    .map(Invoice::getClaim)
                    .filter(claim -> claim.getClaimStatus().equals(claimStatus))
                    .collect(Collectors.toList());
        }
        return claimList;
    }

    private Stream<Claim> fetchClaimsInGivenPeriod(List<String> planIdList, List<String> clinicIdList, String patientId,
                                                   LocalDateTime searchEndDate, LocalDateTime searchStartDate) {
        return caseRepository.findCasesByClinicIdAndMedicalCoveragePlanIds(clinicIdList.get(0), planIdList,searchStartDate,searchEndDate)
                .stream()
                .filter(aCase -> (patientId == null || patientId.isEmpty()) || patientId.equals(aCase.getPatientId()))
                .map(Case::getSalesOrder)
                .filter(salesOrder -> salesOrder.getInvoices() != null)
                .map(SalesOrder::getInvoices).flatMap(Collection::stream)
                .filter(invoice -> invoice.getInvoiceType() != null && invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
                .filter(invoice -> invoice.getInvoiceTime() != null
                        && invoice.getInvoiceTime().isAfter(searchStartDate)
                        && invoice.getInvoiceTime().isBefore(searchEndDate))
                .filter(invoice -> invoice.getClaim() != null)
                .map(Invoice::getClaim);
    }

    private Stream<ClaimViewCore> createClaimViewCoreStream(Claim.ClaimStatus claimStatus, String medicalCoverageId,
                                                            List<Claim> claimList, MedicalCoverage medicalCoverage) {
        List<ClaimViewCore> responses = new ArrayList<>();
        claimList.stream()
                .filter(claim -> (claimStatus == null || claim.getClaimStatus() == null
                        || claim.getClaimStatus().equals(claimStatus)))
                .forEach(claim -> {
                    Case caseForClaim = caseRepository.findCaseByClaimId(claim.getClaimId());
                    Invoice claimableInvoice = caseForClaim.getSalesOrder().getInvoices().stream()
                            .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
                            .filter(invoice -> invoice.getClaim().getClaimId().equals(claim.getClaimId()))
                            .findFirst().get();
                    responses.add(createClaimViewCoreBuilder(claim, caseForClaim, claimableInvoice).build());
                });
        return responses.stream();
    }

    private ClaimViewCore.ClaimViewCoreBuilder createClaimViewCoreBuilder(Claim claim, Case caseForClaim, Invoice claimableInvoice) {
        Patient patient = patientRepository.findById(caseForClaim.getPatientId()).get();
        Clinic clinic = clinicRepository.findById(caseForClaim.getClinicId()).get();
        return createClaimViewCoreBuilder(claim, caseForClaim, claimableInvoice, patient, clinic);
    }

    private ClaimViewCore.ClaimViewCoreBuilder createClaimViewCoreBuilder(Claim claim, Case caseForClaim,
                                                                          Invoice claimableInvoice,
                                                                          Patient patient, Clinic clinic) {

        ClaimViewCore.ClaimViewCoreBuilder builder = ClaimViewCore.builder();
        builder
                .claim(claim)
                .billDate(claimableInvoice.getInvoiceTime())
                .clinicId(caseForClaim.getClinicId())
                .clinicHeCode(clinic.getHeCode())
                .hospitalCode("") // at the moment sending the hospital code as empty
                .claimDoctorId(claim.getClaimDoctorId())
                .diagnosisCodes(claim.getDiagnosisCodes())
                .payersName(useDefaultIfNotPresent(claim.getPayersName(), patient.getName())) // if the payer related details were not fetched from the db use those of patient
                .payersNric(useDefaultIfNotPresent(claim.getPayersNric(), patient.getUserId().getNumber()))
                .patientsName(patient.getName())
                .patientsNric(patient.getUserId().getNumber())
                .expectedClaimAmount(claim.getClaimExpectedAmt())
                .claimedAmount(claim.getClaimedAmount())
                .claimRefNo(claim.getClaimRefNo())
                .claimStatus(claim.getClaimStatus())
                .claimRemarks(claim.getRemark())
                .billReceiptId(claimableInvoice.getInvoiceNumber())
                .documentName("")
                .totalAmount(claimableInvoice.getPayableAmount());
        return builder;
    }

    private String useDefaultIfNotPresent(String valueToCheck, String defaultValue) {
        if (valueToCheck != null && !valueToCheck.isEmpty()) {
            return valueToCheck;
        } else {
            return defaultValue;
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PlanDetails {
        private String category;
        private String patientType;
        private String billType;
        private String tier;
        private boolean acute;
    }
}
