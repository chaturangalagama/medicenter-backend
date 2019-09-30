package com.ilt.cms.api.container;

import com.ilt.cms.api.entity.consultation.ConsultationEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationWrapper {

    private ConsultationEntity consultationEntity;
    private List<String> diagnosisIds;
}
