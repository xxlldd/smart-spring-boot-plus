package com.github.dongglin.smart.spring.boot.plus.common.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.github.dongglin.smart.spring.boot.plus.common.annotation.BindQuery;
import com.github.dongglin.smart.spring.boot.plus.common.enums.Comparison;
import com.github.dongglin.smart.spring.boot.plus.common.parser.ParserCache;
import com.github.dongglin.smart.spring.boot.plus.common.utils.S;
import com.github.dongglin.smart.spring.boot.plus.common.utils.V;
import lombok.Getter;
import lombok.Setter;

import javax.lang.model.type.NullType;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * BindQuery注解连接器
 * @author Mazc@dibo.ltd
 * @version v2.0
 * @date 2020/04/16
 */
@Getter @Setter
public class AnnoJoiner implements Serializable {
    private static final long serialVersionUID = 5998965277333389063L;

    public AnnoJoiner(Field field, BindQuery query){
        this.key = field.getName() + query;
        this.fieldName = field.getName();
        this.comparison = query.comparison();
        // 列名
        if (V.notEmpty(query.column())) {
            this.columnName = query.column();
        }
        else if (field.isAnnotationPresent(TableField.class)) {
            this.columnName = field.getAnnotation(TableField.class).value();
        }
        if(V.isEmpty(this.columnName)){
            this.columnName = S.toSnakeCase(field.getName());
        }
        // join 表名
        if(!NullType.class.equals(query.entity())){
            this.join = ParserCache.getEntityTableName(query.entity());
        }
        // 条件
        if(V.notEmpty(query.condition())){
            this.condition = query.condition();
        }
    }

    private String key;

    private Comparison comparison;

    private String fieldName;

    private String columnName;

    private String condition;

    private String join;
    /**
     * 别名
     */
    private String alias;
    /**
     * on条件
     */
    private String onSegment;

    /**
     * 中间表
     */
    private String middleTable;

    /**
     * 中间表别名
     */
    public String getMiddleTableAlias(){
        if(middleTable != null && alias != null){
            return alias+"m";
        }
        return null;
    }
    /**
     * 中间表on
     */
    private String middleTableOnSegment;

    /**
     * 解析
     */
    public void parse(){
        // 解析查询
        JoinConditionManager.parseJoinCondition(this);
    }
}
