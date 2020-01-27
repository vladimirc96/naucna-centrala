package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.dto.MagazineInfoDTO;
import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.service.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "kp")
public class KPController {

    @Autowired
    MagazineService magazineService;

    @RequestMapping(value = "/createPlan/{magazineId}", method = RequestMethod.GET)
    public ResponseEntity<?> sendKPCreatePlan(@PathVariable("magazineId") long magazineId){
        Magazine m = magazineService.findOneById(magazineId);

        return new ResponseEntity<>("Task obavljen!", HttpStatus.OK);
    }
}
