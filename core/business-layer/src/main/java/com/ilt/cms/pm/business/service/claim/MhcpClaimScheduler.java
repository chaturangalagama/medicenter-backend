package com.ilt.cms.pm.business.service.claim;

import com.ilt.cms.core.entity.casem.Claim;
import com.ilt.cms.core.entity.casem.Invoice;
import com.ilt.cms.core.entity.claim.ClaimViewCore;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.repository.spring.CaseRepositoryImpl;
import com.ilt.cms.repository.spring.SalesOrderRepository;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.security.authentication.AnonymousAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MhcpClaimScheduler implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(MhcpClaimScheduler.class);
    private final ExecutorService executorService;
    private SalesOrderRepository salesOrderRepository;
    private MedicalCoverageRepository medicalCoverageRepository;
    private CaseRepositoryImpl caseRepositoryCustom;
    private ClaimService claimService;
    private int claimTps = 5;
    private int checkStatusTps = 1;

    @Value("#{'${auto.submit.medical.coverage.names:MEDISAVE,CHAS}'.split(',')}")
    private List<String> autoSubmitMedicalCoverageNameList;

    public MhcpClaimScheduler(SalesOrderRepository salesOrderRepository,
                              MedicalCoverageRepository medicalCoverageRepository,
                              CaseRepositoryImpl caseRepositoryCustom,
                              ClaimService claimService) {

        this.salesOrderRepository = salesOrderRepository;
        this.medicalCoverageRepository = medicalCoverageRepository;
        this.caseRepositoryCustom = caseRepositoryCustom;
        this.claimService = claimService;
        executorService = Executors.newFixedThreadPool(16);
    }

