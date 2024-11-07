package com.employee.advatixAPI.repository.shipment;

import com.employee.advatixAPI.entity.shipment.ASNUnits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ASNUnitRepository extends JpaRepository<ASNUnits, Integer> {
}
