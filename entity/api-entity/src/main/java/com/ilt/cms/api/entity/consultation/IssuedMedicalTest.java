package com.ilt.cms.api.entity.consultation;

import com.ilt.cms.api.entity.common.UserPaymentOption;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class IssuedMedicalTest {
    private String id;
    private List<IssuedMedicalTestDetail> issuedMedicalTestDetails = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class IssuedMedicalTestDetail {

        private String testId;
        private String suggestedLocation;
        private UserPaymentOption priceAdjustment;
//        private List<FileMetaData> fileMetaData = new ArrayList<>();
    }
}
