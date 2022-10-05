package com.vinjcent.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/cors")
    // @CrossOrigin(origins = "http://127.0.0.1:63342")
    public String testCors() {
        return "Hello Cors!";
    }
}
