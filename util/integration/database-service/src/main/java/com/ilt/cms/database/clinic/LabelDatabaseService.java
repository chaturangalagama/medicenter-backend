package com.ilt.cms.database.clinic;

import com.ilt.cms.core.entity.label.Label;

import java.util.List;

public interface LabelDatabaseService {
    boolean checkNameExists(String name);

    Label findByName(String name);

    List<Label> findAll();

    Label save(Label label);

    Label findOne(String labelId);
}
