package com.vinjcent.controller;

import com.vinjcent.pojo.User;
import com.vinjcent.service.UserService;
import com.vinjcent.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 登录请求
    @PostMapping("/login")
    public Map<String, Object> login(String username, String password) {
        log.info("用户名: [{}]", username);
        log.info("密码: [{}]", password);

        Map<String, Object> map = new HashMap<>();

        try {
            User user = userService.queryOneByUnameAndPwd(username, password);

            // 有效负载,携带用户参数
            Map<String, String> payload = new HashMap<>();
            payload.put("id", String.valueOf(user.getId()));
            payload.put("username", user.getUsername());
            // 生成JWT的令牌
            String token = JWTUtils.getToken(payload);
            map.put("state", true);
            map.put("msg", "认证成功!");
            map.put("token", token);
        } catch (Exception e) {
            map.put("state", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }

    // 验证请求
    @PostMapping("/test")
    public Map<String, Object> testRequest() {
        Map<String, Object> map = new HashMap<>();
        // 处理自己的业务逻辑 TODO
        map.put("state", true);
        map.put("msg", "Success!");
        return map;
    }
}
