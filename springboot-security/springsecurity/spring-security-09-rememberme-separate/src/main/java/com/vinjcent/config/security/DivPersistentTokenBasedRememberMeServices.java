package com.vinjcent.config.security;

import org.springframework.core.log.LogMessage;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义记住我 services 实现类
 */
public class DivPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {


    /**
     * 自定义前后端分离获取 rememberMe 请求参数
     * @param request 请求
     * @param rememberMe 记住我参数
     * @return 返回boolean
     */
    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String rememberMe) {
        String paramValue = (String) request.getAttribute(rememberMe);
        if (paramValue != null) {
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
                    || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
                return true;
            }
        }
        this.logger.debug(
                LogMessage.format("Did not send remember-me cookie (principal did not set parameter '%s')", paramValue));
        return false;
    }

    public DivPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }
}
