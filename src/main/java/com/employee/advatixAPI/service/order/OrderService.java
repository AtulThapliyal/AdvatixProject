package com.employee.advatixAPI.service.order;

import com.employee.advatixAPI.dto.order.OrderDetailsDto;
import com.employee.advatixAPI.dto.order.OrderListRequestDto;
import com.employee.advatixAPI.dto.order.OrderRequestDto;
import com.employee.advatixAPI.entity.carrier.ClientCarrierInfo;
import com.employee.advatixAPI.entity.carrier.PartnerInfo;
import com.employee.advatixAPI.entity.client.ClientInfo;
import com.employee.advatixAPI.entity.order.*;
import com.employee.advatixAPI.entity.warehouse.ProductOrderEntity;
import com.employee.advatixAPI.entity.warehouse.WarehouseReceivedItems;
import com.employee.advatixAPI.entity.warehouse.enums.InventoryStage;
import com.employee.advatixAPI.entity.warehouse.enums.ReceiveStatus;
import com.employee.advatixAPI.exception.NotFoundException;
import com.employee.advatixAPI.repository.client.ClientRepository;
import com.employee.advatixAPI.repository.order.CILOrderRepository;
import com.employee.advatixAPI.repository.order.FEPOrderRepository;
import com.employee.advatixAPI.repository.warehouse.ProductOrderRepository;
import com.employee.advatixAPI.repository.warehouse.WarehouseRepository;
import com.employee.advatixAPI.repository.partner.ClientCarrier;
import com.employee.advatixAPI.repository.partner.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.employee.advatixAPI.service.warehouse.WarehouseItemsService.saveIntoWarehouseInventory;

@Service
public class OrderService {

    @Autowired
    CILOrderRepository cilOrderRepository;

    @Autowired
    FEPOrderRepository fepOrderRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientCarrier clientCarrierRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    ProductOrderRepository productOrderRepository;

