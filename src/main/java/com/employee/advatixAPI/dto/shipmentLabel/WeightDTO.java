package com.employee.advatixAPI.dto.shipmentLabel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeightDTO {
    private double value;
    private String unit;
}
