package com.employee.advatixAPI.service.shipment;

import com.employee.advatixAPI.dto.shipment.ASNNoticeRequestDto;
import com.employee.advatixAPI.dto.shipment.ASNUnitRequest;
import com.employee.advatixAPI.entity.shipment.ASNNotice;
import com.employee.advatixAPI.entity.shipment.ASNUnits;
import com.employee.advatixAPI.repository.EmployeeRepository;
import com.employee.advatixAPI.repository.shipment.ASNNoticeRepository;
import com.employee.advatixAPI.repository.shipment.ASNUnitRepository;
import com.employee.advatixAPI.repository.warehouse.WarehouseRepository;
import com.employee.advatixAPI.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ASNService {

    @Autowired
    ASNNoticeRepository asnNoticeRepository;

    @Autowired
    ASNUnitRepository asnUnitRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    public ASNNotice addASN(ASNNoticeRequestDto asnNoticeRequest) {
        ASNNotice asnNotice = new ASNNotice();

        asnNotice.setPoNumber(asnNoticeRequest.getPoNumber());
        asnNotice.setLotNumber(asnNoticeRequest.getLotNumber());
        asnNotice.setTotalQuantity(asnNoticeRequest.getTotalQuantity());
        asnNotice.setCreatedOn(LocalDate.now());
        asnNotice.setEmployee(employeeRepository.findById(asnNoticeRequest.getCreatedBy()).get());


        List<ASNUnits> asnUnitsList = new ArrayList<>();
        for (ASNUnitRequest asnUnit : asnNoticeRequest.getAsnUnitList()) {
            ASNUnits unit = new ASNUnits(asnUnit.getQuantity(), asnUnit.getReceivedQuantity(), asnUnit.getLocation(), productRepository.findByProductId(asnUnit.getProductId()).get());
            asnUnitsList.add(unit);
        }
        asnNotice.setAsnUnitsList(asnUnitsList);  // set the saved ASNNotice with its units
        // Save ASNNotice to return

        return asnNoticeRepository.save(asnNotice);
    }
}
