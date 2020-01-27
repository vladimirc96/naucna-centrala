package com.upp.naucnacentrala.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    private String get(){
        return "USPESNO";
    }

}
