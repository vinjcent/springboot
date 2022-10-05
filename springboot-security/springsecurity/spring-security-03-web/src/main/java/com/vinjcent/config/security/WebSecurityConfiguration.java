package com.vinjcent.config.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;


/**
 *  重写 WebSecurityConfigurerAdapter 类使得默认 DefaultWebSecurityCondition 条件失效
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 构造注入使用@Autowired,set注入使用@Resource
    private final DivUserDetailsService userDetailsService;

    // UserDetailsService
    @Autowired
    public WebSecurityConfiguration(DivUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // AuthenticationManager
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    // 拦配置http拦截
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("/toLogin").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/toLogin")
                .loginProcessingUrl("/login")
                .usernameParameter("uname")
                .passwordParameter("passwd")
                .defaultSuccessUrl("/toIndex", true)    // 重定向
                .failureUrl("/toLogin")     // 失败重定向
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/toLogin")
                .and()
                .csrf()
                .disable();

    }

}
