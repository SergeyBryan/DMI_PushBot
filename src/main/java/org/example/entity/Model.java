package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Model {
    @Id
    Integer modelCode;
    Integer material;
    @Column(name = "model_name")
    String modelName;
    int qty;
}
