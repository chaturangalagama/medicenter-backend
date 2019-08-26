package com.ilt.cms.pm.integration.mapper;

import com.ilt.cms.api.entity.casem.CaseEntity;
import com.ilt.cms.api.entity.casem.CaseEntity.VisitView;
import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Case.CaseStatus;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;

import java.util.stream.Collectors;

public class CaseMapper extends Mapper {

    /**
     * Converting {@link CaseEntity to {@link Case} which converting to received data from rest api to model
     *
     * @param caseEntity
     * @return {@link Case}
     */
    public static Case mapToCore(CaseEntity caseEntity) {
        if (caseEntity == null) return null;
        Case aCase = new Case();
        aCase.setClinicId(caseEntity.getClinicId());
        aCase.setPatientId(caseEntity.getPatientId());
        if (caseEntity.getVisitIds() != null && !caseEntity.getVisitIds().isEmpty()) {
            aCase.setVisitIds(caseEntity.getVisitIds().stream().map(VisitView::getVisitId).collect(Collectors.toList()));
        }
        aCase.setPurchasedPackage(PackageMapper.mapToCore(caseEntity.getPurchasedPackage()));
        aCase.setSalesOrder(SalesOrderMapper.mapToSalesOrder(caseEntity.getSalesOrder()));
        caseEntity.getCoverages().forEach(coverageView -> {
            aCase.getAttachedMedicalCoverages().add(new AttachedMedicalCoverage(coverageView.getPlanId()));
        });
        aCase.setSingleVisit(caseEntity.isSingleVisit());
        return aCase;
    }

    /**
     * Converting {@link Case to {@link CaseEntity} which converting to send data from services to rest caller
     *
     * @param aCase
     * @return {@link CaseEntity}
     */
    public static CaseEntity mapToEntity(Case aCase) {
        CaseEntity caseEntity = new CaseEntity();
        if (aCase == null) return caseEntity;
        caseEntity.setCaseId(aCase.getId());
        caseEntity.setCaseNumber(aCase.getCaseNumber());
        if (CaseStatus.OPEN.equals(aCase.getStatus())) {
            caseEntity.setStatus(CaseEntity.CaseStatus.OPEN);
        } else if (CaseStatus.CLOSED.equals(aCase.getStatus())) {
            caseEntity.setStatus(CaseEntity.CaseStatus.CLOSED);
        }
        caseEntity.setSingleVisit(aCase.isSingleVisit());
        caseEntity.setCreatedDate(aCase.getCreatedDate());
        caseEntity.setClinicId(aCase.getClinicId());
        caseEntity.setPatientId(aCase.getPatientId());
        caseEntity.setPurchasedPackage(PackageMapper.mapToEntity(aCase.getPurchasedPackage()));
        caseEntity.setSalesOrder(SalesOrderMapper.mapToSalesOrderEntity(aCase.getSalesOrder()));
        return caseEntity;
    }
}
