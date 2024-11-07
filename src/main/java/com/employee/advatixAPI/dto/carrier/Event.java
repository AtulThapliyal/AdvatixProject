package com.employee.advatixAPI.dto.carrier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    private String ts;

    @JsonProperty("eventDescription")
    private String eventDescription;

    @JsonProperty("timeZone")
    private String barCode;
}
