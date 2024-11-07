package com.employee.advatixAPI.service.warehouse;

import com.employee.advatixAPI.dto.warehouse.Stow;
import com.employee.advatixAPI.dto.warehouse.WarehouseReceivedItemsRequest;
import com.employee.advatixAPI.entity.warehouse.WarehouseReceivedItems;
import com.employee.advatixAPI.entity.warehouse.enums.InventoryStage;
import com.employee.advatixAPI.entity.warehouse.enums.ReceiveStatus;
import com.employee.advatixAPI.exception.NotFoundException;
import com.employee.advatixAPI.repository.warehouse.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class WarehouseItemsService {

    @Autowired
    WarehouseRepository warehouseRepository;


    public ResponseEntity<?> receiveItemsInContainers(WarehouseReceivedItemsRequest warehouseItemsRequest) {
        saveItemsInWarehouse(warehouseItemsRequest, InventoryStage.ON_HAND);
        return ResponseEntity.ok("");
    }

    public ResponseEntity<?> stowItemsInBins(Stow stowRequest) {
        stowRequest.getStowItems().forEach(stowItem -> {
            Optional<WarehouseReceivedItems> whItems = warehouseRepository.findByWarehouseIdAndClientIdAndProductIdAndInventoryStageAndLocation(stowRequest.getWarehouseId(), stowRequest.getClientId(), stowItem.getProductId(), InventoryStage.AVAILABLE, stowItem.getBinId());

            Optional<WarehouseReceivedItems> itemInContainer = warehouseRepository.findByWarehouseIdAndClientIdAndProductIdAndInventoryStageAndLocation(stowRequest.getWarehouseId(), stowRequest.getClientId(), stowItem.getProductId(), InventoryStage.ON_HAND, stowItem.getContainerId());

            if(itemInContainer.get().getQuantity() == 0){
                throw new NotFoundException("NO more quantity can be added in bins");
            }
            if(whItems.isPresent()){
                WarehouseReceivedItems receivedItems = whItems.get();
                receivedItems.setQuantity(receivedItems.getQuantity() + stowItem.getQuantity());
            }else{
                WarehouseReceivedItems warehouseReceivedItems = saveIntoWarehouseInventory(stowRequest.getWarehouseId(), stowItem.getBinId(), stowRequest.getClientId(), stowItem.getQuantity(), stowItem.getProductId(), LocalDate.now(), InventoryStage.AVAILABLE, ReceiveStatus.STOW, stowRequest.getEmployeeId());
                warehouseRepository.save(warehouseReceivedItems);
            }

            itemInContainer.get().setQuantity(itemInContainer.get().getQuantity() - stowItem.getQuantity());
            warehouseRepository.save(itemInContainer.get());
        });

        return ResponseEntity.ok("");
    }

    private void saveItemsInWarehouse(WarehouseReceivedItemsRequest warehouseItemsRequest, InventoryStage stage) {
        warehouseItemsRequest.getContainerProductsList().forEach(container -> {
            container.getProductsList().forEach(products -> {
                Optional<WarehouseReceivedItems> whItems = warehouseRepository.findByWarehouseIdAndClientIdAndProductIdAndInventoryStageAndLocation(warehouseItemsRequest.getWarehouseId(), warehouseItemsRequest.getClientId(), products.getProductId(), stage, container.getId());
                if (whItems.isPresent()) {
                    WarehouseReceivedItems receivedItems = whItems.get();
                    receivedItems.setQuantity(receivedItems.getQuantity() + products.getQuantity());
                } else {
                    WarehouseReceivedItems warehouseReceivedItems = saveIntoWarehouseInventory(warehouseItemsRequest.getWarehouseId(), container.getId(), warehouseItemsRequest.getClientId(), products.getQuantity(), products.getProductId(), LocalDate.now(), InventoryStage.ON_HAND, ReceiveStatus.RECEIVED, whItems.get().getEmployeeId());
                    warehouseRepository.save(warehouseReceivedItems);
                }
            });
        });
    }


    public static WarehouseReceivedItems saveIntoWarehouseInventory(Integer warehouseId, String location, Integer clientId, Integer quantity, Integer productId, LocalDate date, InventoryStage inventoryStage, ReceiveStatus receiveStatus, Integer userId){
        WarehouseReceivedItems warehouseReceivedItems = new WarehouseReceivedItems();

        warehouseReceivedItems.setWarehouseId(warehouseId);
        warehouseReceivedItems.setLocation(location);
        warehouseReceivedItems.setClientId(clientId);
        warehouseReceivedItems.setQuantity(quantity);
        warehouseReceivedItems.setProductId(productId);
        warehouseReceivedItems.setCreatedOn(date);
        warehouseReceivedItems.setInventoryStage(inventoryStage);
        warehouseReceivedItems.setReceiveStatus(receiveStatus);

        return warehouseReceivedItems;
    }



}
