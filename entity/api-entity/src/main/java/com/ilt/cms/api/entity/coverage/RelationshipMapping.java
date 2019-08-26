package com.ilt.cms.api.entity.coverage;

import com.ilt.cms.api.entity.common.Relationship;
import com.ilt.cms.api.entity.common.UserId;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RelationshipMapping {

    private UserId holderId;
    private Relationship relationship;

    private String planId;
}
