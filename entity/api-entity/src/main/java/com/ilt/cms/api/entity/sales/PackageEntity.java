package com.ilt.cms.api.entity.sales;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lippo.cms.util.CMSConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PackageEntity {

    public enum PackageStatus {
        ON_GOING, COMPLETED
    }

    private String itemRefId;
    private String code;
    private String name;
    private int packageQty;
    private int purchasePrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime purchaseDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    @DateTimeFormat(pattern = CMSConstant.JSON_DATE_FORMAT_WITH_SECONDS)
    private LocalDateTime expireDate;
    private PackageStatus status;
    private List<DispatchEntity> dispatches = new ArrayList<>();
}
