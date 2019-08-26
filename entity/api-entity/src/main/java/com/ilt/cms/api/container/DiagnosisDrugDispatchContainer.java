package com.ilt.cms.api.container;


import com.ilt.cms.api.entity.diagnosis.DiagnosisEntity;
import com.ilt.cms.api.entity.item.ItemEntity;
import com.ilt.cms.api.entity.medical.DispatchItemEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisDrugDispatchContainer {

    private List<DiagnosisEntity> diagnosis;
    private List<DispatchContainer> dispatchContainers;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class DispatchContainer {

        private ItemEntity itemEntity;
        private DispatchItemEntity dispatchItemEntity;

    }
}
