package com.vinjcent;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SignatureException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SpringbootJwtApplicationTests {


    // 生成令牌
    @Test
    void contextLoads() {

        Map<String, Object> map = new HashMap();

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 20);  // 设置20s之后过期

        // 生成token令牌
        String token = JWT.create()
                .withHeader(map)        // header
                .withClaim("userId", 1)     // payload
                .withClaim("username", "zhangsan")
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256("!QW@$FS4&"));   // signature
        System.out.println(token);

    }

    // 生成令牌
    @Test
    void createToken() {

        Map<String, Object> map = new HashMap();

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 60);  // 设置20s之后过期

        // 生成token令牌
        String token = JWT.create()
                .withHeader(map)                        // header
                .withClaim("userId", 1)     // payload
                .withClaim("username", "zhangsan")
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256("!QW@$FS4&"));   // signature
        System.out.println(token);

    }

    @Test
    // 解析令牌
    void decodeToken() {
        // 创建验证对象时,需要跟加密的算法以及密钥一致
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!QW@$FS4&")).build();
        // 参数为token的值
        DecodedJWT verify = jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NjI2NDkxMzUsInVzZXJJZCI6MSwidXNlcm5hbWUiOiJ6aGFuZ3NhbiJ9.8dCuxyaMOkU3ylyLw98YGkuaCVznfrG8lDI5QQCYwIw");// 输入对应的token值
        // 注意这里必须要根据在设置的payload参数类型一致,不然会显示为null
        System.out.println(verify.getClaim("userId").asInt());
        System.out.println(verify.getClaim("username").asString());


    }

}
