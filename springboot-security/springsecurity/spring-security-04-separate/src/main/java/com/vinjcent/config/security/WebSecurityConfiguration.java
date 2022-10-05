package com.vinjcent.config.security;

import com.vinjcent.filter.LoginFilter;
import com.vinjcent.handler.DivAuthenticationFailureHandler;
import com.vinjcent.handler.DivAuthenticationSuccessHandler;
import com.vinjcent.handler.DivLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 注入数据源认证
    private final DivUserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfiguration(DivUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    // 自定义AuthenticationManager(自定义需要暴露该bean)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    // 暴露AuthenticationManager,使得这个bean能在组件中进行注入
    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        // 1.创建自定义的LoginFilter对象
        LoginFilter loginFilter = new LoginFilter();
        // 2.设置登陆操作的请求
        loginFilter.setFilterProcessesUrl("/login");
        // 3.动态设置传递的参数key
        loginFilter.setUsernameParameter("uname");  // 指定 json 中的用户名key
        loginFilter.setPasswordParameter("passwd"); // 指定 json 中的密码key
        // 4.设置自定义的用户认证管理者
        loginFilter.setAuthenticationManager(authenticationManager());
        // 5.配置认证成功/失败处理(前后端分离)
        loginFilter.setAuthenticationSuccessHandler(new DivAuthenticationSuccessHandler());  // 认证成功处理
        loginFilter.setAuthenticationFailureHandler(new DivAuthenticationFailureHandler());  // 认证失败处理
        return loginFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .anyRequest().authenticated()  // 所有请求必须认证
                .and()
                .formLogin()    // 登录处理
                .and()
                .logout()
                .logoutUrl("/logout")   // 登出处理(也可以通过自定logoutRequestMatcher配置登出请求url和请求类型)
                .logoutSuccessHandler(new DivLogoutSuccessHandler())    // 注销登录成功处理
                .and()
                .exceptionHandling()    // 异常处理(用于未认证处理返回的数据)
                .authenticationEntryPoint(((req, resp, ex) -> {
                    // 设置响应内容类型"application/json;charset=UTF-8"
                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    // 设置响应状态码为"未授权"401
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    resp.getWriter().println("请认证之后再操作!");
                })) // 配置认证入口点异常处理
                .and()
                .csrf()
                .disable();     // 关闭csrf跨域请求

        // 替换原始 UsernamePasswordAuthenticationFilter 过滤器
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
        /**
            http.addFilter();   // 添加一个过滤器
            http.addFilterAt(); // at: 添加一个过滤器,将过滤链中的某个过滤器进行替换
            http.addFilterBefore(); // before: 添加一个过滤器,追加到某个具体过滤器之前
            http.addFilterAfter();  // after: 添加一个过滤器,追加到某个具体过滤器之后
         */

    }
}
