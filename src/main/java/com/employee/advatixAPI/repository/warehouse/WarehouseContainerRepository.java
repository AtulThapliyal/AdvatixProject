package com.employee.advatixAPI.repository.warehouse;

import com.employee.advatixAPI.entity.warehouse.WarehouseContainers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseContainerRepository extends JpaRepository<WarehouseContainers, Integer> {

    Optional<WarehouseContainers> findByContainerId(String id);

//    Integer findFirstByOrderNumber(String od);

//    String findFirstContainerIdByOrderNumber(String od);

    WarehouseContainers findFirstByOrderNumber(String o);
}
