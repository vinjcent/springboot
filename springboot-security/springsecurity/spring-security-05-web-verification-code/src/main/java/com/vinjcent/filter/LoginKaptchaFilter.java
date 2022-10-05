package com.vinjcent.filter;

import com.vinjcent.exception.KaptchaNotMatchException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义登录验证码 filter
 */
public class LoginKaptchaFilter extends UsernamePasswordAuthenticationFilter {

    private boolean postOnly = true;

    public static final String SPRING_SECURITY_FORM_KAPTCHA = "kaptcha";

    private String kaptchaParameter = SPRING_SECURITY_FORM_KAPTCHA;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 1.先判断是否为 POST 请求
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 2.从请求中获取验证码
        String verifyCode = request.getParameter(getKaptchaParameter());
        // 3.获取 session 中验证码进行比较
        String sessionVerifyCode = (String) request.getSession().getAttribute("kaptcha");
        // 4.与 session 中验证码进行比较
        if (!ObjectUtils.isEmpty(verifyCode) && !ObjectUtils.isEmpty(sessionVerifyCode) && verifyCode.equals(sessionVerifyCode)) {
            return super.attemptAuthentication(request, response);
        }
        // 5.如果匹配不上,抛出自定义异常
        throw new KaptchaNotMatchException("验证码不匹配!");
    }


    @Override
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public String getKaptchaParameter() {
        return kaptchaParameter;
    }

    public void setKaptchaParameter(String kaptchaParameter) {
        this.kaptchaParameter = kaptchaParameter;
    }
}
