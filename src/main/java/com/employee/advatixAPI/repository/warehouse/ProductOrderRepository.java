package com.employee.advatixAPI.repository.warehouse;

import com.employee.advatixAPI.entity.warehouse.ProductOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, Integer> {
}
