package com.ilt.cms.db.service;

import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Case.CaseStatus;
import com.ilt.cms.core.entity.casem.Dispatch;
import com.ilt.cms.core.entity.casem.Package;
import com.ilt.cms.database.casem.CaseDatabaseService;
import com.ilt.cms.repository.spring.CaseRepository;
import com.lippo.cms.container.CaseSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MongoCaseDatabaseService implements CaseDatabaseService {

    private CaseRepository caseRepository;
    private MongoTemplate mongoTemplate;

    public MongoCaseDatabaseService(CaseRepository caseRepository, MongoTemplate mongoTemplate) {
        this.caseRepository = caseRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Case> getAll() {
        return caseRepository.findAll();
    }

    @Override
    public List<Case> getAll(String clinicId) {
        return caseRepository.findByClinicId(clinicId);
    }

    @Override
    public List<Case> getAll(String clinicId, CaseSearchParams sp) {
        Criteria criteria = new Criteria();
        Query query = new Query();
        Criteria criteriaDate = new Criteria();
        Criteria criteriaStatus = new Criteria();
        Criteria criteriaCaseId = new Criteria();

        String caseNumber = sp.getCaseNumber();
        String status = sp.getStatus();
        Date fromDate = sp.getFromDate();
        Date toDate = sp.getToDate();

        if (fromDate != null) {
            if (toDate == null) {
                toDate = new Date();
            }
            criteriaDate = Criteria.where("createdDate").gte(fromDate).lte(toDate);
        }
        if (CaseStatus.OPEN.name().equals(status)) {
            criteriaStatus = Criteria.where("status").is(CaseStatus.OPEN);
        } else if (CaseStatus.CLOSED.name().equals(status)) {
            criteriaStatus = Criteria.where("status").is(CaseStatus.CLOSED);
        } else {
            status = null;
        }
        if (caseNumber != null && !caseNumber.trim().isEmpty()) {
            criteriaCaseId = Criteria.where("caseNumber").is(caseNumber);
        } else {
            caseNumber = null;
        }

        if (fromDate != null && caseNumber != null && status != null) {
            criteria.andOperator(criteriaCaseId, criteriaDate, criteriaStatus);
            query.addCriteria(criteria);
        } else if (caseNumber != null && status != null) {
            criteria.andOperator(criteriaCaseId, criteriaStatus);
            query.addCriteria(criteria);
        } else if (caseNumber != null && fromDate != null) {
            criteria.andOperator(criteriaDate, criteriaCaseId);
            query.addCriteria(criteria);
        } else if (status != null && fromDate != null) {
            criteria.andOperator(criteriaDate, criteriaStatus);
            query.addCriteria(criteria);
        } else if (caseNumber != null) {
            query.addCriteria(criteriaCaseId);
        } else if (status != null) {
            query.addCriteria(criteriaStatus);
        } else if (fromDate != null) {
            query.addCriteria(criteriaDate);
        }
        query.addCriteria(Criteria.where("clinicId").is(clinicId));
        query.with(Sort.by("caseNumber"));

        return mongoTemplate.find(query, Case.class);
    }

    @Override
    public Page<Case> getAll(String clinicId, int page, int size, Direction direction, String... sortBy) {
        return caseRepository.findAll(PageRequest.of(page, size, direction, sortBy));
    }

    @Override
    public Case save(Case aCase) {
        return caseRepository.save(aCase);
    }

//    @Override
//    public boolean update(Case visitCase) {
//        Update update = new Update();
//        update.set("patientId", visitCase.getPatientId())
//                .set("clinicId", visitCase.getClinicId())
//                .set("visitIds", visitCase.getVisitIds())
//                .set("status", visitCase.getStatus());
//        return mongoTemplate
//                .upsert(Query.query(Criteria.where("id").is(visitCase.getId())), update, Case.class)
//                .isModifiedCountAvailable();
//    }

    @Override
    public boolean exists(String caseId) {
        return caseRepository.existsById(caseId);
    }

    @Override
    public Case findByCaseId(String caseId) {
        return caseRepository.findById(caseId).orElse(null);
    }

    @Override
    public void addNewVisitToCase(String caseId, String visitId) {
        mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(caseId)),
                new Update().push("visitIds", visitId), Case.class);
    }

    @Override
    public void remove(String caseId) {
        caseRepository.deleteById(caseId);
    }

    @Override
    public boolean existsAndActive(String caseId) {
        return caseRepository.existsByIdAndStatus(caseId, Case.CaseStatus.OPEN);
    }

    @Override
    public Case getCasePackage(String caseId) {
        Query query = new Query(Criteria.where("id").is(caseId));
        query.fields().include("purchasedPackage");
        return mongoTemplate.findOne(query, Case.class);
    }

    @Override
    public boolean updateCasePackage(String caseId, Package aPackage) {
        Update update = new Update().set("purchasedPackage", aPackage);
        return mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(caseId)), update, Case.class)
                .isModifiedCountAvailable();
    }

    @Override
    public Case getCaseDispatch(String caseId) {
        Query query = new Query(Criteria.where("id").is(caseId));
        query.fields().include("dispatch");
        return mongoTemplate.findOne(query, Case.class);
    }

    @Override
    public boolean updateCaseDispatch(String caseId, Dispatch dispatch) {
        Update update = new Update().set("dispatch", dispatch);
        return mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(caseId)), update, Case.class)
                .isModifiedCountAvailable();
    }

    @Override
    public boolean closeCase(String caseId) {
        return mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(caseId)),
                Update.update("status", CaseStatus.CLOSED), Case.class).isModifiedCountAvailable();
    }
}