    public ResponseEntity<?> createOrder(OrderRequestDto orderInfo) {
        List<Integer> productIds = new ArrayList<>();

        orderInfo.getOrderItemsList().forEach(a -> productIds.add(a.getProductId()));

        Optional<List<WarehouseReceivedItems>> warehouseReceivedItems = warehouseRepository.findAllByClientIdAndProductIdIn(orderInfo.getClientId(), productIds);
        HashMap<Integer, WarehouseReceivedItems> itemsHashMap = new HashMap<>();


        CILOrderInfo cilOrder = new CILOrderInfo();

        if (warehouseReceivedItems.isPresent()) {
            warehouseReceivedItems.get().forEach(item -> itemsHashMap.put(item.getProductId(), item));

            ClientInfo clientInfo = clientRepository.findById(orderInfo.getClientId()).get();

            Optional<ClientCarrierInfo> carrierInfo = clientCarrierRepository.findByClientId(clientInfo.getClientId());
            List<CILOrderItems> orderItems = new ArrayList<>();

            cilOrder.setStatusId(0);
            cilOrder.setClientId(orderInfo.getClientId());
            cilOrder.setShipToCountryId(orderInfo.getCountryId());
            cilOrder.setShipToStateId(orderInfo.getStateId());
            cilOrder.setShipToCityId(orderInfo.getCityId());
            cilOrder.setShipToAddress(orderInfo.getAddress1());
            cilOrder.setWarehouseId(orderInfo.getWarehouseId());
            cilOrder.setShipToName(orderInfo.getShipToName());
            cilOrder.setPostalCode(orderInfo.getPostalCode());
            cilOrder.setPhoneNumber(orderInfo.getPhoneNumber());
            cilOrder.setIsResidential(orderInfo.getIsResidential());
            cilOrder.setEmailAddress(orderInfo.getEmailAddress());

            for (OrderListRequestDto cilOrderItem : orderInfo.getOrderItemsList()) {
                CILOrderItems cilOrderItems = new CILOrderItems();
                cilOrderItems.setProductId(cilOrderItem.getProductId());
                cilOrderItems.setProductQty(cilOrderItem.getProductQty());

                orderItems.add(cilOrderItems);
            }
            cilOrder.setOrderItemsList(orderItems);

            if (carrierInfo.isPresent()) {
                PartnerInfo partnerInfo = partnerRepository.findByPartnerId(carrierInfo.get().getPartnerId());
                cilOrder.setCarrierId(carrierInfo.get().getPartnerId());
                cilOrder.setCarrierName(partnerInfo.getPartnerName());
                cilOrder.setServiceType(partnerInfo.getServiceType());
            }

            CILOrderInfo orderInformation = cilOrderRepository.save(cilOrder);

            orderInformation.setOrderNumber(generateOrderNumber(orderInformation.getOrderId()));

            //checking the addresses like country city state

            if (clientInfo.getIsPartialAllowed()) {
                CILOrderInfo cilOrderInfoBackOrder = new CILOrderInfo();
                List<CILOrderItems> orderItemsBackOrder = new ArrayList<>();

                List<CILOrderItems> newOrderItems = new ArrayList<>();

                for (CILOrderItems cilOrderItem : orderInformation.getOrderItemsList()) {
                    Integer warehouseInventory = itemsHashMap.get(cilOrderItem.getProductId()).getQuantity();

                    if (cilOrderItem.getProductQty() > warehouseInventory) {
                        CILOrderItems backOrderItem = new CILOrderItems();
                        backOrderItem.setProductId(cilOrderItem.getProductId());
                        backOrderItem.setProductQty(cilOrderItem.getProductQty() - warehouseInventory);
                        orderItemsBackOrder.add(backOrderItem);

                        if (warehouseInventory > 0) {
                            CILOrderItems orderItem = new CILOrderItems();

                            orderItem.setProductId(cilOrderItem.getProductId());
                            orderItem.setProductQty(warehouseInventory);
                            newOrderItems.add(orderItem);
                        }
                    } else {
                        CILOrderItems orderItem = new CILOrderItems();

                        orderItem.setProductId(cilOrderItem.getProductId());
                        orderItem.setProductQty(cilOrderItem.getProductQty());
                        newOrderItems.add(orderItem);
                    }

                }

                if (!orderItemsBackOrder.isEmpty()) {
                    cilOrderInfoBackOrder.setClientId(cilOrder.getClientId());

                    //setting the back order list in database with status 18(BackOrder)
                    cilOrderInfoBackOrder.setOrderItemsList(orderItemsBackOrder);

                    cilOrderInfoBackOrder.setStatusId(18);
                    cilOrderInfoBackOrder.setShipToCountryId(orderInfo.getCountryId());
                    cilOrderInfoBackOrder.setShipToStateId(orderInfo.getStateId());
                    cilOrderInfoBackOrder.setShipToCityId(orderInfo.getCityId());
                    cilOrderInfoBackOrder.setCarrierId(cilOrder.getCarrierId());
                    cilOrderInfoBackOrder.setShipToAddress(orderInfo.getAddress1());
                    cilOrderInfoBackOrder.setWarehouseId(orderInfo.getWarehouseId());
                    cilOrderInfoBackOrder.setShipToName(orderInfo.getShipToName());
                    cilOrderInfoBackOrder.setPostalCode(orderInfo.getPostalCode());
                    cilOrderInfoBackOrder.setPhoneNumber(orderInfo.getPhoneNumber());
                    cilOrderInfoBackOrder.setIsResidential(orderInfo.getIsResidential());
                    cilOrderInfoBackOrder.setEmailAddress(orderInfo.getEmailAddress());


                    if (carrierInfo.isPresent()) {
                        PartnerInfo partnerInfo = partnerRepository.findByPartnerId(carrierInfo.get().getPartnerId());
                        cilOrderInfoBackOrder.setCarrierId(carrierInfo.get().getPartnerId());
                        cilOrderInfoBackOrder.setCarrierName(partnerInfo.getPartnerName());
                        cilOrderInfoBackOrder.setServiceType(partnerInfo.getServiceType());
                    }
                    cilOrderInfoBackOrder.setOrderNumber(generateSplitOrderNumber(orderInformation.getOrderId()));

                    cilOrderRepository.save(cilOrderInfoBackOrder);
                }

//                if (!newOrderItems.isEmpty()) {
//                    saveInFEP(cilOrder, newOrderItems, itemsHashMap);
//                }


            } else {
                for (OrderListRequestDto cilOrderItem : orderInfo.getOrderItemsList()) {
                    if (cilOrderItem.getProductQty() <= 0) {
                        throw new NotFoundException("Product with id  " + cilOrderItem.getProductId() + "can not be zero.");
                    } else if (cilOrderItem.getProductQty() > itemsHashMap.get(cilOrderItem.getProductId()).getQuantity()) {
                        orderInformation.setReason("The order can not be completed due to unavailability of products.");
                        return ResponseEntity.ok(orderInformation);
                    }
//                    saveInFEP(cilOrder, orderItems, itemsHashMap);
                }
            }
        } else {
            throw new NotFoundException("This id does not belong to this client");
        }

        return null;
    }

