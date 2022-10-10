package com.vinjcent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = {"com.vinjcent.mapper"})
@SpringBootApplication
public class SpringSecurity14AuthorizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurity14AuthorizeApplication.class, args);
    }

}
