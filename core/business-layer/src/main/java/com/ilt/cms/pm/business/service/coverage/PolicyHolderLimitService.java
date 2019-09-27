package com.ilt.cms.pm.business.service.coverage;

import com.google.common.cache.LoadingCache;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.casem.SalesOrder;
import com.ilt.cms.core.entity.coverage.CapLimiter;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.repository.spring.CaseRepository;
import com.ilt.cms.repository.spring.CaseRepositoryImpl;
import com.ilt.cms.repository.spring.PatientVisitRegistryRepository;
import com.ilt.cms.repository.spring.SalesOrderRepository;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PolicyHolderLimitService {

    public static final int PLAN_LIMIT_EXCEEDED = -1;
    private static final Logger logger = LoggerFactory.getLogger(PolicyHolderLimitService.class);
    private CaseRepositoryImpl caseRepositoryCustom;
    private CaseRepository caseRepository;
    private MedicalCoverageRepository medicalCoverageRepository;
    private PatientVisitRegistryRepository patientVisitRegistryRepository;
    private SalesOrderRepository salesOrderRepository;

    private LoadingCache<LimitCheckUser, Integer> yearlyUsageCache;
    private LoadingCache<LimitCheckUser, Integer> monthlyUsageCache;
    private LoadingCache<LimitCheckUser, Integer> weeklyUsageCache;
    private LoadingCache<LimitCheckUser, Integer> dailyUsageCache;
    private LoadingCache<LimitCheckUser, Integer> patientUsageLimitPerVisitCache;
    private LoadingCache<String, PatientVisitCountHolder> patientVisitCountHolderCache;

    public PolicyHolderLimitService(CaseRepositoryImpl caseRepositoryCustom,
                                    CaseRepository caseRepository,
                                    MedicalCoverageRepository medicalCoverageRepository,
                                    PatientVisitRegistryRepository patientVisitRegistryRepository,
                                    SalesOrderRepository salesOrderRepository) {

        this.caseRepositoryCustom = caseRepositoryCustom;
        this.caseRepository = caseRepository;
        this.medicalCoverageRepository = medicalCoverageRepository;
        this.patientVisitRegistryRepository = patientVisitRegistryRepository;
        this.salesOrderRepository = salesOrderRepository;
    }

    @PostConstruct
    public void initCache() {
//        yearlyUsageCache = CacheBuilder.newBuilder()
//                .expireAfterWrite(1, TimeUnit.MINUTES)
//                .build(new CacheLoader<>() {
//                    @Override
//                    public Integer load(LimitCheckUser limitCheckUser) {
//                        logger.debug("Loading patient {} annual usage limit usage for plan {} to cache.",
//                                limitCheckUser.getPatientId(), limitCheckUser.getPlanId());
//                        return calculateCurrentAvailableLimitWithTimeConstraint(limitCheckUser.getPatientId(),
//                                limitCheckUser.getPlanId(),
//                                LocalDateTime.of(LocalDate.of(LocalDate.now().getYear(), 1, 1),
//                                        LocalTime.MIDNIGHT),
//                                CoveragePlan::getCapPerYear,
//                                PatientVisitCountHolder::getThisYearsVisitCounts
//                        );
//                    }
//                });
//        monthlyUsageCache = CacheBuilder.newBuilder()
//                .expireAfterWrite(1, TimeUnit.MINUTES)
//                .build(new CacheLoader<>() {
//                    @Override
//                    public Integer load(LimitCheckUser limitCheckUser) {
//                        logger.debug("Loading patient {} monthly usage limit usage for plan {} to cache.",
//                                limitCheckUser.getPatientId(), limitCheckUser.getPlanId());
//                        LocalDate today = LocalDate.now();
//                        return calculateCurrentAvailableLimitWithTimeConstraint(limitCheckUser.getPatientId(),
//                                limitCheckUser.getPlanId(),
//                                LocalDateTime.of(LocalDate.of(today.getYear(), today.getMonth(), 1),
//                                        LocalTime.MIDNIGHT),
//                                CoveragePlan::getCapPerMonth,
//                                PatientVisitCountHolder::getThisMonthsVisitCounts);
//                    }
//                });
//        weeklyUsageCache = CacheBuilder.newBuilder()
//                .expireAfterWrite(1, TimeUnit.MINUTES)
//                .build(new CacheLoader<>() {
//                    @Override
//                    public Integer load(LimitCheckUser limitCheckUser) {
//                        logger.debug("Loading patient {} weekly usage limit usage for plan {} to cache.",
//                                limitCheckUser.getPatientId(), limitCheckUser.getPlanId());
//                        return calculateCurrentAvailableLimitWithTimeConstraint(limitCheckUser.getPatientId(),
//                                limitCheckUser.getPlanId(),
//                                LocalDateTime.of(LocalDate.now().with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()).minusWeeks(1),
//                                        LocalTime.MIDNIGHT),
//                                CoveragePlan::getCapPerWeek,
//                                PatientVisitCountHolder::getThisWeeksVisitCounts);
//                    }
//                });
//        dailyUsageCache = CacheBuilder.newBuilder()
//                .expireAfterWrite(1, TimeUnit.MINUTES)
//                .build(new CacheLoader<>() {
//                    @Override
//                    public Integer load(LimitCheckUser limitCheckUser) {
//                        logger.debug("Loading patient {} daily usage limit usage for plan {} to cache.",
//                                limitCheckUser.getPatientId(), limitCheckUser.getPlanId());
//                        return calculateCurrentAvailableLimitWithTimeConstraint(limitCheckUser.getPatientId(),
//                                limitCheckUser.getPlanId(),
//                                LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
//                                CoveragePlan::getCapPerDay,
//                                PatientVisitCountHolder::getTodaysVisitCounts);
//                    }
//                });
//
//        patientUsageLimitPerVisitCache = CacheBuilder.newBuilder()
//                .expireAfterWrite(1, TimeUnit.MINUTES)
//                .build(new CacheLoader<>() {
//                    @Override
//                    public Integer load(LimitCheckUser user) {
//                        return Optional.ofNullable(getPatientVisitLimit(user.getPatientId(), user.getPlanId()))
//                                .orElse(PLAN_LIMIT_EXCEEDED);
//                    }
//                });
//
//        patientVisitCountHolderCache = CacheBuilder.newBuilder()
//                .expireAfterWrite(1, TimeUnit.MINUTES)
//                .build(new CacheLoader<>() {
//                    @Override
//                    public PatientVisitCountHolder load(String patientId) {
//                        return calculateVisitCountForPatient(patientId);
//                    }
//                });


    }


    public Map<String, Integer> findAvailableLimits(List<AttachedMedicalCoverage> attachedCoverages, String patientId) {
        return attachedCoverages.stream()
                .collect(Collectors
                        .toMap(AttachedMedicalCoverage::getPlanId, attachedMedicalCoverage ->
                                calculateCurrentAvailableUsage(patientId, attachedMedicalCoverage.getPlanId())));
    }


    public int calculateCurrentAvailableUsage(String patientId, String planId) {
        int limitAmount;

        int availableAmount = calculateCurrentAvailableVisitLimit(patientId, planId);
        if (availableAmount > 0) {
            limitAmount = calculateCurrentAvailableDailyLimit(patientId, planId);
            availableAmount = getMostRestrictingLimit(availableAmount, limitAmount);
        }

        if (availableAmount > 0) {
            limitAmount = calculateCurrentAvailableWeeklyLimit(patientId, planId);
            availableAmount = getMostRestrictingLimit(availableAmount, limitAmount);
        }

        if (availableAmount > 0) {
            limitAmount = calculateCurrentAvailableMonthlyLimit(patientId, planId);
            availableAmount = getMostRestrictingLimit(availableAmount, limitAmount);
        }

        if (availableAmount > 0) {
            limitAmount = calculateCurrentAvailableYearlyLimit(patientId, planId);
            availableAmount = getMostRestrictingLimit(availableAmount, limitAmount);
        }

        return availableAmount;
    }


    public int calculateCurrentAvailableYearlyLimit(String patientId, String planId) {
        try {
            return yearlyUsageCache.get(new LimitCheckUser(patientId, planId));
        } catch (ExecutionException e) {
            logger.error("Error occurred while getting usage limits from cache. Calculating manually, exception :", e.getMessage());
            LocalDate today = LocalDate.now();
            return calculateCurrentAvailableLimitWithTimeConstraint(patientId, planId,
                    LocalDateTime.of(LocalDate.of(today.getYear(), 1, 1),
                            LocalTime.MIDNIGHT),
                    CoveragePlan::getCapPerYear,
                    PatientVisitCountHolder::getThisYearsVisitCounts);
        }
    }

    public int calculateCurrentAvailableMonthlyLimit(String patientId, String planId) {
        try {
            return monthlyUsageCache.get(new LimitCheckUser(patientId, planId));
        } catch (ExecutionException e) {
            logger.error("Error occurred while getting usage limits from cache. Calculating manually, exception :", e.getMessage());
            LocalDate today = LocalDate.now();
            return calculateCurrentAvailableLimitWithTimeConstraint(patientId, planId,
                    LocalDateTime.of(LocalDate.of(today.getYear(), today.getMonth(), 1),
                            LocalTime.MIDNIGHT),
                    CoveragePlan::getCapPerMonth,
                    PatientVisitCountHolder::getThisMonthsVisitCounts);
        }
    }

    public int calculateCurrentAvailableWeeklyLimit(String patientId, String planId) {
        try {
            return weeklyUsageCache.get(new LimitCheckUser(patientId, planId));
        } catch (ExecutionException e) {
            logger.error("Error occurred while getting usage limits from cache. Calculating manually, exception :", e.getMessage());
            return calculateCurrentAvailableLimitWithTimeConstraint(patientId, planId,
                    LocalDateTime.of(LocalDate.now().with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()).minusWeeks(1),
                            LocalTime.MIDNIGHT),
                    CoveragePlan::getCapPerWeek,
                    PatientVisitCountHolder::getThisWeeksVisitCounts);
        }
    }

    public int calculateCurrentAvailableDailyLimit(String patientId, String planId) {
        try {
            return dailyUsageCache.get(new LimitCheckUser(patientId, planId));
        } catch (ExecutionException e) {
            logger.error("Error occurred while getting usage limits from cache. Calculating manually, exception :", e.getMessage());
            return calculateCurrentAvailableLimitWithTimeConstraint(patientId, planId,
                    LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT),
                    CoveragePlan::getCapPerDay,
                    PatientVisitCountHolder::getTodaysVisitCounts);
        }
    }

    public int calculateCurrentAvailableVisitLimit(String patientId, String planId) {
        try {
            return patientUsageLimitPerVisitCache.get(new LimitCheckUser(patientId, planId));
        } catch (ExecutionException e) {
            logger.error("Error occurred while getting usage limits from cache. Calculating manually, exception :", e.getMessage());
            return Optional.ofNullable(getPatientVisitLimit(patientId, planId)).orElse(PLAN_LIMIT_EXCEEDED);
        }
    }

    private int getMostRestrictingLimit(int limitA, int limitB) {
        if (limitB > 0) {
            return limitA > limitB ? limitB : limitA;
        } else {
            return limitB;
        }
    }

    private Integer getPatientVisitLimit(String patientId, String planId) {
        Map<String, CoveragePlan> planUsageLimits = getCoverageUsageLimitsForPlans(patientId);
        if (planUsageLimits.containsKey(planId)) {
            return planUsageLimits.get(planId).getCapPerVisit().getLimit();
        } else {
            // if the attached medical plan has never been used before get the full limit from the DB
            Optional<CoveragePlan> coveragePlanOpt = fetchCoveragePlanDetailsFromDB(planId);
            return coveragePlanOpt.map(coveragePlan -> coveragePlan.getCapPerVisit().getLimit()).orElse(PLAN_LIMIT_EXCEEDED);
        }
    }

    private Optional<CoveragePlan> fetchCoveragePlanDetailsFromDB(String planId) {
        return medicalCoverageRepository.findMedicalCoverageByPlanId(planId).getCoveragePlans()
                .stream()
                .filter(coveragePlan -> coveragePlan.getId().equalsIgnoreCase(planId))
                .findFirst();
    }

    // todo vimukthi : remove duplicated blocks and refactor
    private PatientVisitCountHolder calculateVisitCountForPatient(String patientId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        LocalDateTime startOfThisYear = LocalDateTime.of(LocalDate.of(startOfDay.getYear(), 1, 1), LocalTime.MIDNIGHT);
        LocalDateTime startOfThisMonth = LocalDateTime.of(LocalDate.of(startOfDay.getYear(), startOfDay.getMonth(), 1), LocalTime.MIDNIGHT);
        LocalDateTime startOfThisWeek = LocalDateTime.of(today
                .with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()).minusWeeks(1), LocalTime.MIDNIGHT);
        List<PatientVisitRegistry> patientVisits = patientVisitRegistryRepository.findAllByPatientIdAndStartTimeBetween(patientId, startOfThisYear, LocalDateTime.of(LocalDate.of(today.getYear(), Month.DECEMBER.getValue(), 31),
                LocalTime.MIDNIGHT));
        List<String> visitIds = patientVisits.stream()
                .filter(PatientVisitRegistry::isAttachedToCase)
                .map(PatientVisitRegistry::getCaseId)
                .collect(Collectors.toList());

        List<SalesOrder> salesOrders = salesOrderRepository
                .findSalesOrdersWithInvoicesCreatedInSearchPeriodWithVisitIds(visitIds, startOfThisYear, today.atTime(LocalTime.now()));


        ConcurrentMap<String, Long> thisYearsVisitCounts = salesOrders.parallelStream()
                .filter(salesOrder -> salesOrder.getInvoices() != null
                        && salesOrder.getInvoices().size() > 0)
                .flatMap(salesOrder -> salesOrder.getInvoices().stream())
                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
                .collect(Collectors.groupingByConcurrent(Invoice::getPlanId, Collectors.counting()));

        ConcurrentMap<String, Long> thisMonthsVisitCounts = generateVisitCountMapForPlans(today, startOfThisMonth, salesOrders);

        ConcurrentMap<String, Long> thisWeeksVisitCounts = generateVisitCountMapForPlans(today, startOfThisWeek, salesOrders);

        ConcurrentMap<String, Long> todaysVisitCounts = generateVisitCountMapForPlans(today, startOfDay, salesOrders);


        return new PatientVisitCountHolder(thisYearsVisitCounts, thisMonthsVisitCounts,
                thisWeeksVisitCounts, todaysVisitCounts);

    }

    private ConcurrentMap<String, Long> generateVisitCountMapForPlans(LocalDate periodEndDate,
                                                                      LocalDateTime periodStartDate,
                                                                      List<SalesOrder> salesOrders) {
        return salesOrders.parallelStream()
                .filter(salesOrder -> salesOrder.getInvoices() != null)
                .flatMap(salesOrder -> salesOrder.getInvoices().stream())
                .filter(invoice -> periodStartDate.isBefore(invoice.getInvoiceTime())
                        && periodEndDate.atTime(LocalTime.now()).isAfter(invoice.getInvoiceTime()))
                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT)
                .collect(Collectors.groupingByConcurrent(Invoice::getPlanId, Collectors.counting()));
    }

    private int calculateCurrentAvailableLimitWithTimeConstraint(String patientId, String planId,
                                                                 LocalDateTime timeConstraint,
                                                                 Function<CoveragePlan, CapLimiter> retrieveFunction,
                                                                 Function<PatientVisitCountHolder, Map<String, Long>> retrieveVisitLimitFunction) {

        CoveragePlan plan = getCoverageUsageLimitsForPlans(patientId)
                .getOrDefault(planId, fetchCoveragePlanDetailsFromDB(planId).orElse(null));

        if (plan == null) {
            // when the plan is not in the DB don't create a invoice by sending PLAN_LIMIT_EXCEEDED
            return PLAN_LIMIT_EXCEEDED;
        }

        int fullAvailableLimit, allPaidAmount, pendingAmount;

        MedicalCoverage medicalCoverage = medicalCoverageRepository.findMedicalCoverageByPlanId(planId);
        CoveragePlan thisPlan = medicalCoverage.getCoveragePlans().stream()
                .filter(coveragePlan -> coveragePlan.getId().equals(planId)).findFirst().get();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(now.getYear(), 1, 1), LocalTime.MIDNIGHT);

        if (!CoveragePlan.LimitResetType.CALENDAR.equals(thisPlan.getLimitResetType())) {
            // get the first invoice created for the patient for this plan
            Optional<Invoice> firstUseOpt = caseRepositoryCustom.findFirstInvoiceOfPlanForPatient(patientId, planId);

            if (firstUseOpt.isPresent()) {

                LocalDateTime cycleFirstTimeUsed = firstUseOpt.get().getInvoiceTime();
                LocalDateTime thisYearsResetDate = LocalDate.of(startDate.getYear(), cycleFirstTimeUsed.getMonth(),
                        cycleFirstTimeUsed.getDayOfMonth()).atStartOfDay();

                if (cycleFirstTimeUsed.isBefore(startDate)) {
                    startDate = thisYearsResetDate.isBefore(now) ? thisYearsResetDate : thisYearsResetDate.minusYears(1);
                } else {
                    startDate = cycleFirstTimeUsed;
                }

            } else {
                // this is the first use of the plan
                startDate = LocalDate.now().atStartOfDay();
            }
        }
        List<Invoice> allInvoicesByPatientId = caseRepositoryCustom
                .findAllInvoicesForGivenTimePeriodByPatientId(patientId, startDate, startDate.plusYears(1));

        // since we only use current plan details no need to populate all plan details here. will get only invoices with given plan
        allPaidAmount = getUsageForPlanWithTimeConstraint(allInvoicesByPatientId, timeConstraint, invoice -> {
            if (planId.equals(invoice.getPlanId()) && isValidInvoice(invoice)) {
                return isPayableClaim(invoice.getClaim().getClaimStatus());
            }
            return false;

        }, invoice -> {
            if (invoice.getClaim().getClaimStatus() != Claim.ClaimStatus.PAID) {
                return invoice.getPayableAmount();
            } else {
                return invoice.getPaidAmount();
            }
        });
        pendingAmount = getUsageForPlanWithTimeConstraint(allInvoicesByPatientId, timeConstraint, invoice ->
                (planId.equals(invoice.getPlanId()) && isValidInvoice(invoice)
                        && invoice.getClaim().getClaimStatus() == Claim.ClaimStatus.PENDING), Invoice::getPayableAmount);

        PatientVisitCountHolder visitCountHolder;
        try {
            visitCountHolder = patientVisitCountHolderCache.get(patientId);
        } catch (ExecutionException e) {
            logger.error("Error occurred while getting patient visit count from cache, calculating it again.", e.getMessage());
            visitCountHolder = calculateVisitCountForPatient(patientId);
        }
        long visitCount = Optional.ofNullable(retrieveVisitLimitFunction.apply(visitCountHolder)
                .get(planId))
                .orElse(0L); // plan id not in usage map means this user haven't use the plan in given time period

        if (((retrieveFunction != null
                && visitCount >= retrieveFunction.apply(plan).getVisits())
        )) {
            return PLAN_LIMIT_EXCEEDED;
        }


        fullAvailableLimit = timeConstraint == null ? plan.getCapPerYear().getLimit() : retrieveFunction.apply(plan).getLimit();

        int currentAvailable = fullAvailableLimit - (allPaidAmount + pendingAmount);
        // if the plan's limit has exceeded then return (-1)
        return currentAvailable > 0 ? currentAvailable : PLAN_LIMIT_EXCEEDED;
    }

    private Map<String, CoveragePlan> getCoverageUsageLimitsForPlans(String patientId) {
        List<AttachedMedicalCoverage> allMedicalCoverages = new ArrayList<>();
        Optional.ofNullable(caseRepository.findCasesByPatientId(patientId))
                .orElse(Collections.emptyList())
                .forEach(caseForPatient -> allMedicalCoverages.addAll(caseForPatient.getAttachedMedicalCoverages()));
        return allMedicalCoverages
                .stream()
                .map(attachedCoverage -> Optional.ofNullable(medicalCoverageRepository
                        .findMedicalCoverageByPlanId(attachedCoverage.getPlanId(), Status.ACTIVE)))
                .map(optListOfCoverages -> optListOfCoverages.orElse(new ArrayList<>()))
                .flatMap(Collection::stream)
                .flatMap(medicalCoverage -> medicalCoverage.getCoveragePlans().stream())
                .collect(Collectors.toMap(CoveragePlan::getId, coveragePlan -> coveragePlan, (coveragePlan, coveragePlan2) -> coveragePlan));

    }

    private boolean isValidInvoice(Invoice invoice) {
        return Invoice.InvoiceState.DELETED != invoice.getInvoiceState()
                && invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT
                && invoice.getClaim() != null;
    }


    private int getUsageForPlanWithTimeConstraint(List<Invoice> allInvoicesByPatientId,
                                                  LocalDateTime dateConstraint,
                                                  Predicate<Invoice> invoicePredicate,
                                                  Function<Invoice, Integer> invoiceAmountFunction) {
        AtomicInteger paymentDetailsMap = new AtomicInteger(0);
        allInvoicesByPatientId.parallelStream()
                .filter(invoicePredicate)
                .filter(invoice -> (dateConstraint == null || dateConstraint.isBefore(invoice.getInvoiceTime())))
                .peek(invoice -> logger.debug("filtered invoice with claim status: " + invoice.getClaim().getClaimStatus()
                        + " . InvoicedDate : " + invoice.getInvoiceTime().toLocalDate()))
                .forEach(invoice -> paymentDetailsMap.addAndGet(invoiceAmountFunction.apply(invoice)));
        return paymentDetailsMap.get();
    }

    private boolean isPayableClaim(Claim.ClaimStatus status) {
        return !(status == Claim.ClaimStatus.FAILED
                || status == Claim.ClaimStatus.REJECTED
                || status == Claim.ClaimStatus.REJECTED_PERMANENT
                || status == Claim.ClaimStatus.PENDING);
    }

    @AllArgsConstructor
    @Getter
    private class PatientVisitCountHolder {
        private ConcurrentMap<String, Long> thisYearsVisitCounts;
        private ConcurrentMap<String, Long> thisMonthsVisitCounts;
        private ConcurrentMap<String, Long> thisWeeksVisitCounts;
        private ConcurrentMap<String, Long> todaysVisitCounts;

    }

    @AllArgsConstructor
    @Getter
    private class LimitCheckUser {
        private String patientId;
        private String planId;

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof LimitCheckUser
                    && this.getPatientId().equals(((LimitCheckUser) obj).getPatientId())
                    && this.getPlanId().equals(((LimitCheckUser) obj).getPlanId()));
        }
    }
}