package com.vinjcent.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vinjcent.utils.JWTUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取请求头中的token
        String token = request.getHeader("token");

        // 用于返回的验证token后的key-value
        Map<String, Object> map = new HashMap<>();

        try {
            // 验证令牌
            DecodedJWT verify = JWTUtils.verify(token);
            map.put("state", true);
            map.put("msg", "Success!");
            return true;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg", "Signature Error!");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg", "Expiration Error!");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "Algorithm Error!");
        } catch (InvalidClaimException e) {
            e.printStackTrace();
            map.put("msg", "Validation Error!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "Token is Invalid!");
        }
        // 设置状态
        map.put("state", false);

        // 将map转为json,依赖jackson
        String json = new ObjectMapper().writeValueAsString(map);
        // 设置响应内容格式
        response.setContentType("application/json;charset=UTF-8");
        // 将错误返回响应给前端
        response.getWriter().println(json);

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
