package com.employee.advatixAPI.repository.product;

import com.employee.advatixAPI.entity.product.Attributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attributes, Integer> {
}
