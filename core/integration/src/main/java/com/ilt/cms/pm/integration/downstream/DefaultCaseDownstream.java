package com.ilt.cms.pm.integration.downstream;

import com.ilt.cms.api.container.PageableHttpApiResponse;
import com.ilt.cms.api.entity.casem.CaseEntity;
import com.ilt.cms.api.entity.casem.CaseEntity.VisitView;
import com.ilt.cms.api.entity.casem.SalesOrderEntity;
import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Package;
import com.ilt.cms.core.entity.casem.SalesOrder;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.visit.PatientVisitRegistry;
import com.ilt.cms.downstream.CaseDownstream;
import com.ilt.cms.pm.business.service.billing.PriceCalculationService;
import com.ilt.cms.pm.business.service.clinic.ClinicService;
import com.ilt.cms.pm.business.service.patient.CaseService;
import com.ilt.cms.pm.business.service.patient.PatientService;
import com.ilt.cms.pm.business.service.patient.PatientVisitService;
import com.ilt.cms.pm.integration.mapper.CaseMapper;
import com.ilt.cms.pm.integration.mapper.PackageMapper;
import com.ilt.cms.pm.integration.mapper.SalesOrderMapper;
import com.lippo.cms.container.CaseSearchParams;
import com.lippo.cms.exception.CMSException;
import com.lippo.commons.web.api.ApiResponse;
import com.lippo.commons.web.api.HttpApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lippo.commons.web.CommonWebUtil.httpApiResponse;

/**
 * The downstream layer implementation of {@link CaseDownstream} integration with rest-layer to business-layer,
 * to generate the {@link ApiResponse} as the http response for the requested service from rest-layer controller.
 */
@Service
public class DefaultCaseDownstream implements CaseDownstream {

    public static final Logger logger = LoggerFactory.getLogger(DefaultCaseDownstream.class);

    private CaseService caseService;
    private PatientService patientService;
    private PatientVisitService patientVisitService;
    private ClinicService clinicService;
    private PriceCalculationService priceCalculationService;


    public DefaultCaseDownstream(CaseService caseService, PatientService patientService, PatientVisitService patientVisitService,
                                 ClinicService clinicService,
                                 PriceCalculationService priceCalculationService) {
        this.caseService = caseService;
        this.patientService = patientService;
        this.patientVisitService = patientVisitService;
        this.clinicService = clinicService;
        this.priceCalculationService = priceCalculationService;
    }

    @Override
    public ResponseEntity<ApiResponse> listAll() {
        List<CaseEntity> caseEntities = caseService.findAllCases()
                .parallelStream()
                .map(this::collectData)
                .collect(Collectors.toList());
        return httpApiResponse(new HttpApiResponse(caseEntities));
    }

