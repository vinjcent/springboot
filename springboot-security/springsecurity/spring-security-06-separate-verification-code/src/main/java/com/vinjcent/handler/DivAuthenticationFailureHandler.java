package com.vinjcent.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义认证失败之后处理
 */
public class DivAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "登陆失败: " + exception.getMessage());
        result.put("status", 500);
        response.setContentType("application/json;charset=UTF-8");
        // 响应返回状态
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String info = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(info);
    }
}
