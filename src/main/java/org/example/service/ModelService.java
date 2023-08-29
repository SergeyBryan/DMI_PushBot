package org.example.service;

import org.example.entity.Model;
import org.example.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * The ModelService class is a service that provides methods for retrieving and manipulating Model objects.
 */
@Service
public class ModelService {
    private final ModelRepository modelRepository;
    /**
     * Constructor for the ModelService class.
     * @param modelRepository The ModelRepository instance for accessing the models in the database.
     */
    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    /**
     * Retrieves a Model object with the specified model code.
     * @param modelCode The code of the model to retrieve.
     * @return The Model object with the specified model code, or null if not found.
     */
    public Model getModel(Integer modelCode) {
        return modelRepository.findById(modelCode).orElse(null);
    }
    /**
     * Retrieves a list of all model codes.
     * @return The list of all model codes.
     */
    public List<Integer> getModelCodes() {
        return modelRepository.getModelCodes();
    }


}
