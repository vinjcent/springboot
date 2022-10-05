package com.vinjcent.config.security;

import com.vinjcent.pojo.Role;
import com.vinjcent.pojo.User;
import com.vinjcent.service.RoleService;
import com.vinjcent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class DivUserDetailsService implements UserDetailsService, UserDetailsPasswordService {

    // dao ===> springboot + mybatis
    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public DivUserDetailsService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.查询用户
        User user = userService.queryUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户名不正确!");
        // 2.查询权限信息
        List<Role> roles = roleService.queryRolesByUid(user.getId());
        user.setRoles(roles);
        return user;
    }

    // 默认使用 DelegatingPasswordEncoder,对密码进行更新时,使用的时 Bcrypt 加密
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Integer result = userService.updatePassword(user.getUsername(), newPassword);
        if (result > 0) {
            ((User) user).setPassword(newPassword);
        }
        return user;
    }
}