//    @Scheduled(cron = "0 0/10 * * * *")
    public void submitMhcpClaim() throws InterruptedException {
        logger.info("Executing Claims Submit scheduler");
        LocalDate now = LocalDate.now();
        LocalDateTime start = now.atStartOfDay();
        LocalDateTime end = now.plusDays(1).atStartOfDay();
        List<String> claimIds = salesOrderRepository
                .listClaimForSubmission(createAutoSubmitPlanIdList(), start, end)
                .stream()
                .filter(salesOrder -> salesOrder.getInvoices() != null && salesOrder.getInvoices().size() > 0)
                .flatMap(salesOrder -> salesOrder.getInvoices().stream())
                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT && invoice.getClaim() != null)
                .map(Invoice::getClaim)
                .map(Claim::getClaimId)
                .collect(Collectors.toList());

        for (String claimId : claimIds) {
            executorService.submit(() -> {
                try {
                    logger.info("submitting claim id [" + claimId + "]");

                    ClaimViewCore submitClaim = claimService.submitClaim(claimId, null);

                    logger.info("Claim status [" + submitClaim.getClaimStatus() + "]");
                    if (submitClaim.getClaimStatus() != Claim.ClaimStatus.SUBMITTED) {
                        logger.error("Error making claim [" + claimId + "] status[" + submitClaim.getClaimStatus() + "] " +
                                "message[" + submitClaim.getClaim().getSubmissionResult().getStatusDescription() + "]");
                    }
                } catch (Exception e) {
                    logger.error("Error submitting request : ", e);
                }
            });
            Thread.sleep(1000 / claimTps);
        }

    }

    private List<String> createAutoSubmitPlanIdList() {
        return medicalCoverageRepository
                .findMedicalCoveragesByNameIn(autoSubmitMedicalCoverageNameList)
                .parallelStream()
                .filter(medicalCoverage -> medicalCoverage.getCoveragePlans() != null)
                .flatMap(medicalCoverage -> medicalCoverage.getCoveragePlans().stream())
                .map(CoveragePlan::getId)
                .collect(Collectors.toList());
    }


    @Scheduled(cron = "0 0/13 * * * *")
    public void mhcpApprovalStatusCheck() {
        logger.info("Executing Approval status check for claims");
        LocalDateTime invoiceDateCheckLimit = LocalDate.now().plusDays(1).atStartOfDay();
        List<Claim> claimList = salesOrderRepository.listClaimForStatusCheck(createAutoSubmitPlanIdList(),
                invoiceDateCheckLimit, Collections.singletonList(Claim.ClaimStatus.SUBMITTED))
                .parallelStream()
                .filter(salesOrder -> salesOrder.getInvoices() != null && salesOrder.getInvoices().size() > 0)
                .flatMap(salesOrder -> salesOrder.getInvoices().stream())
                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT && invoice.getClaim() != null)
                .filter(invoice -> invoice.getInvoiceTime().isBefore(invoiceDateCheckLimit))
                .map(Invoice::getClaim)
                .collect(Collectors.toList());
        mhcpClaimStatusUpdate(claimList);
    }

    @Scheduled(cron = "0 0 6,18 * * *")
    public void mhcpPaidStatusCheck() {
        logger.info("Executing Paid status check for claims");
        LocalDateTime invoiceDateCheckLimit = LocalDate.now().minusDays(5).atStartOfDay();
        List<Claim> claimList = salesOrderRepository.listClaimForStatusCheck(autoSubmitMedicalCoverageNameList,
                invoiceDateCheckLimit, Arrays.asList(Claim.ClaimStatus.APPROVED, Claim.ClaimStatus.APPEALED))
                .parallelStream()
                .filter(salesOrder -> salesOrder.getInvoices() != null && salesOrder.getInvoices().size() > 0)
                .flatMap(salesOrder -> salesOrder.getInvoices().stream())
                .filter(invoice -> invoice.getInvoiceType() == Invoice.InvoiceType.CREDIT && invoice.getClaim() != null)
                .filter(invoice -> invoice.getInvoiceTime().isBefore(invoiceDateCheckLimit))
                .map(Invoice::getClaim)
                .collect(Collectors.toList());
        mhcpClaimStatusUpdate(claimList);
    }


    public void mhcpClaimStatusUpdate(List<Claim> claimList) {

        logger.debug("Found [" + claimList.size() + "] for status check");
        Map<String, List<String>> claimByClinic = new HashMap<>();

        claimList.forEach(claim -> {
            String clinicId = caseRepositoryCustom.findClinicIdByClaimId(claim.getClaimId());
            claimByClinic.computeIfPresent(clinicId, (clinicIdForClaim, claims) -> {
                Claim.SubmissionResult submissionResult = claim.getSubmissionResult();
                if (submissionResult != null) {
                    claims.add(submissionResult.getClaimNo());
                }
                return claims;
            });
        });
        int maxBatchNumber = 100;

//        Authentication auth = new AnonymousAuthenticationToken("system", "system",
//                Collections.singletonList(new SimpleGrantedAuthority("ANONYMOUS")));

        for (Map.Entry<String, List<String>> entry : claimByClinic.entrySet()) {
            IntStream.range(0, (entry.getValue().size() + maxBatchNumber - 1) / maxBatchNumber)
                    .mapToObj(i -> entry.getValue().subList(i * maxBatchNumber, Math.min(entry.getValue().size(), (i + 1) * maxBatchNumber)))
                    .forEach(claimBatch -> {
                        executorService.submit(() -> {
//                            SecurityContextHolder.getContext().setAuthentication(auth);
                            try {
                                logger.info("checking status for batch size of [" + claimBatch.size() + "]");
                                claimService.checkClaimStatus(entry.getKey(), claimBatch);
                            } catch (Exception e) {
                                logger.error("Error submitting request : ", e);
                            }
                        });
                        try {
                            Thread.sleep(1000 / checkStatusTps);
                        } catch (InterruptedException e) {
                            logger.error("Error sleeping status check thread : ", e);
                        }
                    });
        }
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.warn("Shutting down reminder scheduler : [" + event + "]");
        executorService.shutdown();
    }
}
