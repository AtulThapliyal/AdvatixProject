package com.employee.advatixAPI.dto.shipmentLabel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoxRequestDto {
    private String orderNumber;
    private String boxLabel;
    private double boxWeight;
    private BoxDimensionsDTO dimensions;
}
