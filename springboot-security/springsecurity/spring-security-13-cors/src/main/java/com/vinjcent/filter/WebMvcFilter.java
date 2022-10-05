package com.vinjcent.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class WebMvcFilter {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        // 1.定义需要注册的过滤器bean
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        // 2.定义跨域配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);

        // 3.定义过滤器生效的url配置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        registrationBean.setFilter(new CorsFilter(source));
        registrationBean.setOrder(-1);  // 一般是0、1，代表过滤器顺序等级,-1代表优先


        return registrationBean;
    }
}
