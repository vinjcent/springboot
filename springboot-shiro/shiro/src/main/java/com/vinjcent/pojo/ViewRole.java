package com.vinjcent.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author vinjcent
 * @since 2022-07-27 22:15:23
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("viewRole")
@ApiModel(value = "ViewRole对象", description = "### 角色记录表")
public class ViewRole implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("userId")
    private Integer userId;

    @TableField("roleId")
    private Integer roleId;


}
