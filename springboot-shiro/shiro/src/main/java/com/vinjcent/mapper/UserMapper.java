package com.vinjcent.mapper;

import com.vinjcent.pojo.Permission;
import com.vinjcent.pojo.Role;
import com.vinjcent.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vinjcent
 * @since 2022-07-24 00:22:56
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    User queryOneByUsername(@Param("username") String username);

    List<Role> queryRolesByUserId(@Param("uid") Integer uid);

    List<Permission> queryPermsByRoleId(@Param("rid") Integer rid);

}
