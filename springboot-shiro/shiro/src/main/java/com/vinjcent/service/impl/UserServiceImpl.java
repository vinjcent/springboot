package com.vinjcent.service.impl;

import com.vinjcent.pojo.Permission;
import com.vinjcent.pojo.Role;
import com.vinjcent.pojo.User;
import com.vinjcent.mapper.UserMapper;
import com.vinjcent.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vinjcent
 * @since 2022-07-24 00:22:56
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    @Transactional(readOnly = true)
    public User queryOneByUsername(String username) {
        return baseMapper.queryOneByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> queryRolesByUserId(Integer uid) {
        return baseMapper.queryRolesByUserId(uid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Permission> queryPermsByRoleId(Integer rid) {
        return getBaseMapper().queryPermsByRoleId(rid);
    }
}
