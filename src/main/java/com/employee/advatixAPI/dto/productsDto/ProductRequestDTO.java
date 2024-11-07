package com.employee.advatixAPI.dto.productsDto;

import com.employee.advatixAPI.entity.product.Product;
import com.employee.advatixAPI.entity.product.ProductAttribute;
import lombok.Data;

import java.util.List;

@Data
public class ProductRequestDTO {
    Product product;
    List<ProductAttribute> productAttributes;
}
