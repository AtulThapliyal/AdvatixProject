package com.employee.advatixAPI.repository.order;

import com.employee.advatixAPI.entity.order.FEPOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<FEPOrderStatus, Integer> {

}
