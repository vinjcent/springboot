package com.vinjcent.service.impl;


import com.vinjcent.mapper.UserMapper;
import com.vinjcent.pojo.User;
import com.vinjcent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public User queryUserByUsername(String username) {
        return userMapper.queryUserByUsername(username);
    }
}
