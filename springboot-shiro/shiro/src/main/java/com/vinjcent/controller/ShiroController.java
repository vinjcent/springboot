package com.vinjcent.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShiroController {


    // 跳到登录页
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "add";
    }

    @RequestMapping("/toDelete")
    public String toDelete(){
        return "delete";
    }

    @RequestMapping("/toQuery")
    public String toQuery(){
        return "query";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(){
        return "update";
    }

    // 跳到未授权页面
    @RequestMapping("/noauth")
    public String tonoauth(){
        return "noauth";
    }

    // 跳到首页
    @RequestMapping({"/", "/index"})
    public String toIndex(Model model){
        model.addAttribute("msg","hello shiro");
        return "index";
    }

    // 用于测试记住我和认证的区别
    @RequestMapping("/buy")
    public String buy(){
        Subject subject = SecurityUtils.getSubject();
        // 只有认证后才能访问,如果只是记住我则需要先登录
        if(!subject.isAuthenticated()){
            return "redirect:/toLogin";
        }
        return "add";
    }

    // 登录认证
    @RequestMapping("/login")
    public String login(String username,String password,Integer rememberMe,Model model){

        Subject subject = SecurityUtils.getSubject();
        // 令牌
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        if(rememberMe != null && rememberMe == 1){
            // 设置记住我功能
            token.setRememberMe(true);
        }

        try {
            //登录认证
            subject.login(token);
        }catch (UnknownAccountException e){
            model.addAttribute("msg","用户名不存在！");
            return "login";
        }catch (IncorrectCredentialsException e){
            model.addAttribute("msg","密码错误！");
            return "login";
        }catch (LockedAccountException e) {
            model.addAttribute("msg","账户已锁定！");
            return "login";
        } catch (ExcessiveAttemptsException e) {
            model.addAttribute("msg","用户名或密码错误次数过多!请30s后重试!");
            return "login";
        } catch (AuthenticationException e) {
            model.addAttribute("msg","用户名或密码不正确！");
            return "login";
        }
        if(subject.isAuthenticated()){
            System.out.println("认证成功");
            Session session = subject.getSession();
            // 将用户存入shiro里的session中
            session.setAttribute("currentUser", subject.getPrincipal());
            return "index";
        }else{
            token.clear();
            return "login";
        }

    }

    // 注销
    @RequestMapping("/logout")
    public String logout(){

        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "index";
    }




}
