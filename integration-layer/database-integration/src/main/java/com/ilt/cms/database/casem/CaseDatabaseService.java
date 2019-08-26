package com.ilt.cms.database.casem;

import com.ilt.cms.core.entity.casem.Case;
import com.ilt.cms.core.entity.casem.Dispatch;
import com.ilt.cms.core.entity.casem.Package;
import com.lippo.cms.container.CaseSearchParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CaseDatabaseService {

    List<Case> getAll();

    List<Case> getAll(String clinicId);

    List<Case> getAll(String clinicId, CaseSearchParams sp);

    Page<Case> getAll(String clinicId, int page, int size, Sort.Direction direction, String... sortBy);

    Case save(Case aCase);

    Case findByCaseId(String caseId);

    void addNewVisitToCase(String caseId, String visitId);

    void remove(String caseId);

    boolean exists(String caseId);

    boolean existsAndActive(String caseId);

    Case getCasePackage(String caseId);

    boolean updateCasePackage(String caseId, Package aPackage);

    Case getCaseDispatch(String caseId);

    boolean updateCaseDispatch(String caseId, Dispatch dispatch);

    boolean closeCase(String caseId);

}
