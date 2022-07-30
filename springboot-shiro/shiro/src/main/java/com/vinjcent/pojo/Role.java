package com.vinjcent.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author vinjcent
 * @since 2022-07-24 00:22:56
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@TableName("role")
@ApiModel(value = "Role对象", description = "角色表")
public class Role implements Serializable {

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("角色名")
    @TableField("`name`")
    private String name;


}
