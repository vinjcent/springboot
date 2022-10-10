package com.vinjcent.mapper;


import com.vinjcent.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    List<Role> queryRolesByUid(@Param("uid") Integer uid);

}




