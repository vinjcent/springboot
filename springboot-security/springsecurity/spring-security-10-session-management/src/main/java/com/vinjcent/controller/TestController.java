package com.vinjcent.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String toTest() {
        return "Hello   Spring Security!";
    }
}
