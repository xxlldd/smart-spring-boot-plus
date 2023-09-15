package com.github.dongglin.smart.spring.boot.plus.common.query;

import java.lang.reflect.Field;

/**
 * @author 11960
 * @ClassName FieldAndValue
 * @description: 保存字段Field对象和字段值
 * @date 2023年07月18日
 * @version: 1.0
 */
public class FieldAndValue {
    private final Field field;
    private final Object value;

    public FieldAndValue(Field field, Object value) {
        this.field = field;
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }
}
