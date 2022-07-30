package com.vinjcent.config;

import com.vinjcent.pojo.Permission;
import com.vinjcent.pojo.Role;
import com.vinjcent.pojo.User;
import com.vinjcent.service.UserService;
import com.vinjcent.utils.MyByteSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;


public class UserRealm extends AuthorizingRealm {

    /*
        解决 xxx is not eligible for getting processed by all BeanPostProcessors
        (for example: not eligible for auto-proxying 问题
     */
    @Lazy // 由于某一个service实现了BeanPostProcessor接口,同时这个Service又依赖其他的Service导致的
    @Autowired
    UserService userService;

    // 执行授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        System.out.println("执行了授权doGetAuthorizationInfo");

        // 获得当前subject
        Subject subject = SecurityUtils.getSubject();
        // 获得当前的principal,也就是认证完后放入当前对象的信息
        User currentUser = (User) subject.getPrincipal();

        // 认证之后,如果前端shiro标签中有出现需要权限的标签,或者过滤器中某个链接需要权限,就会进行认证
        // 根据主身份信息获取"角色"和"权限"信息
        List<Role> roles = userService.queryRolesByUserId(currentUser.getId());
        if (!CollectionUtils.isEmpty(roles)){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            roles.forEach(viewRole -> {
                simpleAuthorizationInfo.addRole(viewRole.getName());
                List<Permission> perms = userService.queryPermsByRoleId(viewRole.getId());
                if (!CollectionUtils.isEmpty(perms)){
                    /*
                        添加权限
                        注意!如果数据库没有为该用户设置权限,该字段为null,会报错,需要手动添加一个
                     */
                    perms.forEach(viewPerm -> simpleAuthorizationInfo.addStringPermission(viewPerm.getName()));
                }
            });
            return simpleAuthorizationInfo;
        }
        return null;
    }

    // 执行认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了认证doGetAuthenticationInfo");

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        // 从数据库中查询该用户
        User user = userService.queryOneByUsername(token.getUsername());

        if (user != null){
            // 如果身份认证验证成功,返回一个AuthenticationInfo实现


            // 第一个参数为principal;
            // 第二个参数为从数据库中查出的用于验证的密码,shiro中密码验证不需要我们自己去做;
            // 第三个参数为盐值,根据盐值与输入的原密码进行加密之后,再与当前user.getPassword()进行匹配
            // 第四个参数为realm的名称
            return new SimpleAuthenticationInfo(user,
                                                user.getPassword(),
                                                // ByteSource.Util.bytes(user.getUsername() + user.getSalt()),
                                                new MyByteSource((user.getUsername() + user.getSalt()).getBytes()),
                                                this.getName());
        }
        // 如果不存在该用户,返回null,会抛出UnknownAccountException异常
        return null;
    }

    // ======================================重写和添加以下的方法======================================

    /**
     * 重写方法,清除当前用户的 "授权" 缓存
     * @param principals 当前用户
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 重写方法,清除当前用户的 "认证" 缓存
     * @param principals 当前用户
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    /**
     * 重写方法,清除当前用户的缓存
     * @param principals 当前用户
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    /**
     * 自定义方法: 清除所有 "授权" 缓存
     * 需要手动调用!
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 "认证" 缓存
     * 需要手动调用!
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的 "认证" 缓存和 "授权" 缓存
     * 需要手动调用!
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
