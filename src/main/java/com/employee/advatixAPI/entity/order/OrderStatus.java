package com.employee.advatixAPI.entity.order;

import jakarta.persistence.*;

@Entity
@Table(name = "oms_order_status")
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="status_id")
    private Integer statusId;

    private String statusDesc;

}
