package com.vinjcent.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 注入
    private final FindByIndexNameSessionRepository sessionRepository;

    @Autowired
    public WebSecurityConfiguration(FindByIndexNameSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    // 创建 session 同步到 redis 中
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successForwardUrl("/test")
                .and()
                .logout()   // 需要开启退出登录
                .and()
                .csrf()
                .disable()
                .sessionManagement()    // 开启会话管理
                .maximumSessions(1)     // 允许会话最大并发只能一个客户端
                // .expiredUrl("/toLogin");        // 传统架构的会话过期处理方案
                .expiredSessionStrategy(event -> {      // 前后端分离架构会话过期处理方案
                    HttpServletResponse response = event.getResponse();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("status", 500);
                    result.put("msg", "当前会话已经失效,请重新登录");
                    String str = new ObjectMapper().writeValueAsString(result);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().println(str);
                })
                .maxSessionsPreventsLogin(true)    // 一旦登录,禁止再次登录
                .sessionRegistry(sessionRegistry());     // 将 session 交给谁管理,前后端分离自定义过滤器需要配setSessionAuthenticationStrategy
    }


    // 用于监听会话的创建和销毁
    //@Bean
    //public HttpSessionEventPublisher httpSessionEventPublisher() {
    //    return new HttpSessionEventPublisher();
    //}
}
