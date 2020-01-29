package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.client.OrderClient;
import com.upp.naucnacentrala.dto.FinalizeOrderDTO;
import com.upp.naucnacentrala.dto.InitOrderResponseDTO;
import com.upp.naucnacentrala.dto.MagazineDTO;
import com.upp.naucnacentrala.model.OrderObject;
import com.upp.naucnacentrala.service.OrderObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/magazine/initSub", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initSub(@RequestBody MagazineDTO magazineDTO, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.createSub(magazineDTO, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/finalize", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity finalizeOrder(@RequestBody FinalizeOrderDTO foDTO){
        orderObjectService.finalizeOrder(foDTO);
        return new ResponseEntity(HttpStatus.OK);
    }




}
