package com.upp.naucnacentrala.controller;

import com.upp.naucnacentrala.Utils;
import com.upp.naucnacentrala.client.OrderClient;
import com.upp.naucnacentrala.dto.*;
import com.upp.naucnacentrala.model.OrderObject;
import com.upp.naucnacentrala.security.TokenUtils;
import com.upp.naucnacentrala.service.OrderObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderObjectService orderObjectService;

    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<OrderDTO>> getAllOrders() {
        return new ResponseEntity<>(orderObjectService.getAllOrders(), HttpStatus.OK);
    }

    @RequestMapping(value = "/all-by-user", method = RequestMethod.GET)
    public ResponseEntity<List<OrderDTO>> getAllOrdersByUsername(HttpServletRequest request){
        return new ResponseEntity<>(orderObjectService.findAllByUserId(Utils.getUsernameFromRequest(request, tokenUtils)), HttpStatus.OK);
    }

    @RequestMapping(value = "/magazine/init", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initOrder(@RequestBody MagazineDTO magazineDTO, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.create(magazineDTO, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/magazine/initSub", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initSub(@RequestBody MagazineDTO magazineDTO, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.createSub(magazineDTO, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/scPaper/init", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitOrderResponseDTO> initPaper(@RequestBody SciencePaperDTO paperDTO, HttpServletRequest request){
        return new ResponseEntity(orderObjectService.createPaper(paperDTO, request), HttpStatus.OK);
    }

    @RequestMapping(value = "/finalize", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity finalizeOrder(@RequestBody FinalizeOrderDTO foDTO){
        orderObjectService.finalizeOrder(foDTO);
        return new ResponseEntity(HttpStatus.OK);
    }


}
