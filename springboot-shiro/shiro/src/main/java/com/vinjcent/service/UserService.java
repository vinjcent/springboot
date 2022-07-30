package com.vinjcent.service;

import com.vinjcent.pojo.Permission;
import com.vinjcent.pojo.Role;
import com.vinjcent.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vinjcent
 * @since 2022-07-24 00:22:56
 */
public interface UserService extends IService<User> {

    User queryOneByUsername(String username);

    List<Role> queryRolesByUserId(Integer uid);

    List<Permission> queryPermsByRoleId(Integer rid);

}
