package com.employee.advatixAPI.repository.warehouse;

import com.employee.advatixAPI.entity.warehouse.WarehouseReceivedItems;
import com.employee.advatixAPI.entity.warehouse.enums.InventoryStage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<WarehouseReceivedItems, Integer>
{
    List<WarehouseReceivedItems> findByProductIdIn(List<Integer> productIds);

    WarehouseReceivedItems findByProductId(Integer productId);

    @Modifying
    @Transactional
    @Query("UPDATE WarehouseReceivedItems w SET w.quantity = :quantity WHERE w.productId = :productId")
    void updateQuantityByProductId(Integer quantity, Integer productId);

    Optional<WarehouseReceivedItems> findWarehouseReceivedItemsByProductId(Integer productId);

    Optional<List<WarehouseReceivedItems>> findAllByClientIdAndProductIdIn(Integer clientId, List<Integer> productIds);

    Optional<WarehouseReceivedItems> findByWarehouseIdAndClientIdAndProductIdAndInventoryStageAndLocation(Integer warehouseId, Integer clientId, Integer productId, InventoryStage inventoryStage, String id);
    Optional<List<WarehouseReceivedItems>> findByWarehouseIdAndClientIdAndInventoryStageAndProductIdIn(Integer warehouseId, Integer clientId,  InventoryStage inventoryStage,List<Integer> productIds);
}