    public String generateOrderNumber(Integer orderId) {
        return "ORDER" + orderId;
    }

    public String generateSplitOrderNumber(Integer orderId) {
        return "ORDER" + orderId + "S1";
    }

    public ResponseEntity<?> createNewOrder(OrderRequestDto orderInfo) {
        List<Integer> productIds = new ArrayList<>();

        orderInfo.getOrderItemsList().forEach(a -> productIds.add(a.getProductId()));

        Optional<List<WarehouseReceivedItems>> warehouseReceivedItems = warehouseRepository.findByWarehouseIdAndClientIdAndInventoryStageAndProductIdIn(orderInfo.getWarehouseId(), orderInfo.getClientId(), InventoryStage.AVAILABLE, productIds);
        HashMap<Integer, Integer> itemsHashMap = new HashMap<>();

        if (warehouseReceivedItems.isPresent()) {
            warehouseReceivedItems.get().forEach(item -> {
                int productId = item.getProductId();
                int quantity = item.getQuantity();

                itemsHashMap.put(productId, itemsHashMap.getOrDefault(productId, 0) + quantity);
            });
            CILOrderInfo cilOrder = saveOrderInfo(orderInfo);

            CILOrderInfo orderInformation = cilOrderRepository.save(cilOrder);
            orderInformation.setOrderNumber(generateOrderNumber(orderInformation.getOrderId()));
            cilOrderRepository.save(orderInformation);

            for (OrderListRequestDto cilOrderItem : orderInfo.getOrderItemsList()) {
                if (cilOrderItem.getProductQty() <= 0) {
                    throw new NotFoundException("Product with id  " + cilOrderItem.getProductId() + "can not be zero.");
                } else if (cilOrderItem.getProductQty() > itemsHashMap.get(cilOrderItem.getProductId())) {
                    orderInformation.setReason("The order can not be completed due to unavailability of products.");
                    cilOrderRepository.save(orderInformation);
                    return ResponseEntity.ok(orderInformation);

                }
            }
            saveOrderInFep(cilOrder, cilOrder.getOrderItemsList(), warehouseReceivedItems.get());
        } else {
            throw new NotFoundException("This id does not belong to this client");
        }

        return null;
    }
    public  CILOrderInfo saveOrderInfo(OrderRequestDto orderInfo)
    {

        ClientInfo clientInfo = clientRepository.findById(orderInfo.getClientId()).get();

        Optional<ClientCarrierInfo> carrierInfo = clientCarrierRepository.findByClientId(clientInfo.getClientId());
        List<CILOrderItems> orderItems = new ArrayList<>();
        CILOrderInfo cilOrder = new CILOrderInfo();

        cilOrder.setStatusId(0);
        cilOrder.setClientId(orderInfo.getClientId());
        cilOrder.setShipToCountryId(orderInfo.getCountryId());
        cilOrder.setShipToStateId(orderInfo.getStateId());
        cilOrder.setShipToCityId(orderInfo.getCityId());
        cilOrder.setShipToAddress(orderInfo.getAddress1());
        cilOrder.setWarehouseId(orderInfo.getWarehouseId());
        cilOrder.setShipToName(orderInfo.getShipToName());
        cilOrder.setPostalCode(orderInfo.getPostalCode());
        cilOrder.setPhoneNumber(orderInfo.getPhoneNumber());
        cilOrder.setIsResidential(orderInfo.getIsResidential());
        cilOrder.setEmailAddress(orderInfo.getEmailAddress());

        for (OrderListRequestDto cilOrderItem : orderInfo.getOrderItemsList()) {
            CILOrderItems cilOrderItems = new CILOrderItems();
            cilOrderItems.setProductId(cilOrderItem.getProductId());
            cilOrderItems.setProductQty(cilOrderItem.getProductQty());

            orderItems.add(cilOrderItems);
        }
        cilOrder.setOrderItemsList(orderItems);

        if (carrierInfo.isPresent()) {
            PartnerInfo partnerInfo = partnerRepository.findByPartnerId(carrierInfo.get().getPartnerId());
            cilOrder.setCarrierId(carrierInfo.get().getPartnerId());
            cilOrder.setCarrierName(partnerInfo.getPartnerName());
            cilOrder.setServiceType(partnerInfo.getServiceType());
        }
        return cilOrder;
    }

