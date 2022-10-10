package com.vinjcent.mapper;

import com.vinjcent.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    // 根据用户名返回用户信息
    User queryUserByUsername(@Param("username") String username);

}
