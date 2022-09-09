package com.vinjcent.service;

import com.vinjcent.pojo.User;

public interface UserService {

    User queryOneByUnameAndPwd(String username, String password);

}
