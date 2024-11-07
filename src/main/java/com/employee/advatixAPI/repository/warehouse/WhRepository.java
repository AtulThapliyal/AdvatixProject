package com.employee.advatixAPI.repository.warehouse;

import com.employee.advatixAPI.entity.warehouse.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhRepository extends JpaRepository<Warehouse, Integer> {
}
