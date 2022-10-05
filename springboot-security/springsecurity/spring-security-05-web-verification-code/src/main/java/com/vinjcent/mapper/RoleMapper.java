package com.vinjcent.mapper;


import com.vinjcent.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author vinjcent
 * @description 针对表【role】的数据库操作Mapper
 * @createDate 2022-09-25 12:01:18
 */
@Mapper
@Repository
public interface RoleMapper {

    List<Role> queryRolesByUid(@Param("uid") Integer uid);

}




