package com.vinjcent.mapper;

import com.vinjcent.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User queryOneByUnameAndPwd(@Param("username") String username, @Param("password") String password);
}
