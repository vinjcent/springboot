package com.vinjcent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/index")
    public String toIndex() {
        return "index ok";
    }

    @PostMapping("/withdraw")
    public String withdraw() {
        System.out.println("第一次转账操作");
        return "执行第一次转账操作";
    }
}
