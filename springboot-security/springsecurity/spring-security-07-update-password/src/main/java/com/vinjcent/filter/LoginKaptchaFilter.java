package com.vinjcent.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinjcent.exception.KaptchaNotMatchException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 自定义登录验证码 filter
 */
public class LoginKaptchaFilter extends UsernamePasswordAuthenticationFilter {

    private boolean postOnly = true;

    public static final String SPRING_SECURITY_FORM_KAPTCHA = "kaptcha";

    private String kaptchaParameter = SPRING_SECURITY_FORM_KAPTCHA;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 1.判断是否满足 POST 类型的请求
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 2.判断使用的数据格式类型是否是json
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            // 如果是json格式,需要转化成对象并从中获取用户输入的用户名和密码进行认证 {"username": "root","password": "123"}
            try {
                Map<String, String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                // 将用户名(username)和密码(password)通过动态传递的方式,进行获取
                // getUsernameParameter()、getPasswordParameter()是父类的方法,通过父类设置这两个属性的值
                String username = userInfo.get(getUsernameParameter());
                String password = userInfo.get(getPasswordParameter());
                System.out.println("用户名: " + username + " 密码: " + password);
                // 生成用户令牌
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
                setDetails(request, token);
                // 为了保证自定义的过滤器拥有 AuthenticationManager,我们还需手动配置一个
                return this.getAuthenticationManager().authenticate(token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return super.attemptAuthentication(request, response);
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
