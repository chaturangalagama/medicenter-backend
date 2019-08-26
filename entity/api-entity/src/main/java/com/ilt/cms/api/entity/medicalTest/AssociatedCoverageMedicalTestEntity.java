package com.ilt.cms.api.entity.medicalTest;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class AssociatedCoverageMedicalTestEntity {

    private String id;
    private String medicalCoverageId;
    private String coveragePlanId;
    private List<MedicalTestSchemeEntity> modifiedMedicalTestScheme;

    private List<MedicalTestSchemeEntity> excludedMedicalTestScheme;

    public AssociatedCoverageMedicalTestEntity() {
        modifiedMedicalTestScheme = new ArrayList<>();
        excludedMedicalTestScheme = new ArrayList<>();
    }


    public AssociatedCoverageMedicalTestEntity(String medicalCoverageId, String coveragePlanId) {
        this();
        this.medicalCoverageId = medicalCoverageId;
        this.coveragePlanId = coveragePlanId;
    }

    public void clearFieldsForPersistence() {
        id = null;
    }

    /**
     * When adding drugs, the scheme is not valid because before persistence schemes are validated.
     * So make sure to use proper Drug schemes
     *
     * @param medicalTestScheme : Drug Scheme to add
     */
    public void addModifiedMedicalTestSchemeEntity(MedicalTestSchemeEntity medicalTestScheme) {
        modifiedMedicalTestScheme.add(medicalTestScheme);
    }

    /**
     * When adding drugs, the scheme is not valid because before persistence schemes are validated.
     * So make sure to use proper Drug schemes
     *
     * @param medicalTestScheme : Drug Scheme to add
     */
    public void addExcludedMedicalTestSchemeEntity(MedicalTestSchemeEntity medicalTestScheme) {
        excludedMedicalTestScheme.add(medicalTestScheme);

    }

}
