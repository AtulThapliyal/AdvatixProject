package com.employee.advatixAPI.controller.carrier;

import com.employee.advatixAPI.dto.carrier.TrackingRequest;
import com.employee.advatixAPI.exception.NotFoundException;
import com.employee.advatixAPI.service.carrier.TrackingShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tracking")
public class TrackingShipmentController {

    @Autowired
    TrackingShipmentService trackingShipmentService;

    @PostMapping("/getTrackingDetails")
    public String getTrackingDetails(@RequestBody TrackingRequest trackingRequest){
        try{
           return  trackingShipmentService.getTrackingResponse(trackingRequest);
        }catch (Exception e){
            throw new NotFoundException("Not found");
        }
    }
}
