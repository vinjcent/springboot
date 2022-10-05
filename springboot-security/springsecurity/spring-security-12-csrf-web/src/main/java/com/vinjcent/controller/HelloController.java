package com.vinjcent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @PostMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello spring security!";
    }


    @RequestMapping("/toIndex")
    public String toIndex() {
        return "index";
    }
}
