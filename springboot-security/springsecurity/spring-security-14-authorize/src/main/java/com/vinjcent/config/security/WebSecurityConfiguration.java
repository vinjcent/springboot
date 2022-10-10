package com.vinjcent.config.security;

import com.vinjcent.config.security.meta.DivSecurityMetaSource;
import com.vinjcent.config.security.service.DivUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * 自定义 spring security 配置类
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 注入认证数据源
    private final DivUserDetailsService userDetailsService;

    // 注入url元数据数据源
    private final DivSecurityMetaSource securityMetaSource;

    @Autowired
    public WebSecurityConfiguration(DivUserDetailsService userDetailsService, DivSecurityMetaSource securityMetaSource) {
        this.userDetailsService = userDetailsService;
        this.securityMetaSource = securityMetaSource;
    }

    // 替换认证数据源
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

    // http 拦截
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 1.获取工厂对象
        ApplicationContext context = http.getSharedObject(ApplicationContext.class);
        // 2.设置自定义 url 权限元数据
        http.apply(new UrlAuthorizationConfigurer<>(context))
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        // 配置元数据信息
                        object.setSecurityMetadataSource(securityMetaSource);
                        // 是否拒绝公共资源的访问,设置为true,不允许访问公共资源
                        object.setRejectPublicInvocations(false);
                        return object;
                    }
                });

        // 开启表单验证
        http.formLogin();

        // 关闭csrf
        http.csrf().disable();
    }

}
