package com.ilt.cms.db.service.policyholder;

import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.coverage.PolicyHolder;
import com.ilt.cms.database.policyholder.ChasDatabaseService;
import com.ilt.cms.repository.spring.policy.PolicyHolderChasRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoChasDatabaseService implements ChasDatabaseService {

    private PolicyHolderChasRepository policyHolderChasRepository;

    public MongoChasDatabaseService(PolicyHolderChasRepository policyHolderChasRepository){
        this.policyHolderChasRepository = policyHolderChasRepository;
    }
    @Override
    public int countAllByMedicalCoverageIdAndStatus(String medicalCoverageId, Status status) {
        return policyHolderChasRepository.countAllByMedicalCoverageIdAndStatus(medicalCoverageId, status);
    }

    @Override
    public List<PolicyHolder.PolicyHolderChas> findByIdentificationNumber(UserId identificationNumber) {
        return policyHolderChasRepository.findByIdentificationNumber(identificationNumber);
    }

    @Override
    public PolicyHolder.PolicyHolderChas save(PolicyHolder.PolicyHolderChas policyHolderChas) {
        return policyHolderChasRepository.save(policyHolderChas);
    }

    @Override
    public boolean delete(String holderId) {
        policyHolderChasRepository.deleteById(holderId);
        return true;
    }

    @Override
    public PolicyHolder.PolicyHolderChas findOne(String holderId) {
        return policyHolderChasRepository.findById(holderId).orElse(null);
    }

    @Override
    public List<PolicyHolder.PolicyHolderChas> findAllByIdentificationNumberAndStatus(UserId identificationNumber, Status status) {
        return policyHolderChasRepository.findAllByIdentificationNumberAndStatus(identificationNumber, status);
    }

    @Override
    public boolean exists(PolicyHolder.PolicyHolderChas chas) {
        return policyHolderChasRepository.exists(Example.of(chas));
    }
}
