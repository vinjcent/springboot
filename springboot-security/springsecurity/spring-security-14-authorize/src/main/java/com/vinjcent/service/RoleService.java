package com.vinjcent.service;


import com.vinjcent.pojo.Role;

import java.util.List;

public interface RoleService {

    // 根据用户uid查询角色信息
    List<Role> queryRolesByUid(Integer uid);

}
