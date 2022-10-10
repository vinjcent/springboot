//package com.vinjcent.config.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
//
//import javax.sql.DataSource;
//
///**
// * 开启 oauth2 资源服务器
// */
//@Configuration
//@EnableResourceServer
//public class JdbcResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//
//    // 注入数据源
//    private final DataSource dataSource;
//
//    @Autowired
//    public JdbcResourceServerConfiguration(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    // token 存储方式
//    @Bean
//    public TokenStore tokenStore() {
//        return new JdbcTokenStore(dataSource);
//    }
//}
