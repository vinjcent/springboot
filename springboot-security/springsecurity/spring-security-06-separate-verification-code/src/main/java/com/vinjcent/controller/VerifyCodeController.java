package com.vinjcent.controller;

import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class VerifyCodeController {

    private final Producer producer;

    @Autowired
    public VerifyCodeController(Producer producer) {
        this.producer = producer;
    }

    @RequestMapping("/vc.jpg")
    public String verifyCode(HttpSession session) throws IOException {
        // 1.生成验证码
        String verifyCode = producer.createText();
        // 2.保存到 session(可以存入到redis当中)
        session.setAttribute("kaptcha", verifyCode);
        // 3.生成图片
        BufferedImage image = producer.createImage(verifyCode);
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(image, "jpg", fos);

        // 4.将生成的图片转为Base64格式返回给前端
        return Base64.encodeBase64String(fos.toByteArray());
    }
}
