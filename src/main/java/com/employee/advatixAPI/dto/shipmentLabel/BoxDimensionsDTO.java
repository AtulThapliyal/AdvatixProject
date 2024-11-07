package com.employee.advatixAPI.dto.shipmentLabel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxDimensionsDTO {
    private Double length;
    private Double width;
    private Double height;
}
