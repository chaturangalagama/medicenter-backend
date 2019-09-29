package com.ilt.cms.pm.integration.mapper.clinic;

import com.ilt.cms.api.entity.label.LabelEntity;
import com.ilt.cms.core.entity.label.Label;

public class LabelMapper {

    public static Label mapToCore(LabelEntity labelEntity){
        if(labelEntity == null){
            return null;
        }
        Label label = new Label(labelEntity.getName(), labelEntity.getTemplate());
        return label;
    }

    public static LabelEntity mapToEntity(Label label){
        if(label == null){
            return null;
        }
        LabelEntity labelEntity = new LabelEntity(label.getId(),label.getName(), label.getTemplate());
        return labelEntity;
    }
}
