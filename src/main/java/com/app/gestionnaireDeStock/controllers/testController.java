package com.app.gestionnaireDeStock.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @GetMapping("Test")
    public String hello(){
        return "hello";    }
}
