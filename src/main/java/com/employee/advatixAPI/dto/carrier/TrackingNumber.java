package com.employee.advatixAPI.dto.carrier;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString

public class TrackingNumber {
    private String trackingNumber;
    private String country;
    private List<Event> events;
    private List<String> pods;
}
