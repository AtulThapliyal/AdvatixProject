package com.employee.advatixAPI.repository.shipment;

import com.employee.advatixAPI.entity.shipment.ASNNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ASNNoticeRepository extends JpaRepository<ASNNotice, Integer> {
}
