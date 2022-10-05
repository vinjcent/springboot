package com.vinjcent.service.impl;

import com.vinjcent.mapper.RoleMapper;
import com.vinjcent.pojo.Role;
import com.vinjcent.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  * @author vinjcent
  * @description 针对表【role】的数据库操作Service实现
  * @createDate 2022-09-25 12:01:18
  */
@Service
public class RoleServiceImpl implements RoleService {


    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public List<Role> queryRolesByUid(Integer uid) {
        return roleMapper.queryRolesByUid(uid);
    }
}




