package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.client.RegistrationClient;
import com.upp.naucnacentrala.dto.KPRegistrationDTO;
import com.upp.naucnacentrala.dto.MagazineDTO;
import com.upp.naucnacentrala.dto.MagazineInfoDTO;
import com.upp.naucnacentrala.dto.StringDTO;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.service.KPService;
import com.upp.naucnacentrala.model.SciencePaper;
import com.upp.naucnacentrala.service.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping(value = "kp")
public class KPController {

    @Autowired
    MagazineService magazineService;

    @Autowired
    RegistrationClient registrationClient;

    @Autowired
    KPService kpService;

    @RequestMapping(value = "/createPlan/{magazineId}", method = RequestMethod.GET)
    public ResponseEntity<?> sendKPCreatePlan(@PathVariable("magazineId") long magazineId){
        return new ResponseEntity<>(kpService.createPlan(magazineId), HttpStatus.OK);
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity registerSeller(@RequestBody MagazineDTO magazineDTO){
        return new ResponseEntity<>(kpService.initRegistration(magazineDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/registration/review", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity reviewRegistrationSeller(@RequestBody MagazineDTO magazineDTO){
        return new ResponseEntity<>(kpService.reviewRegistrationSeller(magazineDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/registration/status", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity registerSeller(@RequestBody KPRegistrationDTO kprDTO){
        kpService.changeRegistrationStatus(kprDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/subscriptions/{magazineId}", method = RequestMethod.GET)
    public ResponseEntity<?> subscriptions(@PathVariable("magazineId") long magazineId){
        return new ResponseEntity<>(kpService.getSubscriptions(magazineId), HttpStatus.OK);
    }

}
