package com.vinjcent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SpringSecurity02ApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123");
        System.out.println(encode);
        // 这里的"10"代表散列次数,在创建 BCryptPasswordEncoder 时,可以设置参数
        // 第一次加密: $2a$10$oCDDewSNLloZlagnFW0SYexBBBl6QnXwspj2..WZGNwO7tv7AbTPy
        // 第二次加密: $2a$10$EeuYmlPFpuxJHDTe.jXRzuUrDRu0YrChz4NqUVu5n6BqTZgtc/aoS
        // 第三次加密: $2a$10$AiUtLkjsyQ7m3oHVu6hZs.10/lf4ibUDs8ScmSC.XXRNNywsv8CjS
    }

}
