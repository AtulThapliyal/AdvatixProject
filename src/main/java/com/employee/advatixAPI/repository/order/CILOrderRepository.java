package com.employee.advatixAPI.repository.order;

import com.employee.advatixAPI.entity.order.CILOrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CILOrderRepository extends JpaRepository<CILOrderInfo, Integer> {
}
