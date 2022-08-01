package com.vinjcent;


import com.vinjcent.pojo.User;
import com.vinjcent.service.UserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class ShiroApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
    }


    @Test
    void test01(){

    /*
        测试用例
        加密名称"md5" 大写小写都没有关系,但必须是加密规定的名称,不可自定义!
        username = vinjcent
        password = 123456
        salt = username + time
        散列次数1024
        md5Pwd为加密之后的密码,也就是存入数据库的密码
     */
        String username = "totoro";   // 原用户名
        String password = "123456";     // 原密码
        String currentTime = String.valueOf(System.currentTimeMillis());   // 将当前时间作为盐值一部分,真正的salt是username+currentTime,这里可以自定义
        // 根据SimpleHash()得到加密字符后,转为Hex编码字符串，也可以使用Base64字符串
        String md5Pwd = new SimpleHash("MD5", password,
                ByteSource.Util.bytes(username + currentTime), 1024)
                .toHex();
        //String md5Pwd = new SimpleHash("MD5", password, username + currentTime, 1024).toBase64();
        userService.save(new User(null, "test", username, md5Pwd, currentTime,null));

    }

}
