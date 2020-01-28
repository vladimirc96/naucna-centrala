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
    RestTemplate restTemplate;

    @Autowired
    RegistrationClient registrationClient;

    @Autowired
    KPService kpService;

    @RequestMapping(value = "/createPlan/{magazineId}", method = RequestMethod.GET)
    public ResponseEntity<?> sendKPCreatePlan(@PathVariable("magazineId") long magazineId){
        Magazine m = magazineService.findOneById(magazineId);
        List<SciencePaper> radovi = m.getSciencePapers();
        double amount = 0;
        for(SciencePaper rad : radovi) {
            amount += rad.getPrice();
        }
        amount = amount * 0.9;
        String currency = "USD";
        if(!radovi.isEmpty()) {
            currency = radovi.get(0).getCurrency();
        }
        double roundAmount = Math.round(amount * 100.0) / 100.0;
        MagazineInfoDTO magazineDTO = new MagazineInfoDTO(m.getSellerId(), m.getName(), m.getIssn(), currency, roundAmount);
        ResponseEntity response = restTemplate.postForEntity("https://localhost:8500/sellers/sellers/createPlan", new HttpEntity<>(magazineDTO),
                String.class);
        StringDTO text = new StringDTO((String) response.getBody());
        return new ResponseEntity<>(text, HttpStatus.OK);
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

}
