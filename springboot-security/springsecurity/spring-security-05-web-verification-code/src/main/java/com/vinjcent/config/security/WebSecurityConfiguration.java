package com.vinjcent.config.security;


import com.vinjcent.filter.LoginKaptchaFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 *  重写 WebSecurityConfigurerAdapter 类使得默认 DefaultWebSecurityCondition 条件失效
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 构造注入使用@Autowired,set注入使用@Resource
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

    // 自定义认证过滤器
    @Bean
    public LoginKaptchaFilter loginKaptchaFilter() throws Exception {
        LoginKaptchaFilter loginKaptchaFilter = new LoginKaptchaFilter();
        // 动态绑定参数
        loginKaptchaFilter.setUsernameParameter("uname");
        loginKaptchaFilter.setPasswordParameter("passwd");
        loginKaptchaFilter.setKaptchaParameter("kaptcha");
        // 指定认证管理器
        loginKaptchaFilter.setAuthenticationManager(authenticationManager());
        // 指定认证url
        loginKaptchaFilter.setFilterProcessesUrl("/login");
        // 指定认证成功处理
        loginKaptchaFilter.setAuthenticationSuccessHandler(((request, response, authentication) -> {
            response.sendRedirect("/index");
        }));
        // 指定认证失败处理
        loginKaptchaFilter.setAuthenticationFailureHandler(((request, response, exception) -> {
            response.sendRedirect("/toLogin");
        }));
        return loginKaptchaFilter;
    }

    // 拦配置http拦截
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/toLogin").permitAll()
                .mvcMatchers("/vc.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/toLogin")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/toLogin")
                .and()
                .csrf()
                .disable();
        // 替换自定义过滤器
        http.addFilterAt(loginKaptchaFilter(), UsernamePasswordAuthenticationFilter.class);

    }

}
