package com.ilt.cms.pm.business.service;

import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeRequest;
import com.ilt.cms.core.entity.casem.*;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.consultation.Consultation;
import com.ilt.cms.core.entity.consultation.MedicalCertificate;
import com.ilt.cms.core.entity.consultation.PatientReferral;
import com.ilt.cms.core.entity.doctor.Doctor;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.item.SellingPrice;
import com.ilt.cms.core.entity.medical.DispatchItem;
import com.ilt.cms.core.entity.medical.MedicalReference;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.ilt.cms.core.entity.visit.ConsultationFollowup;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.core.entity.visit.Priority;
import com.ilt.cms.database.RunningNumberService;
import com.ilt.cms.database.casem.CaseDatabaseService;
import com.ilt.cms.database.clinic.ClinicDatabaseService;
import com.ilt.cms.database.doctor.DoctorDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.ilt.cms.database.visit.PatientVisitRegistryDatabaseService;
import com.ilt.cms.pm.business.service.billing.NewCaseService;
import com.ilt.cms.pm.business.service.queue.QueueService;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.util.UserInfoHelper;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
public class PatientVisitService {

    private static final Logger logger = LoggerFactory.getLogger(PatientVisitService.class);
    private final int EDITABLE_HOURS = 24;
    private PatientVisitRegistryDatabaseService patientVisitRegistryDatabaseService;
    private PatientDatabaseService patientDatabaseService;
    private CaseDatabaseService caseDatabaseService;
    private ClinicDatabaseService clinicDatabaseService;
    private DoctorDatabaseService doctorDatabaseService;
    private ConsultationService consultationService;
    private ConsultationFollowupService consultationFollowupService;
    private PatientReferralService patientReferralService;
    private ItemService itemService;
    private NewCaseService caseService;
    private RunningNumberService runningNumberService;
    private DiagnosisService diagnosisService;
    private MedicalCoverageService medicalCoverageService;
    private PolicyHolderService policyHolderService;
    private QueueService queueService;
    private PriceCalculationService priceCalculationService;


    public PatientVisitService(PatientVisitRegistryDatabaseService patientVisitRegistryDatabaseService,
                               PatientDatabaseService patientDatabaseService, CaseDatabaseService caseDatabaseService,
                               ClinicDatabaseService clinicDatabaseService, DoctorDatabaseService doctorDatabaseService,
                               ConsultationService consultationService, ConsultationFollowupService consultationFollowupService,
                               PatientReferralService patientReferralService, ItemService itemService, NewCaseService caseService,
                               RunningNumberService runningNumberService, DiagnosisService diagnosisService,
                               MedicalCoverageService medicalCoverageService, PolicyHolderService policyHolderService,
                               QueueService queueService, PriceCalculationService priceCalculationService) {
        this.patientVisitRegistryDatabaseService = patientVisitRegistryDatabaseService;
        this.patientDatabaseService = patientDatabaseService;
        this.caseDatabaseService = caseDatabaseService;
        this.clinicDatabaseService = clinicDatabaseService;
        this.doctorDatabaseService = doctorDatabaseService;
        this.consultationService = consultationService;
        this.consultationFollowupService = consultationFollowupService;
        this.patientReferralService = patientReferralService;
        this.itemService = itemService;
        this.caseService = caseService;
        this.runningNumberService = runningNumberService;
        this.diagnosisService = diagnosisService;
        this.medicalCoverageService = medicalCoverageService;
        this.policyHolderService = policyHolderService;
        this.queueService = queueService;
        this.priceCalculationService = priceCalculationService;
    }

