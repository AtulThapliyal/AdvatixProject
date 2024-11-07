package com.employee.advatixAPI.repository.order;

import com.employee.advatixAPI.entity.order.OrderPickerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderPickerInfoRepository extends JpaRepository<OrderPickerInfo, Integer> {

    Optional<OrderPickerInfo> findByOrderNumber(String orderNumber);

    List<OrderPickerInfo> findAllByBoxId(String labelId);
}
