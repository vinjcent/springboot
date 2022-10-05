//package com.vinjcent.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebMvcConfiguration implements WebMvcConfigurer {
//
//    // 用来全局处理跨域
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  // 对哪些请求进行跨域
//                .allowedMethods("*")
//                .allowedOrigins("*")
//                .allowedHeaders("*")
//                .maxAge(3600);
//    }
//}
