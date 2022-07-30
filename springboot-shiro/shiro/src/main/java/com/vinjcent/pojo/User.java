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
@TableName("user")
@ApiModel(value = "User对象", description = "用户表")
public class User implements Serializable {

    @ApiModelProperty("用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("姓名")
    @TableField("`name`")
    private String name;

    @ApiModelProperty("用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty("密码")
    @TableField("`password`")
    private String password;

    @ApiModelProperty("盐")
    @TableField("salt")
    private String salt;

    @ApiModelProperty("状态,0为冻结,1为正常")
    @TableField("state")
    private Integer state;


}
