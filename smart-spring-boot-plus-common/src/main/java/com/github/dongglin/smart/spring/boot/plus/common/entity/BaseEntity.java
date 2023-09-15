package com.github.dongglin.smart.spring.boot.plus.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gitee.sunchenbin.mybatis.actable.annotation.*;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity基础父类
 */
@Getter
@Setter
@ApiModel
public class BaseEntity {

    private static final long serialVersionUID = 10203L;

    /**
     * 默认主键字段id，自增主键
     */
    @IsKey
    @IsAutoIncrement
    @IsNotNull
    @Column(length = 38)
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    @ColumnComment("主键")
    private Long id;

    /**
     * 默认字段GUID，类型为String型雪花id
     */
    @Column(name = "GUID", length = 36)
    @ApiModelProperty("全局唯一标识符")
    @ColumnComment("全局唯一标识符")
    private String guid;

    /**
     * 默认逻辑删除标记，is_deleted=0有效
     */
    @TableLogic
    @JsonIgnore
    @TableField(value = "delete_flag", select = false)
    @ColumnComment("逻辑删除1 0未删除")
    @Column(type = MySqlTypeConstant.INT, length = 1)
    private Integer deleteFlag = 0;

    /**
     * 默认记录创建时间字段，新建时由数据库赋值
     */
    @ColumnComment("创建时间")
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT, value = "create_date")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_date")
    @ColumnComment("更新时间")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
