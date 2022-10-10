package com.vinjcent.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 自定义 spring security 配置
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 开启 http 请求认证
        http.authorizeRequests().anyRequest().authenticated();

        // 使用 oauth2 认证,配置文件中配置认证服务
        http.oauth2Login();

    }
}
