package com.employee.advatixAPI.entity.carrier;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "wh_orders_shipment_journey")
@Data
public class ShipmentJourney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipId;

    private String status;

    private String time;

    private String barCode;

    private String imagePath;

    private Boolean webHookSent;

}
