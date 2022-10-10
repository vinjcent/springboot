package com.vinjcent.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * 自定义 Security 配置
 * 在 springboot 2.7.x 后,WebSecurityConfigurerAdapter 配置不再存在
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 所有请求都需要认证
        http.authorizeRequests()
                .anyRequest()
                .authenticated();

        // 开启登录请求
        http.formLogin();

        // 解决跨域请求
        http.cors()
                .configurationSource(configurationSource());

        // 关闭 csrf 跨域请求伪造攻击
        http.csrf()
                .disable();
    }

    // 跨域请求配置
    public CorsConfigurationSource configurationSource() {
        // 1.定义跨域配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);

        // 2.定义过滤器生效的url配置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
