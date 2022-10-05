package com.vinjcent.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/getUserInfo")
    public String getUserInfo() {
        // 1. 代码中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        System.out.println("username ===> " + user.getUsername());
        System.out.println("authorities ===> " + user.getAuthorities());
        return "认证成功!";
    }

    @RequestMapping("/test")
    public String test() {
        return "测试!";
    }
}
