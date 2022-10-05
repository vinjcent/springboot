package com.vinjcent.config.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import javax.sql.DataSource;
import java.util.UUID;


/**
 *  重写 WebSecurityConfigurerAdapter 类使得默认 DefaultWebSecurityCondition 条件失效
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 构造注入使用@Autowired,set注入使用@Resource
    private final DivUserDetailsService userDetailsService;
    // token 存储数据源
    private final DataSource dataSource;

    // UserDetailsService
    @Autowired
    public WebSecurityConfiguration(DivUserDetailsService userDetailsService, DataSource dataSource) {
        this.userDetailsService = userDetailsService;
        this.dataSource = dataSource;
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
                .rememberMe()
                .rememberMeServices(rememberMeServices())
                // .rememberMeParameter("remember-me") // 用来接受请求中哪个参数作为开启记住我的参数
                // .alwaysRemember(true)   // 总是记住我,只针对服务后台设置
                .and()
                .csrf()
                .disable();
    }

    // 指定记住我的实现
    @Bean
    public RememberMeServices rememberMeServices() {
        // 配置 token 数据源,保证服务重启之后仍然有存储记录
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        // 配置数据源
        tokenRepository.setDataSource(dataSource);
        // 设置第一次启动时,创建表结构(当对http请求的配置中不设置rememberMeServices()时,该设置生效,不然会报错)
        // tokenRepository.setCreateTableOnStartup(true);

        return new PersistentTokenBasedRememberMeServices(
                UUID.randomUUID().toString(), // 自定义一个生成令牌 key,默认 UUID
                userDetailsService,     // 认证数据源
                tokenRepository);     // 令牌存储方式(不建议使用内存的方式存储令牌)
    }

}
