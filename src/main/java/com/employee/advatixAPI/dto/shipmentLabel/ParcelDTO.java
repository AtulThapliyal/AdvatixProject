package com.employee.advatixAPI.dto.shipmentLabel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelDTO {
    private DimensionsDTO dimensions;
    private WeightDTO weight;
}
