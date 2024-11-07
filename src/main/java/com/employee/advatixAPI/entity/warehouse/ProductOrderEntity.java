package com.employee.advatixAPI.entity.warehouse;

import com.employee.advatixAPI.entity.warehouse.enums.InventoryStage;
import com.employee.advatixAPI.entity.warehouse.enums.ReceiveStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_product_locations")
@Data
public class ProductOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productOrderId;

    private String orderNumber;

    private Integer productId;

    private Integer quantity;

    private Integer clientId;

    private Integer warehouseId;

    @Enumerated(EnumType.STRING)
    private InventoryStage inventoryStage;

    @Enumerated(EnumType.STRING)
    private ReceiveStatus receiveStatus;

    private String locationBarCode;

}
