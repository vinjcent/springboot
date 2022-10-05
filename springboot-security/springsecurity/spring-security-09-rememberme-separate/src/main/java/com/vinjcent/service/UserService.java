package com.vinjcent.service;


import com.vinjcent.pojo.User;

/**
  * @author vinjcent
  * @description 针对表【user】的数据库操作Service
  * @createDate 2022-09-25 12:03:42
  */
public interface UserService {

    // 根据用户名username查询用户信息
    User queryUserByUsername(String username);

}
