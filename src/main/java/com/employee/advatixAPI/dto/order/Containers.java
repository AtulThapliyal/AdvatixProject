package com.employee.advatixAPI.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Containers {
    private String id;
    private List<ContainerProducts> productsList;
}
