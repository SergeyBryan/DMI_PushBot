package org.example.repository;

import org.example.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {
    @Query(nativeQuery = true, value = "SELECT model_code from model")
    List<Integer> getModelCodes();

}
