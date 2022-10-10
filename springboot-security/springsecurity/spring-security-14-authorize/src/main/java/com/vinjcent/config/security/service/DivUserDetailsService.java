package com.vinjcent.config.security.service;

import com.vinjcent.pojo.Role;
import com.vinjcent.pojo.User;
import com.vinjcent.service.RoleService;
import com.vinjcent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class DivUserDetailsService implements UserDetailsService {

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
        if (ObjectUtils.isEmpty(user)) throw new UsernameNotFoundException("用户名不存在!");
        // 2.查询权限信息
        List<Role> roles = roleService.queryRolesByUid(user.getId());
        user.setRoles(roles);
        return user;
    }
}