    public PatientVisitRegistry searchById(String visitId) throws CMSException {
        PatientVisitRegistry registry = patientVisitRegistryDatabaseService.searchById(visitId);
        if (registry == null) {
            logger.debug("PatientVisitRegistry not found for [id]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000, "Records not found for id " + visitId);
        } else {
            logger.debug("PatientVisitRegistry found for [id]:[{}] in the system", registry.getId());
            return registry;
        }
    }

    public List<PatientVisitRegistry> searchByIds(List<String> ids) {
        List<PatientVisitRegistry> visits = patientVisitRegistryDatabaseService.searchByIds(ids);
        logger.debug("PatientVisitRegistry [{}] found for [ids]:[{}] in the system", visits.size(), ids);
        return visits;
    }

    public PatientVisitRegistry searchByVisitNumber(String visitNumber) throws CMSException {
        PatientVisitRegistry registry = patientVisitRegistryDatabaseService.searchByVisitNumber(visitNumber);
        if (registry == null) {
            logger.debug("PatientVisitRegistry not found for [visitNumber]:[{}]", visitNumber);
            throw new CMSException(StatusCode.E2000, "Records not found for visit number " + visitNumber);
        } else {
            logger.debug("PatientVisitRegistry found for [visitNumber]:[{}] in the system", registry.getVisitNumber());
            return registry;
        }
    }

    public List<PatientVisitRegistry> listVisits(String patientId) {
        List<PatientVisitRegistry> visitRegistries = patientVisitRegistryDatabaseService.listPatientVisits(patientId);
        logger.debug("found [{}] PatientVisitRegistries in the system", visitRegistries.size());
        return visitRegistries;
    }

    public Page<PatientVisitRegistry> listVisits(String patientId, int page, int size) {

        Page<PatientVisitRegistry> visits = patientVisitRegistryDatabaseService.listPatientVisits(patientId, page, size, new Sort(Sort.Direction.DESC, "startTime"));
        if (visits != null) {
            logger.debug("found [{}] PatientVisitRegistries in the system", visits.getContent().size());
            return visits;
        } else {
            logger.debug("No PatientVisitRegistry found");
            return Page.empty();
        }
    }

    public PatientVisitRegistry updatePatientVisitRegistry(String visitId, PatientVisitRegistry visitRegistry) throws CMSException {
        PatientVisitRegistry currentRegistry = patientVisitRegistryDatabaseService.searchById(visitId);
        if (currentRegistry == null) {
            logger.debug("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }
        if (!PatientVisitRegistry.PatientVisitState.INITIAL.equals(currentRegistry.getVisitStatus())) {
            logger.debug("Patient visit [visitId]:[{}] not allowed to update", visitId);
            throw new CMSException(StatusCode.E1009);
        }
        checkPatientVisitRegistryValidity(visitRegistry);
        currentRegistry.copy(visitRegistry);
        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentRegistry);
        logger.debug("PatientVisitRegistry updated [visitId]:[{}]", savedRegistry.getVisitNumber());
        return savedRegistry;
    }

    public void detachedVisitFromCase(String visitId, String caseId) throws CMSException {
        if (!caseDatabaseService.exists(caseId)) {
            logger.debug("Referred case cannot find for [caseId]:[{}]", caseId);
            throw new CMSException(StatusCode.E1002, "Case Id not valid");
        }
        if (!patientVisitRegistryDatabaseService.exists(visitId)) {
            throw new CMSException(StatusCode.E2000, "visit not found for given visitId");
        }
        Case aCase = caseDatabaseService.findByCaseId(caseId);
        PatientVisitRegistry visitRegistry = patientVisitRegistryDatabaseService.searchById(visitId);
        if (!aCase.getVisitIds().contains(visitRegistry.getId())) {
            throw new CMSException(StatusCode.E1002, "Visit Number not valid");
        }

        List<String> visitIds = aCase.getVisitIds();
        visitIds.remove(visitRegistry.getId());
        aCase.setVisitIds(visitIds);
        caseDatabaseService.save(aCase);

        visitRegistry.setAttachedToCase(false);
        visitRegistry.setCaseId(null);
        patientVisitRegistryDatabaseService.save(visitRegistry);
        logger.debug("PatientVisitRegistry removed [visitId]:[{}]", visitId);
    }

    public void attachedVisitToCase(List<String> visitIds, String caseId) throws CMSException {
        if (!caseDatabaseService.exists(caseId)) {
            logger.debug("Referred case cannot find for [caseId]:[{}]", caseId);
            throw new CMSException(StatusCode.E1002, "Case Id not valid");
        }
        for (String visitId : visitIds) {
            if (!patientVisitRegistryDatabaseService.exists(visitId)) {
                logger.debug("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
                throw new CMSException(StatusCode.E2000, "visit not found for given visitId");
            }
        }
        for (String visitId : visitIds) {
            PatientVisitRegistry visitRegistry = patientVisitRegistryDatabaseService.searchById(visitId);
            if (visitRegistry.isAttachedToCase()) {
                detachedVisitFromCase(visitId, visitRegistry.getCaseId());
            }
            caseDatabaseService.addNewVisitToCase(caseId, visitRegistry.getId());
            visitRegistry.setAttachedToCase(true);
            visitRegistry.setCaseId(caseId);
            patientVisitRegistryDatabaseService.save(visitRegistry);
            logger.debug("PatientVisitRegistry [visitId]:[{}] attached to case [caseId]:[{}]", visitId, caseId);
        }
    }

    public PatientVisitRegistry createPatientVisitRegistryForCase(String caseId, PatientVisitRegistry visitRegistry) throws CMSException {
        if (!caseDatabaseService.existsAndActive(caseId)) {
            logger.debug("Referred case not found or its not in open status for [id]:[{}]", caseId);
            throw new CMSException(StatusCode.E1002);
        }
        Case aCase = caseDatabaseService.findByCaseId(caseId);
        if (aCase.isSingleVisit() && aCase.getVisitIds().size() > 0) {
            logger.debug("Case id [" + caseId + "] is a single visit case which already has another visit attached to it");
            throw new CMSException(StatusCode.E2000, "Case ID is not allowed to have multiple visits attached");
        }

        checkPatientVisitRegistryValidity(visitRegistry);
        visitRegistry.setVisitNumber(runningNumberService.generateVisitNumber());
        visitRegistry.setAttachedToCase(true);
        visitRegistry.setVisitStatus(PatientVisitRegistry.PatientVisitState.INITIAL);
        visitRegistry.setCaseId(caseId);
        visitRegistry.setMedicalReference(new MedicalReference());
        visitRegistry.setStartTime(LocalDateTime.now());
        visitRegistry.setPatientQueue(queueService.generateQueueNumber(visitRegistry.getClinicId(), visitRegistry.getVisitPurpose()));
        PatientVisitRegistry createVisitRegistry = patientVisitRegistryDatabaseService.save(visitRegistry);
        caseService.attachedVisitToCase(caseId, createVisitRegistry.getId());
        logger.debug("PatientVisitRegistry [id]:[{}] created and added to the case [id]:[{}]", createVisitRegistry.getId(), caseId);
        return createVisitRegistry;
    }

    public PatientVisitRegistry createPatientVisitRegistry(PatientVisitRegistry visitRegistry, List<String> coverageIds, Boolean isSingleVisitCase) throws CMSException {
        checkPatientVisitRegistryValidity(visitRegistry);
        visitRegistry.setVisitNumber(runningNumberService.generateVisitNumber());
        visitRegistry.setAttachedToCase(true);
        visitRegistry.setVisitStatus(PatientVisitRegistry.PatientVisitState.INITIAL);
        visitRegistry.setMedicalReference(new MedicalReference());
        visitRegistry.setStartTime(LocalDateTime.now());
        PatientVisitRegistry.PatientQueue patientQueue = queueService.generateQueueNumber(visitRegistry.getClinicId(), visitRegistry.getVisitPurpose());
        if (visitRegistry.getPriority() != null && visitRegistry.getPriority() == Priority.HIGH) {
            patientQueue.setUrgent(true);
        }
        visitRegistry.setPatientQueue(patientQueue);
        PatientVisitRegistry createVisitRegistry = patientVisitRegistryDatabaseService.save(visitRegistry);

        Case aCase = new Case();
        aCase.setClinicId(createVisitRegistry.getClinicId());
        aCase.setPatientId(createVisitRegistry.getPatientId());
        aCase.setAttachedMedicalCoverages(checkAndAttachMedicalCoverages(coverageIds, createVisitRegistry.getPatientId()));
        Case createdCase = caseService.createNewCase(aCase, isSingleVisitCase == null || isSingleVisitCase, createVisitRegistry.getId());
        logger.debug("PatientVisitRegistry [visitId]:[{}] created and added to the case [id]:[{}]", createVisitRegistry.getVisitNumber(), createdCase.getId());
        return createVisitRegistry;
    }

    private List<AttachedMedicalCoverage> checkAndAttachMedicalCoverages(List<String> coverageIds, String patientId) throws CMSException {
        Optional<Patient> foundPatientOpt = patientDatabaseService.findPatientById(patientId);
        if (!foundPatientOpt.isPresent()) {
            throw new CMSException(StatusCode.E2000, "Patient not found");
        }
        Patient foundPatient = foundPatientOpt.get();
        List<AttachedMedicalCoverage> attachedMedicalCoverages = new ArrayList<>();
        for (String coverageId : coverageIds) {
            if (medicalCoverageService.findCoverageByPlan(coverageId) != null) {
                attachedMedicalCoverages.add(new AttachedMedicalCoverage(coverageId));
            } else {
                logger.debug("Medical coverages are not valid");
                throw new CMSException(StatusCode.E1002, "Invalid medical coverage plans");
            }
        }
        if (!policyHolderService.areCoveragesValid(foundPatient.getUserId(), attachedMedicalCoverages)) {
            logger.debug("One or more of the medical coverage is not valid");
            throw new CMSException(StatusCode.E1005, "One or more of the medical coverage is not valid");
        }
        return attachedMedicalCoverages;
    }

    public List<PatientVisitRegistry> searchClinicAndDateRange(String clinicId, LocalDateTime start, LocalDateTime end) throws CMSException {
        if (!clinicDatabaseService.exists(clinicId)) {
            logger.debug("Clinic not find for [id]: [{}]", clinicId);
            throw new CMSException(StatusCode.E2002);
        }
        List<PatientVisitRegistry> visitRegistries = patientVisitRegistryDatabaseService.listByClinicIdAndStartTime(clinicId, start, end);
        if (visitRegistries != null) {
            logger.debug("Visits found for clinic [id]:[{}] startTime between [{}] & [{}]", clinicId, start, end);
            return visitRegistries;
        } else {
            logger.debug("No visits found for clinic [id]:[{}] startTime between [{}] & [{}]", clinicId, start, end);
            return Collections.emptyList();
        }
    }

    public List<PatientVisitRegistry> searchVisitByPatentAndMonth(String patientId, String caseId, LocalDateTime startTime, LocalDateTime endTime, int limit) throws CMSException {
        if (!patientDatabaseService.exists(patientId)) {
            logger.debug("Patient not found for [id]:[{}]", patientId);
            throw new CMSException(StatusCode.E2003);
        } else {
            LocalDate startOfMonth = startTime.toLocalDate().with(firstDayOfMonth());
            LocalTime startOfDay = LocalTime.MIN;
            startTime = LocalDateTime.of(startOfMonth, startOfDay);
            if (endTime == null) {
                LocalDate endOfMonth = startTime.toLocalDate().with(lastDayOfMonth());
                LocalTime endOfDay = LocalTime.MAX;
                endTime = LocalDateTime.of(endOfMonth, endOfDay);
            }
            List<PatientVisitRegistry> visitRegistries = patientVisitRegistryDatabaseService.findByPatientIdAndStartTime(patientId, startTime, endTime);
            List<PatientVisitRegistry> attachableVisits = new ArrayList<>();
            for (PatientVisitRegistry visitRegistry : visitRegistries) {
                if (attachableVisits.size() < limit) {
                    if (caseDatabaseService.existsAndActive(visitRegistry.getCaseId()) && !visitRegistry.getCaseId().equals(caseId)) {
                        attachableVisits.add(visitRegistry);
                    }
                } else {
                    break;
                }
            }
            return attachableVisits;
        }
    }

    public PatientVisitRegistry changeStateToConsult(String visitId, String doctorId, boolean setQueueAsCalled) throws CMSException {
        if (!patientVisitRegistryDatabaseService.exists(visitId)) {
            logger.debug("PatientVisit not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000, "visit not found for given visitId");
        }

        PatientVisitRegistry currentRegistry = patientVisitRegistryDatabaseService.searchById(visitId);
        if (!PatientVisitRegistry.PatientVisitState.INITIAL.equals(currentRegistry.getVisitStatus()) ||
                PatientVisitRegistry.PatientVisitState.POST_CONSULT.equals(currentRegistry.getVisitStatus())) {
            logger.debug("Patient visit not allowed to change status to [status]:[CONSULT]");
            throw new CMSException(StatusCode.E1009);
        }
        if (PatientVisitRegistry.PatientVisitState.INITIAL.equals(currentRegistry.getVisitStatus())) {
            Consultation consultation = consultationService.createConsultation(new Consultation(currentRegistry.getPatientId(),
                    doctorId, currentRegistry.getClinicId(), LocalDateTime.now()));
            currentRegistry.getMedicalReference().setConsultation(consultation);
        }
        currentRegistry.setVisitStatus(PatientVisitRegistry.PatientVisitState.CONSULT);
        if (setQueueAsCalled) {
            currentRegistry.getPatientQueue().setPatientCalled(true);
        }
        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentRegistry);
        logger.debug("PatientVisitRegistry [visitId]:[{}] status updated to : [CONSULT]", visitId);
        return savedRegistry;
    }

    public PatientVisitRegistry saveConsultationData(String visitId, MedicalReference medicalReference, Principal principal) throws CMSException {
        PatientVisitRegistry currentRegistry = patientVisitRegistryDatabaseService.searchById(visitId);
        if (currentRegistry == null) {
            logger.debug("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }
        if (!PatientVisitRegistry.PatientVisitState.CONSULT.equals(currentRegistry.getVisitStatus())) {
            logger.debug("Patient visit not allowed to save consult data");
            throw new CMSException(StatusCode.E1009);
        }
        if (medicalReference != null) {
            validateAndCreateMedicalReference(principal, currentRegistry, medicalReference, currentRegistry.getMedicalReference());
        }

        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentRegistry);
        logger.debug("PatientVisitRegistry [visitId]:[{}] status updated to : [POST_CONSULT]", savedRegistry.getVisitNumber());
        return savedRegistry;
    }

    public PatientVisitRegistry changeStateToPostConsult(String visitId, MedicalReference medicalReference, Principal principal) throws CMSException {
        PatientVisitRegistry currentRegistry = patientVisitRegistryDatabaseService.searchById(visitId);
        logger.debug("currentRegistry:" + currentRegistry);
        if (currentRegistry == null) {
            logger.debug("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }
        if (!PatientVisitRegistry.PatientVisitState.CONSULT.equals(currentRegistry.getVisitStatus()) ||
                PatientVisitRegistry.PatientVisitState.PAYMENT.equals(currentRegistry.getVisitStatus())) {
            logger.debug("Patient visit not allowed to change status to [status]:[POST_CONSULT]");
            throw new CMSException(StatusCode.E1009);
        }
        if (PatientVisitRegistry.PatientVisitState.CONSULT.equals(currentRegistry.getVisitStatus())) {
            MedicalReference reference = validateAndCreateMedicalReference(principal, currentRegistry, medicalReference, currentRegistry.getMedicalReference());
            reference.getConsultation().setConsultationEndTime(LocalDateTime.now());
        }


        currentRegistry.setVisitStatus(PatientVisitRegistry.PatientVisitState.POST_CONSULT);
        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentRegistry);
        logger.debug("PatientVisitRegistry [visitId]:[{}] status updated to : [POST_CONSULT]", savedRegistry.getVisitNumber());
        return savedRegistry;
    }

    public PatientVisitRegistry saveDispensingData(String visitId, MedicalReference newMedicalReference, Principal principal) throws CMSException {
        PatientVisitRegistry currentRegistry = patientVisitRegistryDatabaseService.searchById(visitId);
        if (currentRegistry == null) {
            logger.debug("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }
        if (!PatientVisitRegistry.PatientVisitState.POST_CONSULT.equals(currentRegistry.getVisitStatus())) {
            logger.debug("Patient visit not allowed to save dispensing data");
            throw new CMSException(StatusCode.E1009);
        }

        if (newMedicalReference != null) {
            validateAndCreateMedicalReference(principal, currentRegistry, newMedicalReference, currentRegistry.getMedicalReference());
        }
        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentRegistry);
        logger.debug("PatientVisitRegistry [visitId]:[{}] status updated to : [PAYMENT]", savedRegistry.getVisitNumber());
        return savedRegistry;
    }

    public PatientVisitRegistry changeStatusToPayment(String visitId, MedicalReference medicalReference, Principal principal, Map<String, Integer> planMaxUsage) throws CMSException {
        PatientVisitRegistry currentRegistry = patientVisitRegistryDatabaseService.searchById(visitId);
        if (currentRegistry == null) {
            logger.debug("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }
        if (!PatientVisitRegistry.PatientVisitState.POST_CONSULT.equals(currentRegistry.getVisitStatus())) {
            logger.debug("Patient visit not allowed to change status to [status]:[PAYMENT]");
            throw new CMSException(StatusCode.E1009);
        }

        if (medicalReference != null) {

            currentRegistry.setMedicalReference(validateAndUpdateMedicalReference(principal, currentRegistry, medicalReference, currentRegistry.getMedicalReference()));
            if (medicalReference.getDispatchItems() != null) {
                updateSalesOrderPurchaseItems(currentRegistry.getCaseId(), visitId, medicalReference.getDispatchItems(), planMaxUsage);
            }
        }

        currentRegistry.setVisitStatus(PatientVisitRegistry.PatientVisitState.PAYMENT);

        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentRegistry);
        logger.debug("PatientVisitRegistry [visitId]:[{}] status updated to : [PAYMENT]", savedRegistry.getVisitNumber());
        return savedRegistry;
    }

    private void updateSalesOrderPurchaseItems(String caseId, String visitId, List<DispatchItem> dispatchItems,
                                               Map<String, Integer> planMaxUsage) throws CMSException {
        Case aCase = caseDatabaseService.findByCaseId(caseId);
        SalesOrder salesOrder = aCase.getSalesOrder();
        List<SalesItem> items = new ArrayList<>();

        Map<String, Item> itemMap = itemService.searchItemByIds(dispatchItems.stream()
                .map(DispatchItem::getItemId)
                .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(PersistedObject::getId, item -> item));

        List<ItemChargeDetail> itemChargeDetails = dispatchItems.stream()
                .map(item -> new ItemChargeDetail(item.getItemId(), item.getQuantity(), null, null, item.getExcludedCoveragePlanIds()))
                .collect(Collectors.toList());

        Map<String, ItemChargeDetail> itemPriceMapping = priceCalculationService
                .calculateSalesPrice(caseId, new ItemChargeRequest(planMaxUsage, itemChargeDetails)).getChargeDetails()
                .stream()
                .collect(Collectors.toMap(ItemChargeDetail::getItemId, itemChargeDetail -> itemChargeDetail));

        for (DispatchItem dispatchItem : dispatchItems) {
            Item item = itemMap.get(dispatchItem.getItemId());
            SalesItem salesItem = new SalesItem(item, null);

            Charge itemChargeablePrice = itemPriceMapping.get(dispatchItem.getItemId()).getCharge();
            salesItem.setSellingPrice(new SellingPrice(itemChargeablePrice.getPrice(), itemChargeablePrice.isTaxIncluded()));

            salesItem.setItemRefId(dispatchItem.getItemId());
            salesItem.setCost(item.getCost());
            salesItem.setPurchaseQty(dispatchItem.getQuantity());
            salesItem.setDosage(dispatchItem.getDosage());
            salesItem.setDuration(dispatchItem.getDuration());
            salesItem.setPurchaseUom(item.getPurchaseUom());
            salesItem.setInstruct(dispatchItem.getInstruct());
            salesItem.setBatchNo(dispatchItem.getBatchNo());
            salesItem.setRemarks(dispatchItem.getRemarks());
            salesItem.setExpireDate(dispatchItem.getExpiryDate());
            salesItem.setItemPriceAdjustment(dispatchItem.getItemPriceAdjustment());
            salesItem.setExcludedCoveragePlanIds(dispatchItem.getExcludedCoveragePlanIds());
            salesItem.populateSoldPrice();
            items.add(salesItem);
        }

        salesOrder.updatePurchaseItems(items);
        caseService.updateSalesOrder(aCase.getId(), salesOrder.getPurchaseItems());
        caseService.generateAndSaveInvoices(aCase.getId(), visitId, planMaxUsage);
    }

    public PatientVisitRegistry rollbackStatusToPostConsult(String visitId) throws CMSException {
        PatientVisitRegistry currentVisit = patientVisitRegistryDatabaseService.searchById(visitId);
        if (currentVisit == null) {
            logger.error("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }

        if (currentVisit.getVisitStatus() != PatientVisitRegistry.PatientVisitState.PAYMENT) {
            logger.error("Only visit with payment state can be rolledback to POST_CONSULT");
            throw new CMSException(StatusCode.E2000, "Visit is not payment to rollback to post_consult");
        }

        currentVisit.setVisitStatus(PatientVisitRegistry.PatientVisitState.POST_CONSULT);
        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentVisit);
        logger.info("removing invoices for rollback for visit [" + visitId + "]");
        caseService.removeInvoicesForVisit(currentVisit.getCaseId(), visitId);
        return savedRegistry;
    }


    public PatientVisitRegistry changeStatusToComplete(String visitId) throws CMSException {
        PatientVisitRegistry currentVisit = patientVisitRegistryDatabaseService.searchById(visitId);
        if (currentVisit == null) {
            logger.error("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }

        Case aCase = caseDatabaseService.findByCaseId(currentVisit.getCaseId());
        if (aCase == null) {
            logger.error("Case not found for [caseId]:[{}]", currentVisit.getCaseId());
            throw new CMSException(StatusCode.E1002);
        }
        currentVisit.setVisitStatus(PatientVisitRegistry.PatientVisitState.COMPLETE);
        currentVisit.setEndTime(LocalDateTime.now());
        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentVisit);
        caseService.processCaseClosure(savedRegistry.getCaseId());
//        if (invoiceCalculationService.calculateDueAmount(aCase.getId()) <= 0 && aCase.isSingleVisit()) {
//            caseService.closeCase(aCase.getId());
//            logger.debug("Case status changed to closed since single visit and no due amount");
//        }

        logger.debug("PatientVisitRegistry [visitId]:[{}] status updated to : [PAYMENT]", savedRegistry.getVisitNumber());
        return savedRegistry;

    }

    public PatientVisitRegistry updateConsultation(String visitId, Consultation consultation, List<String> diagnosisIds, Principal principal) throws CMSException {
        PatientVisitRegistry currentVisit = patientVisitRegistryDatabaseService.searchById(visitId);
        if (currentVisit == null) {
            logger.error("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }

        String consultDoctorId = null;
        MedicalReference medicalReference = currentVisit.getMedicalReference();
        if (medicalReference != null && medicalReference.getConsultation() != null
                && medicalReference.getConsultation().getDoctorId() != null) {
            consultDoctorId = medicalReference.getConsultation().getDoctorId();
        }

        Optional<Doctor> doctorOpt = doctorDatabaseService.findOne(consultDoctorId);
        if (!UserInfoHelper.isCA(principal) && doctorOpt.isPresent() && !doctorOpt.get().getUsername().equals(UserInfoHelper.loginName(principal))) {
            logger.error("Consultation update not done by the same consultation doctor[{}],login doctor[{}] for [visitId]:[{}]", doctorOpt.get().getUsername(), UserInfoHelper.loginName(principal), visitId);
            throw new CMSException(StatusCode.E1010, "Patient referral update should done by the same doctor");
        }
        if (currentVisit.getVisitStatus() == PatientVisitRegistry.PatientVisitState.COMPLETE
                && currentVisit.getEndTime().plusHours(EDITABLE_HOURS).isBefore(LocalDateTime.now())) {
            logger.error("Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E1010, "Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete");
        }

        if (diagnosisIds == null || diagnosisIds.size() == 0) {
            logger.error("Minimum of one diagnosis should be set");
            throw new CMSException(StatusCode.E1010, "Minimum of one diagnosis should be set");
        }
        if (diagnosisService.checkDiagnosisIdsValidity(diagnosisIds)) {
            currentVisit.getMedicalReference().setDiagnosisIds(diagnosisIds);
        }
        if (!consultation.areParametersValid() || consultation.getId() == null) {
            logger.error("Consultation parameters are not valid [" + consultation + "]");
            throw new CMSException(StatusCode.E1010, "Consultation details are not valid");
        }
        consultationService.isConsultationExists(consultation.getId());
        currentVisit.getMedicalReference().setConsultation(consultationService.updateConsultation(consultation.getId(), consultation));

        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentVisit);
        logger.debug("PatientVisitRegistry [visitId]:[{}] consultation updated", savedRegistry.getVisitNumber());
        return savedRegistry;
    }

    public PatientVisitRegistry updatePatientReferral(String visitId, PatientReferral referral, Principal principal) throws CMSException {
        PatientVisitRegistry currentVisit = patientVisitRegistryDatabaseService.searchById(visitId);

        if (currentVisit == null) {
            logger.debug("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }
        String consultDoctorId = null;
        MedicalReference medicalReference = currentVisit.getMedicalReference();
        if (medicalReference != null && medicalReference.getPatientReferral() != null
                && medicalReference.getPatientReferral().getPatientReferrals() != null) {
            Optional<PatientReferral.PatientReferralDetails> first = medicalReference.getPatientReferral().getPatientReferrals().stream()
                    .findFirst();
            if (first.isPresent()) {
                consultDoctorId = first.get().getDoctorId();
            }
        }

        Optional<Doctor> doctorOpt = doctorDatabaseService.findOne(consultDoctorId);
        if (!UserInfoHelper.isCA(principal) && doctorOpt.isPresent() && !doctorOpt.get().getUsername().equals(UserInfoHelper.loginName(principal))) {
            logger.error("Consultation update not done by the same consultation doctor[{}],login doctor[{}] for [visitId]:[{}]",
                    doctorOpt.get().getUsername(), UserInfoHelper.loginName(principal), visitId);
            throw new CMSException(StatusCode.E1010, "Patient referral update should done by the same doctor");
        }
        if (currentVisit.getVisitStatus() == PatientVisitRegistry.PatientVisitState.COMPLETE
                && currentVisit.getEndTime().plusHours(EDITABLE_HOURS).isBefore(LocalDateTime.now())) {
            logger.error("Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E1010, "Updates are only allowed till " + EDITABLE_HOURS + " hours after visit complete");
        }
        if (!referral.areParametersValid() || referral.getId() == null) {
            logger.error("PatientReferral parameters are not valid [" + referral + "]");
            throw new CMSException(StatusCode.E1010, "PatientReferral details are not valid");
        }
        patientReferralService.isPatientReferralExists(referral.getId());

        currentVisit.getMedicalReference().getPatientReferral().copy(referral);
        patientReferralService.updateReferral(referral.getId(), referral);
        return currentVisit;
    }

    public PatientVisitRegistry updateConsultationFollowup(String visitId, ConsultationFollowup followup, Principal principal) throws CMSException {
        PatientVisitRegistry currentVisit = patientVisitRegistryDatabaseService.searchById(visitId);

        if (currentVisit == null) {
            logger.debug("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }
        String consultDoctorId = null;
        MedicalReference medicalReference = currentVisit.getMedicalReference();
        if (medicalReference != null && medicalReference.getConsultationFollowup() != null) {
            consultDoctorId = medicalReference.getConsultationFollowup().getDoctorId();
        }

        Optional<Doctor> doctorOpt = doctorDatabaseService.findOne(consultDoctorId);
        if (!UserInfoHelper.isCA(principal) && doctorOpt.isPresent() && !doctorOpt.get().getUsername().equals(UserInfoHelper.loginName(principal))) {
            logger.error("Consultation update not done by the same consultation doctor[{}],login doctor[{}] for [visitId]:[{}]", doctorOpt.get().getUsername(), UserInfoHelper.loginName(principal), visitId);
            throw new CMSException(StatusCode.E1010, "Patient referral update should done by the same doctor");
        }
        if (currentVisit.getVisitStatus() == PatientVisitRegistry.PatientVisitState.COMPLETE
                && currentVisit.getEndTime().plusHours(EDITABLE_HOURS).isBefore(LocalDateTime.now())) {
            logger.error("Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E1010, "Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete");
        }
        if (!followup.areParametersValid() || followup.getId() == null) {
            logger.error("ConsultationFollowup parameters are not valid [" + followup + "]");
            throw new CMSException(StatusCode.E1010, "ConsultationFollowup details are not valid");
        }
        consultationFollowupService.isConsultationFollowupExists(followup.getId());

        currentVisit.getMedicalReference().getConsultationFollowup().copyParameters(followup);
        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentVisit);
        logger.debug("PatientVisitRegistry [visitId]:[{}] PatientReferral updated", savedRegistry.getVisitNumber());
        return savedRegistry;
    }

    public PatientVisitRegistry updateMedicalCertificates(String visitId, List<MedicalCertificate> medicalCertificates, Principal principal) throws CMSException {
        PatientVisitRegistry currentVisit = patientVisitRegistryDatabaseService.searchById(visitId);

        if (currentVisit == null) {
            logger.debug("PatientVisitRegistry not found for [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E2000);
        }
        String consultDoctorId = null;
        MedicalReference medicalReference = currentVisit.getMedicalReference();
        if (medicalReference != null && medicalReference.getConsultation() != null
                && medicalReference.getConsultation().getDoctorId() != null) {
            consultDoctorId = medicalReference.getConsultation().getDoctorId();
        }

        Optional<Doctor> doctorOpt = doctorDatabaseService.findOne(consultDoctorId);
        if (!UserInfoHelper.isCA(principal) && doctorOpt.isPresent() && !doctorOpt.get().getUsername().equals(UserInfoHelper.loginName(principal))) {
            logger.error("Consultation update not done by the same consultation doctor[{}],login doctor[{}] for [visitId]:[{}]", doctorOpt.get().getUsername(), UserInfoHelper.loginName(principal), visitId);
            throw new CMSException(StatusCode.E1010, "Patient referral update should done by the same doctor");
        }
        if (currentVisit.getVisitStatus() == PatientVisitRegistry.PatientVisitState.COMPLETE
                && currentVisit.getEndTime().plusHours(EDITABLE_HOURS).isBefore(LocalDateTime.now())) {
            logger.error("Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete [visitId]:[{}]", visitId);
            throw new CMSException(StatusCode.E1010, "Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete");
        }
        if (medicalCertificates == null || medicalCertificates.isEmpty()) {
            logger.debug("Provided Medical certificates is empty");
            throw new CMSException(StatusCode.E1005);
        }
        if (checkMedicalCertificateValidity(medicalCertificates)) {
            currentVisit.getMedicalReference().setMedicalCertificates(medicalCertificates);
        }

        PatientVisitRegistry savedRegistry = patientVisitRegistryDatabaseService.save(currentVisit);
        logger.debug("PatientVisitRegistry [visitId]:[{}] PatientReferral updated", savedRegistry.getVisitNumber());
        return savedRegistry;
    }

    private boolean checkMedicalCertificateValidity(List<MedicalCertificate> certificates) throws CMSException {
        for (MedicalCertificate certificate : certificates) {
            if (!certificate.areParametersValid()) {
                logger.debug("Medical certificates are not parameter valid");
                throw new CMSException(StatusCode.E1002);
            }
        }
        return true;
    }


    private MedicalReference validateAndUpdateMedicalReference(Principal principal, PatientVisitRegistry patientVisitRegistry, MedicalReference newReference, MedicalReference currentReference) throws CMSException {


        if (patientVisitRegistry.getVisitStatus() == PatientVisitRegistry.PatientVisitState.COMPLETE
                && patientVisitRegistry.getEndTime().plusHours(EDITABLE_HOURS).isBefore(LocalDateTime.now())) {
            logger.error("Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete [visitId]:[{}]", patientVisitRegistry.getVisitNumber());
            throw new CMSException(StatusCode.E1010, "Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete");
        }

        String consultDoctorId = null;
        if (currentReference.getConsultation() != null && currentReference.getConsultation().getDoctorId() != null) {
            consultDoctorId = currentReference.getConsultation().getDoctorId();
        }

        Optional<Doctor> doctorOpt = doctorDatabaseService.findOne(consultDoctorId);
        if (!UserInfoHelper.isCA(principal) && doctorOpt.isPresent() && !doctorOpt.get().getUsername().equals(UserInfoHelper.loginName(principal))) {
            logger.error("Consultation update not done by the same consultation doctor[{}],login doctor[{}] for [visitId]:[{}]", doctorOpt.get().getUsername(), UserInfoHelper.loginName(principal), patientVisitRegistry.getVisitNumber());
            throw new CMSException(StatusCode.E1010, "Patient referral update should done by the same doctor");
        }

        if (!newReference.areParameterValid()) {
            logger.debug("MedicalReference parameters not valid");
            throw new CMSException(StatusCode.E1002);
        }

        if (newReference.getConsultation() != null) {
            consultationService.checkConsultationValidity(newReference.getConsultation());
            if (currentReference.getConsultation() != null) {
                Consultation consultation = currentReference.getConsultation().copy(newReference.getConsultation());
                currentReference.setConsultation(consultationService.updateConsultation(consultation.getId(), consultation));
            } else {
                currentReference.setConsultation(consultationService.createConsultation(newReference.getConsultation()));
            }
        }

        if (newReference.getConsultationFollowup() != null) {
            consultationFollowupService.checkConsultationFollowupValidity(newReference.getConsultationFollowup());
            if (currentReference.getConsultationFollowup() != null) {
                ConsultationFollowup followup = currentReference.getConsultationFollowup().copyParameters(newReference.getConsultationFollowup());
                currentReference.setConsultationFollowup(consultationFollowupService.updateFollowup(followup.getId(), followup));
            } else {
                currentReference.setConsultationFollowup(consultationFollowupService.createFollowup(newReference.getConsultationFollowup()));
            }
        }

        if (newReference.getPatientReferral() != null) {
            patientReferralService.checkPatientReferralValidity(newReference.getPatientReferral());
            if (currentReference.getPatientReferral() != null) {
                PatientReferral referral = currentReference.getPatientReferral().copy(newReference.getPatientReferral());
                currentReference.setPatientReferral(patientReferralService.updateReferral(referral.getId(), referral));
            } else {
                currentReference.setPatientReferral(patientReferralService.createReferral(newReference.getPatientReferral()));
            }
        }

        currentReference.copyNonReferanceFields(newReference);
        return currentReference;
    }

    private MedicalReference validateAndCreateMedicalReference(Principal principal, PatientVisitRegistry patientVisitRegistry,
                                                               MedicalReference newReference, MedicalReference currentReference) throws CMSException {
        String consultDoctorId = null;
        if (currentReference.getConsultation() != null && currentReference.getConsultation().getDoctorId() != null) {
            consultDoctorId = currentReference.getConsultation().getDoctorId();
        }

        Optional<Doctor> doctorOpt = doctorDatabaseService.findOne(consultDoctorId);
        if (!UserInfoHelper.isCA(principal) && doctorOpt.isPresent() && !doctorOpt.get().getUsername().equals(UserInfoHelper.loginName(principal))) {
            logger.error("Consultation update not done by the same consultation doctor[{}],login doctor[{}] for [visitId]:[{}]", doctorOpt.get().getUsername(), UserInfoHelper.loginName(principal), patientVisitRegistry.getVisitNumber());
            throw new CMSException(StatusCode.E1010, "Patient referral update should done by the same doctor");
        }
        if (patientVisitRegistry.getVisitStatus() == PatientVisitRegistry.PatientVisitState.COMPLETE
                && patientVisitRegistry.getEndTime().plusHours(EDITABLE_HOURS).isBefore(LocalDateTime.now())) {
            logger.error("Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete [visitId]:[{}]", patientVisitRegistry.getVisitNumber());
            throw new CMSException(StatusCode.E1010, "Updates are only allowed till [" + EDITABLE_HOURS + "] hours after visit complete");
        }

        if (newReference.getConsultation() != null) {
            consultationService.checkConsultationValidity(newReference.getConsultation());
        }

        if (newReference.getConsultationFollowup() != null) {
            consultationFollowupService.checkConsultationFollowupValidity(newReference.getConsultationFollowup());
        }

        if (newReference.getPatientReferral() != null) {
            patientReferralService.checkPatientReferralValidity(newReference.getPatientReferral());
        }

        if (newReference.getConsultation() != null) {
            if (currentReference.getConsultation() != null) {
                Consultation consultation = currentReference.getConsultation().copy(newReference.getConsultation());
                currentReference.setConsultation(consultationService.updateConsultation(consultation.getId(), consultation));
            } else {
                currentReference.setConsultation(consultationService.createConsultation(newReference.getConsultation()));
            }
        }
        if (newReference.getConsultationFollowup() != null) {
            if (currentReference.getConsultationFollowup() != null) {
                ConsultationFollowup followup = currentReference.getConsultationFollowup().copyParameters(newReference.getConsultationFollowup());
                currentReference.setConsultationFollowup(consultationFollowupService.updateFollowup(followup.getId(), followup));
            } else {
                currentReference.setConsultationFollowup(consultationFollowupService.createFollowup(newReference.getConsultationFollowup()));
            }
        }
        if (newReference.getPatientReferral() != null) {
            if (currentReference.getPatientReferral() != null) {
                PatientReferral referral = currentReference.getPatientReferral().copy(newReference.getPatientReferral());
                currentReference.setPatientReferral(patientReferralService.updateReferral(referral.getId(), referral));
            } else {
                currentReference.setPatientReferral(patientReferralService.createReferral(newReference.getPatientReferral()));
            }
        }
        currentReference.copyNonReferanceFields(newReference);
        return currentReference;
    }

    private void checkPatientVisitRegistryValidity(PatientVisitRegistry registry) throws CMSException {
        if (!registry.areParametersValid()) {
            logger.debug("Parameters not valid in PatientVisitRegistry [{}]", registry.toString());
            throw new CMSException(StatusCode.E1002);
        }
        if (!clinicDatabaseService.exists(registry.getClinicId())) {
            logger.debug("Clinic not found for id : [{}]", registry.getClinicId());
            throw new CMSException(StatusCode.E2002);
        }
        if (!patientDatabaseService.exists(registry.getPatientId())) {
            logger.debug("Patient not found for id : [{}]", registry.getPatientId());
            throw new CMSException(StatusCode.E2003);
        }
        if (registry.getPreferredDoctorId() != null && !doctorDatabaseService.exists(registry.getPreferredDoctorId())) {
            logger.debug("Doctor not found for id : [{}]", registry.getPreferredDoctorId());
            throw new CMSException(StatusCode.E1002, "Couldn't find a doctor for given id");
        }
    }

}
