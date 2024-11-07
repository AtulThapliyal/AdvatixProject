package com.employee.advatixAPI.dto.carrier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackingRequest {
    private List<String> trackingNumbers;
}
