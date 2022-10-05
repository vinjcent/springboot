package com.vinjcent.mapper;

import com.vinjcent.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @author vinjcent
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2022-09-25 12:03:42
 */
@Mapper
@Repository
public interface UserMapper {

    // 根据用户名返回用户信息
    User queryUserByUsername(@Param("username") String username);

    // 根据用户名修改密码
    Integer updatePassword(@Param("username") String username, @Param("password") String password);

}
