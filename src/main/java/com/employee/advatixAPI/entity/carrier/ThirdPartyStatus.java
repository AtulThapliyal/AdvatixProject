package com.employee.advatixAPI.entity.carrier;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "wh_orders_third_party_status")
@Data
public class ThirdPartyStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusId;

    private String status;

    private String time;

    private String barCode;

    private String imagePath;

}
