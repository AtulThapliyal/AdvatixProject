package com.employee.advatixAPI.entity.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class
ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productAttributeInfoId;
    private Integer productId;
    private Integer attributeId;
    private String attributeDesc;
}
