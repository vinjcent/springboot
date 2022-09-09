package com.vinjcent.service.impl;

import com.vinjcent.mapper.UserMapper;
import com.vinjcent.pojo.User;
import com.vinjcent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("all")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User queryOneByUnameAndPwd(String username, String password) {
        User user = userMapper.queryOneByUnameAndPwd(username, password);
        if (user != null) return user;
        throw new RuntimeException("认证失败!");
    }
}
