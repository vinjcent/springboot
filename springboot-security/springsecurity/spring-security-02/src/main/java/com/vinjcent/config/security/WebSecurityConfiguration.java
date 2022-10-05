package com.vinjcent.config.security;

import com.vinjcent.handler.DivAuthenticationFailureHandler;
import com.vinjcent.handler.DivAuthenticationSuccessHandler;
import com.vinjcent.handler.DivLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

/**
 *  重写 WebSecurityConfigurerAdapter 类使得默认 DefaultWebSecurityCondition 条件失效
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DivUserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfiguration(DivUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 指定单一的加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 注意密码不同区别
    @Bean
    public UserDetailsService userDetailsService() {
        // 定义内存用户信息管理者对象,将用户信息存储在内存当中
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        // 创建用户对象
        UserDetails userDetails = User.withUsername("aaa").password("{bcrypt}$2a$10$AiUtLkjsyQ7m3oHVu6hZs.10/lf4ibUDs8ScmSC.XXRNNywsv8CjS").roles("admin").build();
        // 将用户对象交由用户管理者去管理
        userDetailsService.createUser(userDetails);
        return userDetailsService;
    }


    // 在之前查看源码中发现,当发现有 UserDetailsService.class 这个bean的时候, UserDetailsServiceAutoConfiguration 就会失效
    //@Bean
    //public UserDetailsService userDetailsService() {
    //    // 定义内存用户信息管理者对象,将用户信息存储在内存当中
    //    InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
    //    // 创建用户对象
    //    UserDetails userDetails = User.withUsername("aaa").password("{noop}123").roles("admin").build();
    //    // 将用户对象交由用户管理者去管理
    //    userDetailsService.createUser(userDetails);
    //    return userDetailsService;
    //}

    // 方式一: springboot 对 security 默认配置中,进行自动配置时,自动在工厂中创建全局 AuthenticationManager
    // 相当于将默认已注入的bean进行属性设置,不妨直接创建一个bean ===> UserDetailsService 使得默认配置失效即可
    //@Autowired
    //public void initialize(AuthenticationManagerBuilder builder) throws Exception {
    //    System.out.println("springboot默认配置: " + builder);
    //    InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
    //    UserDetails userDetails = User.withUsername("aaa").password("{noop}123").roles("admin").build();
    //    userDetailsService.createUser(userDetails);
    //    builder.userDetailsService(userDetailsService);
    //}

    // 方式二: 自定义 AuthenticationManager(推荐)
    // 自定义的 AuthenticationManager 将会覆盖默认的配置,优先级更高(不支持自定义组件注入)
    @Override
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService);
    }

    // 作用: 用来将自定义的 AuthenticationManager 在工厂中进行暴露,可以在任何位置注入
    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .mvcMatchers("/toLogin").permitAll()    // 放行登录页面
                .mvcMatchers("/index").permitAll()      // 放行资源
                .anyRequest().authenticated()   // 【注意】所有放行的资源放在任何拦截请求前面
                .and()                          // 链式编程,继续拼接
                .formLogin()
                .loginPage("/toLogin")          // 指定登录页面,【注意】一旦指定登陆页面之后,必须指定登录请求url
                .loginProcessingUrl("/login")   // 指定处理登录请求的url
                .usernameParameter("uname")     // 指定认证用户名参数的名称
                .passwordParameter("passwd")    // 指定认证密码参数的名称
                // .successForwardUrl("/hello")    // 认证成功后跳转的路径(转发),始终在认证成功之后跳转到指定路径
                // .defaultSuccessUrl("/index", true)    // 认证成功之后的跳转(重定向),如果在访问权限资源之前被拦截,那么认证之后将会跳转之前先访问的资源
                .successHandler(new DivAuthenticationSuccessHandler())  // 认证成功时处理,前后端分离解决方案
                // .failureForwardUrl("/toLogin")      // 认证失败后跳转的路径(转发)
                // .failureUrl("/toLogin")             // 认证失败后跳转的路径(重定向)
                .failureHandler(new DivAuthenticationFailureHandler())  // 认证失败时处理,前后端分离解决方案
                .and()
                .logout()                       // 注销登录的logout
                // .logoutUrl("/logout")           // 指定注销的路径url   【注意】请求方式类型必须是GET
                //.logoutRequestMatcher(new OrRequestMatcher(     // 配置多个注销登录的请求
                //        new AntPathRequestMatcher("/aLogout", "GET"),
                //        new AntPathRequestMatcher("/bLogout", "POST")
                //))
                .logoutSuccessHandler(new DivLogoutSuccessHandler())    // 成功退出登录时,前后端分离解决方案
                .invalidateHttpSession(true)    // 默认开启,会话清除
                .clearAuthentication(true)      // 默认开启,清除认证标记
                .logoutSuccessUrl("/toLogin")   // 注销登录成功之后跳转的页面
                .and()
                .csrf().disable();              // 禁止 csrf 跨站请求保护
    }

}
