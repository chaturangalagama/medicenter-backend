package com.ilt.cms.core.entity.consultation;

import com.ilt.cms.core.entity.PersistedObject;
import com.lippo.commons.util.CommonUtils;

public class ConsultationTemplate extends PersistedObject {

    private String name;
    private transient String type;
    private String template;

    public ConsultationTemplate() {
    }

    public ConsultationTemplate(String name, String template) {
        this.name = name;
        this.template = template;
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(name, template);
    }

    public void copy(ConsultationTemplate updateLabel) {
        name = updateLabel.getName();
        template = updateLabel.getTemplate();
    }


    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return "ConsultationTemplate{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", template='" + template + '\'' +
                '}';
    }
}
