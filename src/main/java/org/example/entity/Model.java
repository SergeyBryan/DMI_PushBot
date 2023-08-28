package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Model {
    @Id
    @Column(name = "model_code")
    private Integer modelCode;
    private Integer material;
    @Column(name = "model_name")
    private String modelName;
    private int qty;
}
