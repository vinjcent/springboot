package com.vinjcent.mapper;

import com.vinjcent.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
public interface RoleMapper extends BaseMapper<Role> {

}
