package org.example.service;

import org.example.entity.Model;
import org.example.repository.ModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {
    Logger logger = LoggerFactory.getLogger(ModelService.class);

    ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public void create() {
        Model model = new Model();
        model.setModelCode(8001000);
        model.setModelName("B100");
        model.setQty(40);
        model.setMaterial(1748341);
        modelRepository.save(model);
    }

    public Model getModel(Integer modelCode) {
        return modelRepository.findById(modelCode).orElse(null);
    }



}
