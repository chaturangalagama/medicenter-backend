package com.ilt.cms.pm.business.service.clinic;

import com.ilt.cms.core.entity.allergy.AllergyGroup;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.patient.Patient;
import com.ilt.cms.core.entity.patient.PatientAllergy;
import com.ilt.cms.database.clinic.AllergyGroupDatabaseService;
import com.ilt.cms.database.clinic.inventory.ItemDatabaseService;
import com.ilt.cms.database.patient.PatientDatabaseService;
import com.lippo.cms.exception.AllergyGroupException;
import com.lippo.commons.util.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AllergyGroupService {

    private static final Logger logger = LoggerFactory.getLogger(AllergyGroupService.class);

    private AllergyGroupDatabaseService allergyGroupDatabaseService;
    private ItemDatabaseService itemDatabaseService;
    private PatientDatabaseService patientDatabaseService;

    public AllergyGroupService(AllergyGroupDatabaseService allergyGroupDatabaseService, ItemDatabaseService itemDatabaseService,
                               PatientDatabaseService patientDatabaseService) {
        this.allergyGroupDatabaseService = allergyGroupDatabaseService;
        this.itemDatabaseService = itemDatabaseService;
        this.patientDatabaseService = patientDatabaseService;
    }


    public List<AllergyGroup> listGroups() {
        logger.info("Listing all allergy groups");
        List<AllergyGroup> groupList = allergyGroupDatabaseService.findAll();
        logger.info("found [" + groupList.size() + "]");
        return groupList;
    }

    public List<String> checkAllergy(String patientId, List<String> drugIds) throws AllergyGroupException {
        List<String> allergyList = new ArrayList<>();
        Optional<Patient> patientOpt = patientDatabaseService.findPatientById(patientId);
        if (!patientOpt.isPresent()) {
            logger.error("Patient id [" + patientId + "] not valid");
            throw new AllergyGroupException(StatusCode.E1005, "Patient ID not valid");
        } else {
            Patient patient = patientOpt.get();
            List<PatientAllergy> allergies = patient.getAllergies();
            if (allergies == null) {
                return Collections.emptyList();
            } else {
                logger.info("Checking allergies for patient [" + patientId + "] drugIds [" + drugIds + "]");
                List<Item> drugs = itemDatabaseService.findItemsByIds(drugIds);

                for (PatientAllergy patientAllergy : patient.getAllergies()) {
                    switch (patientAllergy.getAllergyType()) {
                        case SPECIFIC_DRUG: {
                            Optional<Item> drugId = drugs.stream()
                                    .filter(drug -> drug.getName().equals(patientAllergy.getName())).findFirst();
                            drugId.ifPresent(drug -> allergyList.add(drug.getId()));
                            break;
                        }
                        case NAME_STARTING_WITH: {
                            Optional<Item> drugId = drugs.stream()
                                    .filter(drug -> drug.getName().startsWith(patientAllergy.getName())).findFirst();
                            drugId.ifPresent(drug -> allergyList.add(drug.getId()));
                            break;
                        }
                        case NAME_CONTAINING: {
                            Optional<Item> drugId = drugs.stream()
                                    .filter(drug -> drug.getName().contains(patientAllergy.getName())).findFirst();
                            drugId.ifPresent(drug -> allergyList.add(drug.getId()));
                            break;
                        }
                        case ALLERGY_GROUP: {
                            AllergyGroup allergyGroup = allergyGroupDatabaseService.findFirstByGroupCode(patientAllergy.getName());
                            if (allergyGroup != null) {
                                Optional<String> allergyDrug = allergyGroup.getDrugIds().stream()
                                        .filter(drugIds::contains).findFirst();
                                allergyDrug.ifPresent(drug -> allergyList.add(allergyDrug.get()));
                            } else {
                                logger.error("There is a patient with a group which is not in the system anymore [" + patientAllergy.getName() + "]");
                            }
                            break;
                        }
                        case FOOD:
                        case OTHER:
                            break;
                        default:
                            logger.error("Unknown type [" + patientAllergy.getAllergyType() + "]");
                    }
                }
            }

        }
        return allergyList;
    }


    public AllergyGroup createAllergyGroup(AllergyGroup allergyGroup) throws AllergyGroupException {
        logger.info("Adding a new allergy group code [" + allergyGroup + "]");
        boolean areParametersValid = allergyGroup.areParametersValid();
        if (!areParametersValid) {
            logger.error("Parameters not valid [" + allergyGroup + "]");
            //return new CmsServiceResponse<>(StatusCode.E1002);
            throw new AllergyGroupException(StatusCode.E1002);
        }
        boolean codeExists = allergyGroupDatabaseService.allergyCodeExists(allergyGroup.getGroupCode());
        if (codeExists) {
            logger.error("Allergy code already exists [" + allergyGroup.getGroupCode() + "]");
            //return new CmsServiceResponse<>(StatusCode.E1007);
            throw new AllergyGroupException(StatusCode.E1007);
        } else {
            boolean drugIdExists = validateDrugIds(allergyGroup.getDrugIds());
            if (!drugIdExists) {
                //return new CmsServiceResponse<>(StatusCode.E1005, "Given drug ids does not exists the in the system");
                throw new AllergyGroupException(StatusCode.E1005, "Given drug ids does not exists in the system");
            } else {
                AllergyGroup savedVersion = allergyGroupDatabaseService.save(allergyGroup);
                return savedVersion;
            }
        }
    }

    public AllergyGroup modifyAllergyGroup(String allergyGroupId, AllergyGroup allergyGroup) throws AllergyGroupException {
        logger.info("Modifying group [" + allergyGroup.getGroupCode() + "]");

        AllergyGroup existingGroup = allergyGroupDatabaseService.findOne(allergyGroupId);
        if (existingGroup == null) {
            logger.error("Allergy group [" + allergyGroupId + "] not found");
            //return new CmsServiceResponse<>(StatusCode.E1005, "Given group not found");
            throw new AllergyGroupException(StatusCode.E1005, "Given group not found");
        }
        if (allergyGroupDatabaseService.allergyCodeExists(allergyGroup.getGroupCode(), allergyGroupId)) {
            logger.error("Given new code already exists [" + allergyGroup.getGroupCode() + "]");
            //return new CmsServiceResponse<>(StatusCode.E1005);
            throw new AllergyGroupException(StatusCode.E1005);
        }
        existingGroup.copy(allergyGroup);
        logger.info("saving modified group");
        AllergyGroup savedVersion = allergyGroupDatabaseService.save(existingGroup);
        return savedVersion;

    }

    public String deleteAllergyGroup(String allergyGroupId) {
        logger.info("Deleting group [" + allergyGroupId + "]");
        allergyGroupDatabaseService.delete(allergyGroupId);
        logger.info("group deleted");
        return StatusCode.S0000.name();
    }


    private boolean validateDrugIds(List<String> drugIds) {
        return drugIds.stream().allMatch(drugId -> itemDatabaseService.existsById(drugId));
    }
}
