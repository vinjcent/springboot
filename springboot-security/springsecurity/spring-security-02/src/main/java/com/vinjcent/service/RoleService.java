package com.vinjcent.service;


import com.vinjcent.pojo.Role;


import java.util.List;

/**
  * @author 86137
  * @description 针对表【role】的数据库操作Service
  * @createDate 2022-09-25 12:01:18
  */
public interface RoleService {

    // 根据用户uid查询角色信息
    List<Role> queryRolesByUid(Integer uid);

}
