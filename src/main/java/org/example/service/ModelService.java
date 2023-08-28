package org.example.service;

import org.example.entity.Model;
import org.example.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {
    private final ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }


    public Model getModel(Integer modelCode) {
        return modelRepository.findById(modelCode).orElse(null);
    }

    public List<Integer> getModelCodes() {
        return modelRepository.getModelCodes();
    }


}
