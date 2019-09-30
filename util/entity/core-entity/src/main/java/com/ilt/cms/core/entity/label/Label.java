package com.ilt.cms.core.entity.label;

import com.ilt.cms.core.entity.PersistedObject;
import com.lippo.commons.util.CommonUtils;
import org.springframework.data.mongodb.core.index.Indexed;

public class Label extends PersistedObject {

    @Indexed(unique = true)
    private String name;
    private String template;

    public Label() {
    }

    public Label(String name, String template) {
        this.name = name;
        this.template = template;
    }

    public boolean areParametersValid() {
        return CommonUtils.isStringValid(name, template);
    }

    public void copy(Label updateLabel) {
        name = updateLabel.getName();
        template = updateLabel.getTemplate();
    }


    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    @Override
    public String toString() {
        return "Label{" +
                "name='" + name + '\'' +
                ", template='" + template + '\'' +
                '}';
    }
}
