package com.employee.advatixAPI.controller.shipment;

import com.employee.advatixAPI.dto.shipment.ASNNoticeRequestDto;
import com.employee.advatixAPI.entity.shipment.ASNNotice;
import com.employee.advatixAPI.service.shipment.ASNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asn")
public class ASNController {

    @Autowired
    ASNService asnService;

//    @GetMapping("/{asnId}")
//    public ASNNotice getASNbyId(@PathVariable Integer asnId){
//        return asnNoticeRepository.findById(asnId).get();
//    }

    @PostMapping("/addASN")
    public ASNNotice addASNNotice(@RequestBody ASNNoticeRequestDto asnNotice){
        return asnService.addASN(asnNotice);
    }



}
