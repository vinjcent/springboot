package com.vinjcent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    /**
     * 资源          可访问的角色
     * /admin/**    ROLE_ADMIN
     * /user/**     ROLE_ADMIN ROLE_USER
     * /guest/**    ROLE_ADMIN ROLE_USER ROLE_GUEST
     *
     *
     * 用户          具有的角色信息
     * admin        ADMIN USER GUEST
     * user         USER GUEST
     * vinjcent     GUEST
     *
     */

    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "hello user";
    }

    @GetMapping("/guest/hello")
    public String guest() {
        return "hello guest";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