    private void saveOrderInFep(CILOrderInfo orderInfo, List<CILOrderItems> orderItems, List<WarehouseReceivedItems> warehouseReceivedItems) {
        FEPOrderInfo fepOrderInfo = new FEPOrderInfo();
        List<FEPOrderItems> fepOrderItemsList = new ArrayList<>();
        List<ProductOrderEntity> productOrderEntities = new ArrayList<>();

        fepOrderInfo.setClientId(orderInfo.getClientId());
        fepOrderInfo.setOrderId(orderInfo.getOrderId());
        fepOrderInfo.setShipToCityId(orderInfo.getShipToCityId());
        fepOrderInfo.setShipToCountryId(orderInfo.getShipToCountryId());
        fepOrderInfo.setShipToStateId(orderInfo.getShipToStateId());
        fepOrderInfo.setStatusId(1);
        fepOrderInfo.setWarehouseId(orderInfo.getWarehouseId());
        fepOrderInfo.setOrderNumber(orderInfo.getOrderNumber());
        fepOrderInfo.setShipToAddress(orderInfo.getShipToAddress());
        fepOrderInfo.setShipToName(orderInfo.getShipToName());
        fepOrderInfo.setPostalCode(orderInfo.getPostalCode());
        fepOrderInfo.setEmail(orderInfo.getEmailAddress());
        fepOrderInfo.setIsResidential(orderInfo.getIsResidential());
        fepOrderInfo.setPhone(orderInfo.getPhoneNumber());
        System.out.println(warehouseReceivedItems);

        Map<Integer, List<WarehouseReceivedItems>> warehouseItemsMap = warehouseReceivedItems.stream()
                .collect(Collectors.groupingBy(WarehouseReceivedItems::getProductId));

        for (CILOrderItems cilOrderItems : orderItems) {
            Integer productId = cilOrderItems.getProductId();
            Integer quantity = cilOrderItems.getProductQty();

            List<WarehouseReceivedItems> productWarehouseItems = warehouseItemsMap.getOrDefault(productId, Collections.emptyList());

            if (warehouseItemsMap.containsKey(productId)) {
                int remainingQty = quantity;

                for (WarehouseReceivedItems warehouseItem : productWarehouseItems) {

                    if (remainingQty <= 0) {
                        break;
                    }
                    int availableInWarehouse = warehouseItem.getQuantity();
                    Optional<WarehouseReceivedItems> allocatedItem = warehouseRepository.findByWarehouseIdAndClientIdAndProductIdAndInventoryStageAndLocation(
                            orderInfo.getWarehouseId(), orderInfo.getClientId(), productId, InventoryStage.ALLOCATED, warehouseItem.getLocation());

                    if(availableInWarehouse < remainingQty){
                        remainingQty-=availableInWarehouse;

                        if(allocatedItem.isPresent()){
                            allocatedItem.get().setQuantity(availableInWarehouse+allocatedItem.get().getQuantity());
                            warehouseRepository.save(allocatedItem.get());
                        }else{
                            WarehouseReceivedItems newEntryWarehouse = saveIntoWarehouseInventory(orderInfo.getWarehouseId(), warehouseItem.getLocation(), orderInfo.getClientId(), warehouseItem.getQuantity(), warehouseItem.getProductId(), LocalDate.now(), InventoryStage.ALLOCATED, ReceiveStatus.STOW, warehouseItem.getEmployeeId());
                            warehouseRepository.save(newEntryWarehouse);
                        }
                        warehouseItem.setQuantity(0);
                        warehouseRepository.save(warehouseItem);

                        ProductOrderEntity productOrderEntity = addProductInOrderProduct(orderInfo, productId, warehouseItem.getLocation(), availableInWarehouse);
                        productOrderEntities.add(productOrderEntity);
                    }
                    else{
                        availableInWarehouse -=remainingQty;
                        if(allocatedItem.isPresent()){
                            allocatedItem.get().setQuantity(remainingQty + allocatedItem.get().getQuantity());
                            warehouseRepository.save(allocatedItem.get());
                        }else{
                            WarehouseReceivedItems newEntryWarehouse = saveIntoWarehouseInventory(orderInfo.getWarehouseId(), warehouseItem.getLocation(), orderInfo.getClientId(), remainingQty, warehouseItem.getProductId(), LocalDate.now(), InventoryStage.ALLOCATED, ReceiveStatus.STOW, warehouseItem.getEmployeeId());
                            warehouseRepository.save(newEntryWarehouse);
                        }
                        warehouseItem.setQuantity(availableInWarehouse);
                        warehouseRepository.save(warehouseItem);

                        ProductOrderEntity productOrderEntity = addProductInOrderProduct(orderInfo, productId, warehouseItem.getLocation(), remainingQty);
                        productOrderEntities.add(productOrderEntity);

                        remainingQty = 0;

                    }
                }

                FEPOrderItems fepOrderItem = new FEPOrderItems();
                fepOrderItem.setProductId(productId);
                fepOrderItem.setProductQty(quantity);
                fepOrderItemsList.add(fepOrderItem);
            }
        }

        fepOrderInfo.setOrderItemsList(fepOrderItemsList);
        fepOrderRepository.save(fepOrderInfo);
        productOrderRepository.saveAll(productOrderEntities);
    }

