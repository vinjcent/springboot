package com.vinjcent.service.impl;


import com.vinjcent.mapper.UserMapper;
import com.vinjcent.pojo.User;
import com.vinjcent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
  * @author vinjcent
  * @description 针对表【user】的数据库操作Service实现
  * @createDate 2022-09-25 12:03:42
  */
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
