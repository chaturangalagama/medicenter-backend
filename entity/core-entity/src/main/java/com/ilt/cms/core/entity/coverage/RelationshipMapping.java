package com.ilt.cms.core.entity.coverage;

import com.ilt.cms.core.entity.UserId;
import com.ilt.cms.core.entity.common.Relationship;

public class RelationshipMapping {

    private UserId holderId;
    private Relationship relationship;

    private String planId;

    public RelationshipMapping() {
    }

    public RelationshipMapping(UserId holderId, Relationship relationship, String planId) {
        this.holderId = holderId;
        this.relationship = relationship;
        this.planId = planId;
    }

    public String getPlanId() {
        return planId;
    }

    public UserId getHolderId() {
        return holderId;
    }

    public Relationship getRelationship() {
        return relationship;
    }
    
    @Override
    public String toString() {
        return "RelationshipMapping{" +
                "holderId=" + holderId +
                ", relationship=" + relationship +
                ", planId='" + planId + '\'' +
                '}';
    }
}
