package com.vinjcent.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DivWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 当访问url路径,映射一个view视图
        registry.addViewController("/toLogin").setViewName("login");
        registry.addViewController("/index").setViewName("index");
    }
}
