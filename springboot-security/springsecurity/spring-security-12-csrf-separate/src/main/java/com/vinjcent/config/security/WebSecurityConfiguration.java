package com.vinjcent.config.security;

import com.vinjcent.filter.LoginFilter;
import com.vinjcent.handler.DivAuthenticationFailureHandler;
import com.vinjcent.handler.DivAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    // 暴露自定义认证数据源
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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
        http.authorizeRequests().anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .csrf()
                // .disable();
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());    // 将令牌保存到 cookie 中,允许 cookie 前端获取

        // 替换原始 UsernamePasswordAuthenticationFilter 过滤器
        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
