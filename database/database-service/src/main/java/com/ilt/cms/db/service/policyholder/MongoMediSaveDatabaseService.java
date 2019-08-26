package com.ilt.cms.db.service.policyholder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import com.ilt.cms.database.policyholder.MediSaveDatabaseService;
import com.ilt.cms.repository.spring.policy.PolicyHolderMediSaveRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoMediSaveDatabaseService implements MediSaveDatabaseService {

    private PolicyHolderMediSaveRepository policyHolderMediSaveRepository;

    public MongoMediSaveDatabaseService(PolicyHolderMediSaveRepository policyHolderMediSaveRepository){
        this.policyHolderMediSaveRepository = policyHolderMediSaveRepository;
    }
    @Override
    public int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status) {
        return policyHolderMediSaveRepository.countAllByMedicalCoverageIdAndStatus(medicalCoverageId, status);
    }

    @Override
    public List<PolicyHolder.PolicyHolderMediSave> findByIdentificationNumber(UserId identificationNumber) {
        return policyHolderMediSaveRepository.findByIdentificationNumber(identificationNumber);
    }

    @Override
    public PolicyHolder.PolicyHolderMediSave save(PolicyHolder.PolicyHolderMediSave policyHolderMediSave) {
        return policyHolderMediSaveRepository.save(policyHolderMediSave);
    }

    @Override
    public boolean delete(String holderId) {
        policyHolderMediSaveRepository.deleteById(holderId);
        return true;
    }

    @Override
    public PolicyHolder.PolicyHolderMediSave findOne(String holderId) {
        return policyHolderMediSaveRepository.findById(holderId).orElse(null);
    }

    @Override
    public List<PolicyHolder.PolicyHolderMediSave> findAllByIdentificationNumberAndStatus(UserId userId, Status status) {
        return policyHolderMediSaveRepository.findAllByIdentificationNumberAndStatus(userId, status);
    }

    @Override
    public boolean exists(PolicyHolder.PolicyHolderMediSave mediSave) {
        return policyHolderMediSaveRepository.exists(Example.of(mediSave));
    }
}
