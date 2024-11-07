package com.employee.advatixAPI.dto;

import com.employee.advatixAPI.entity.address.City;
import com.employee.advatixAPI.entity.client.ClientInfo;
import com.employee.advatixAPI.entity.address.Country;
import com.employee.advatixAPI.entity.address.States;
import lombok.Data;

@Data
public class ClientResponse {
    private ClientInfo client;
    private Country country;
    private States states;
    private City city;
}
