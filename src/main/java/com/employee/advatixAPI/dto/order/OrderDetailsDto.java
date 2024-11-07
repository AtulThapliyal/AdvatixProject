package com.employee.advatixAPI.dto.order;

import com.employee.advatixAPI.entity.order.FEPOrderItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OrderDetailsDto {
    private String orderNumber;
    private String orderStatus;
    private List<FEPOrderItems> productDetails;
}
