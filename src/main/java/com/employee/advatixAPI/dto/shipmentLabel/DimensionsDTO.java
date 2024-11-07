package com.employee.advatixAPI.dto.shipmentLabel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimensionsDTO {
    private double length;
    private double width;
    private double height;
}
