package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.client.OrderClient;
import com.upp.naucnacentrala.dto.InitOrderResponseDTO;
import com.upp.naucnacentrala.dto.MagazineDTO;
import com.upp.naucnacentrala.model.OrderObject;
import com.upp.naucnacentrala.service.OrderObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderObjectService orderObjectService;

    @RequestMapping(value = "/magazine/init", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initOrder(@RequestBody MagazineDTO magazineDTO, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.create(magazineDTO, request), HttpStatus.OK);
    }


}
