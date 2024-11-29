package org.market.trade.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/v1")
    public String getV1(){
        return "Tradialize";
    }
}
