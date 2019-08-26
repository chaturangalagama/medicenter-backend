package com.ilt.cms.db.service;

import com.ilt.cms.core.entity.label.Label;
import com.ilt.cms.database.label.LabelDatabaseService;
import com.ilt.cms.repository.spring.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoLabelDatabaseService implements LabelDatabaseService {

    private LabelRepository labelRepository;

    public MongoLabelDatabaseService(LabelRepository labelRepository){
        this.labelRepository = labelRepository;
    }
    @Override
    public boolean checkNameExists(String name) {
        return labelRepository.checkNameExists(name);
    }

    @Override
    public Label findByName(String name) {
        return labelRepository.findByName(name);
    }

    @Override
    public List<Label> findAll() {
        return labelRepository.findAll();
    }

    @Override
    public Label save(Label label) {
        return labelRepository.save(label);
    }

    @Override
    public Label findOne(String labelId) {
        return labelRepository.findById(labelId).orElse(null);
    }
}