    @Override
    public ResponseEntity<ApiResponse> listAll(String clinicId, CaseSearchParams sp) {
        try {
            List<CaseEntity> caseEntities;
            if (sp != null && !sp.isParametersNull()) {
                caseEntities = caseService.findAllCases(clinicId, sp)
                        .parallelStream()
                        .map(this::collectData)
                        .collect(Collectors.toList());
            } else {
                caseEntities = caseService.findAllCases(clinicId)
                        .parallelStream()
                        .map(this::collectData)
                        .collect(Collectors.toList());
            }
            return httpApiResponse(new HttpApiResponse(caseEntities));
        } catch (CMSException e) {
            logger.error("Error found [{}]:[{}]", e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> listAll(String clinicId, int page, int size, CaseSearchParams sp) {
        try {
            List<CaseEntity> caseEntities;
            if (sp != null && !sp.isParametersNull()) {
                List<Case> allCases = caseService.findAllCases(clinicId, sp);
                int totalElements = allCases.size();
                int nonFragmentPages = totalElements / size;
                int lastPageElements = totalElements % size;
                int totalPages = lastPageElements != 0 ? nonFragmentPages + 1 : nonFragmentPages;

                int fromIndex = 0;
                int toIndex;

                if (nonFragmentPages >= page) {
                    fromIndex = page * size;
                    toIndex = getToIndex(size, totalElements, lastPageElements, fromIndex);
                } else {
                    toIndex = getToIndex(size, totalElements, lastPageElements, fromIndex);
                    page = 0;
                }

                allCases = allCases.subList(fromIndex, toIndex);
                caseEntities = allCases
                        .parallelStream()
                        .map(this::collectData)
                        .collect(Collectors.toList());
                return httpApiResponse(new PageableHttpApiResponse(totalElements, totalPages, page, caseEntities));
            } else {
                Page<Case> allCases = caseService.findAllCases(clinicId, page, size);
                caseEntities = allCases.getContent()
                        .parallelStream()
                        .map(this::collectData)
                        .collect(Collectors.toList());
                return httpApiResponse(new PageableHttpApiResponse((int) allCases.getTotalElements(), allCases.getTotalPages(), allCases.getNumber(), caseEntities));
            }
        } catch (CMSException e) {
            logger.error("Error found [{}]:[{}]", e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    private CaseEntity collectData(Case aCase) {

        CaseEntity caseEntity = CaseMapper.mapToEntity(aCase);

        Patient patient = patientService.findPatientById(aCase.getPatientId());
        if (patient != null) {
            caseEntity.setPatientNRIC(patient.getUserId() != null ? patient.getUserId().getNumber() : null);
            caseEntity.setPatientName(patient.getName());
        }
        List<PatientVisitRegistry> visitRegistries = patientVisitService.searchByIds(aCase.getVisitIds());

        Map<String, Clinic> clinics = clinicService.searchById(visitRegistries.stream()
                .map(PatientVisitRegistry::getClinicId)
                .collect(Collectors.toList()));

        for (PatientVisitRegistry visitRegistry : visitRegistries) {
            Clinic clinic = clinics.get(visitRegistry.getClinicId());
            caseEntity.getVisitIds().add(new VisitView(
                    visitRegistry.getVisitNumber(),
                    clinic == null ? "": clinic.getName(),
                    visitRegistry.getStartTime()));
        }

        return caseEntity;
    }

    private int getToIndex(int size, int totalElements, int lastPageElements, int fromIndex) {
        int toIndex;
        if ((totalElements - fromIndex) >= size) {
            toIndex = fromIndex + size;
        } else {
            toIndex = fromIndex + lastPageElements;
        }
        return toIndex;
    }

    @Override
    public ResponseEntity<ApiResponse> findByCaseId(String caseId) {
        try {
            Case aCase = caseService.findByCaseId(caseId);
            return httpApiResponse(new HttpApiResponse(collectData(aCase)));
        } catch (CMSException e) {
            logger.error("Case find error [{}]:[{}]", e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> createCase(CaseEntity caseEntity) {
        try {
            Case aCase = caseService.createCase(CaseMapper.mapToCore(caseEntity));
            return httpApiResponse(new HttpApiResponse(collectData(aCase)));
        } catch (CMSException e) {
            logger.error("Case create error [{}]:[{}]", e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateCase(String caseId, CaseEntity caseEntity) {
        try {
            Case aCase = caseService.changeCase(caseId, CaseMapper.mapToCore(caseEntity));
            return httpApiResponse(new HttpApiResponse(collectData(aCase)));
        } catch (CMSException e) {
            logger.error("Case update error [{}]:[{}]", e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getCasePackage(String caseId) {
        try {
            Package aPackage = caseService.getCasePackage(caseId);
            return httpApiResponse(new HttpApiResponse(PackageMapper.mapToEntity(aPackage)));
        } catch (CMSException e) {
            logger.error("Package find error in case id [{}]:[{}]:[{}]", caseId, e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateCasePackage(String caseId, String packageItemId) {
        try {
            Case aCase = caseService.addPurchasedPackage(caseId, packageItemId);
            return httpApiResponse(new HttpApiResponse(collectData(aCase)));
        } catch (CMSException e) {
            logger.error("Package update error in case id [{}]:[{}]:[{}]", caseId, e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getSalesOrder(String caseId) {
        try {
            SalesOrder salesOrder = caseService.getSalesOrder(caseId);
            return httpApiResponse(new HttpApiResponse(SalesOrderMapper.mapToSalesOrderEntity(salesOrder)));
        } catch (CMSException e) {
            logger.error("Sales Order search error in case id [{}]:[{}]:[{}]", caseId, e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateSalesOrder(String caseId, SalesOrderEntity salesOrderEntity) {
        try {
            SalesOrder salesOrder = caseService.updateSalesOrder(caseId, SalesOrderMapper.mapToSalesOrder(salesOrderEntity));
            return httpApiResponse(new HttpApiResponse(SalesOrderMapper.mapToSalesOrderEntity(salesOrder)));
        } catch (CMSException e) {
            logger.error("Sales order update error in case id [{}]:[{}]:[{}]", caseId, e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> closeCase(String caseId) {
        try {
            Case aCase = caseService.closeCase(caseId);
            return httpApiResponse(new HttpApiResponse(collectData(aCase)));
        } catch (CMSException e) {
            logger.error("Case state update error in case id [{}]:[{}]:[{}]", caseId, e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateCaseSingleVisit(String caseId, String state) {
        try {
            Case aCase = caseService.updateCaseSingleVisit(caseId, state);
            return httpApiResponse(new HttpApiResponse(collectData(aCase)));
        } catch (CMSException e) {
            logger.error("Case state update error in case id [{}]:[{}]:[{}]", caseId, e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getItemChangePriceCalculation(String caseId, ItemChargeDetail.ItemChargeRequest itemPriceRequests) {
        try {
            return httpApiResponse(new HttpApiResponse(priceCalculationService.calculateSalesPrice(caseId, itemPriceRequests)));
        } catch (CMSException e) {
            logger.error("Case error in case id [{}]:[{}]:[{}]", caseId, e.getStatusCode(), e.getMessage());
            return httpApiResponse(new HttpApiResponse(e.getStatusCode(), e.getMessage()));
        }
    }

}