    private ProductOrderEntity addProductInOrderProduct( CILOrderInfo orderInfo, Integer productId, String location, Integer quantity) {
        ProductOrderEntity productOrder = new ProductOrderEntity();
        productOrder.setOrderNumber(orderInfo.getOrderNumber());
        productOrder.setProductId(productId);
        productOrder.setClientId(orderInfo.getClientId());
        productOrder.setWarehouseId(orderInfo.getWarehouseId());
        productOrder.setInventoryStage(InventoryStage.ALLOCATED);
        productOrder.setReceiveStatus(ReceiveStatus.STOW);
        productOrder.setLocationBarCode(location);
        productOrder.setQuantity(quantity);

        return productOrder;
    }

    public ResponseEntity<?> getOrderDetails(String orderNumber) {
        OrderDetailsDto orderDetails = new OrderDetailsDto();

        Optional<FEPOrderInfo> fepOrderInfo = fepOrderRepository.findByOrderNumber(orderNumber);
        if (fepOrderInfo.isPresent()) {
            orderDetails.setOrderNumber(fepOrderInfo.get().getOrderNumber());
            orderDetails.setProductDetails(fepOrderInfo.get().getOrderItemsList());

            return ResponseEntity.ok(orderDetails);
        } else {
            throw new NotFoundException("The Order number does not exist" + orderNumber);
        }
    }


}

