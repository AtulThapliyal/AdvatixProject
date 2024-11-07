package com.employee.advatixAPI.repository.boxLabel;

import com.employee.advatixAPI.entity.shipmentLabels.BoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoxLabelRepository extends JpaRepository<BoxEntity, Integer> {
    List<BoxEntity> findAllByOrderNumber(String orderNumber);
}
