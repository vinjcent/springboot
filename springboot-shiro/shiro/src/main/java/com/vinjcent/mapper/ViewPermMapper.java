package com.vinjcent.mapper;

import com.vinjcent.pojo.ViewPerm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vinjcent
 * @since 2022-07-27 22:15:22
 */
@Mapper
@Repository
public interface ViewPermMapper extends BaseMapper<ViewPerm> {

}
