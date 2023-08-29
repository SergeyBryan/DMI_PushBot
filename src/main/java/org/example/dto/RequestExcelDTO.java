package org.example.dto;

import com.ebay.xcelite.annotations.Column;
import com.ebay.xcelite.annotations.Row;
import lombok.Data;

/**
 * Data Transfer Object class for representing a request in Excel format.
 */
@Data
@Row(colsOrder = {"model_code", "qty", "comment", "size", "email"})
public class RequestExcelDTO {
    @Column(name = "model_code")
    Integer modelCode;
    @Column(name = "qty")
    Integer qty;
    @Column(name = "comment")
    String comment;
    @Column(name = "size")
    String size;
    @Column(name = "email")
    String email;
}
