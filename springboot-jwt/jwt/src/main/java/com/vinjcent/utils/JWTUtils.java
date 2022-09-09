package com.vinjcent.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {

    // 签名加密的密钥secret
    private static final String SIGNATURE = "!QW@$FS4&";

    /**
     * 生成token header.payload.signature
     * @param map payload携带的用户信息
     * @return  返回生成的token字符串
     */
    public static String getToken(Map<String, String> map) {

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);  // 默认7天过期

        // 创建JWT builder
        JWTCreator.Builder builder = JWT.create();

        // payload
        map.forEach(builder::withClaim);

        // 设置令牌过期时间、算法以及密钥

        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SIGNATURE));
    }

    /**
     * 验证token合法性
     * @param token 令牌
     * @return 返回解析之后的token对象
     */
    public static DecodedJWT verify(String token) {
        // 创建验证对象时,需要跟加密的算法以及密钥一致
        return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }

}
