package com.employee.advatixAPI.dto.order;

import lombok.Data;

import java.util.List;
@Data
public class OrderRequestDto {
    private Integer clientId;
    private String shipToName;
    private List<OrderListRequestDto> orderItemsList;
    private Integer countryId;
    private Integer stateId;
    private Integer cityId;
    private String address1;
    private String postalCode;
    private String phoneNumber;
    private String emailAddress;
    private Boolean isResidential;
    private Integer warehouseId;
}
