/*
 * Copyright (c) 2015-2020, www.dibo.ltd (service@dibo.ltd).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.dongglin.smart.spring.boot.plus.common.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Bean相关处理工具类
 * @author mazc@dibo.ltd
 * @version v2.0
 * @date 2019/01/01
 */
@SuppressWarnings({"unchecked", "rawtypes", "JavaDoc", "unused"})
public class BeanUtils {
    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

     /**
     * 获取类所有属性（包含父类中属性）
     * @param clazz
     * @return
     */
    public static List<Field> extractAllFields(Class clazz){
        return extractClassFields(clazz, null);
    }

    /**
     * 获取类所有属性（包含父类中属性）
     * @param clazz
     * @return
     */
    public static List<Field> extractFields(Class<?> clazz, Class<? extends Annotation> annotation){
        return extractClassFields(clazz, annotation);
    }

    /**
     * 获取类的指定属性（包含父类中属性）
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field extractField(Class<?> clazz, String fieldName) {
        return ReflectionUtils.findField(clazz, fieldName);
    }

    /**
     * 获取数据表的列名（驼峰转下划线蛇形命名）
     * <br>
     * 列名取值优先级： @TableField.value > field.name
     *
     * @param field
     * @return
     */
    public static String getColumnName(Field field) {
        String columnName = null;
        if (field.isAnnotationPresent(TableField.class)) {
            columnName = field.getAnnotation(TableField.class).value();
        }
        else if(field.isAnnotationPresent(TableId.class)) {
            columnName = field.getAnnotation(TableId.class).value();
        }
        return S.getIfEmpty(columnName, () -> S.toSnakeCase(field.getName()));
    }

    /**
     * 获取目标类
     * @param instance
     * @return
     */
    public static Class<?> getTargetClass(Object instance) {
        return (instance instanceof Class) ? (Class<?>) instance : AopProxyUtils.ultimateTargetClass(instance);
    }

    /**
     * 从实例中获取目标对象的泛型定义类class
     * @param instance 对象实例
     * @param index
     * @return
     */
    public static Class getGenericityClass(Object instance, int index){
        Class hostClass = getTargetClass(instance);
        ResolvableType resolvableType = ResolvableType.forClass(hostClass).getSuperType();
        ResolvableType[] types = resolvableType.getGenerics();
        if(V.isEmpty(types) || index >= types.length){
            types = resolvableType.getSuperType().getGenerics();
        }
        if(V.notEmpty(types) && types.length > index){
            return types[index].resolve();
        }
        log.debug("无法从 {} 类定义中获取泛型类{}", hostClass.getName(), index);
        return null;
    }


    /**
     * 根据指定Key对list去重
     * @param list
     * @param getterFn
     * @param <T>
     * @return 去重后的list
     */
    public static <T> List<T> distinctByKey(List<T> list, Function<? super T, ?> getterFn){
        return list.stream().filter(distinctPredicate(getterFn)).collect(Collectors.toList());
    }

    /**
     * 去重的辅助方法
     * @param getterFn
     * @param <T>
     * @return
     */
    private static <T> Predicate<T> distinctPredicate(Function<? super T, ?> getterFn) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(getterFn.apply(t));
    }

    /***
     * 获取类对应的Lambda
     * @param fn
     * @return
     */
    public static SerializedLambda getSerializedLambda(Serializable fn){
        SerializedLambda lambda = null;
        try{
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            lambda = (SerializedLambda) method.invoke(fn);
        }
        catch (Exception e){
            log.error("获取SerializedLambda异常, class="+fn.getClass().getSimpleName(), e);
        }
        return lambda;
    }


    /**
     * 清除属性值值
     *
     * @param object        对象
     * @param fieldNameList 属性名称列表
     */
    public static void clearFieldValue(Object object, List<String> fieldNameList) {
        if (fieldNameList == null) {
            return;
        }
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        for (String fieldName : fieldNameList) {
            wrapper.setPropertyValue(fieldName, null);
        }
    }

    /**
     * 转换集合中的string类型id值为指定类型
     * @param values
     * @param fieldType
     * @return
     */
    public static Collection convertIdValuesToType(Collection<?> values, Class fieldType) {
        if(V.isEmpty(values)) {
            return values;
        }
        if(V.equals(values.iterator().next().getClass(), fieldType)) {
            return values;
        }
        Collection formatValues = new ArrayList(values.size());
        for(Object value : values) {
            formatValues.add(convertIdValueToType(value, fieldType));
        }
        return formatValues;
    }

    /**
     * 转换string类型id值为指定类型
     * @param value
     * @param fieldType
     * @return
     */
    public static Object convertIdValueToType(Object value, Class fieldType) {
        if(V.isEmpty(value)) {
            return null;
        }
        if(Long.class.equals(fieldType)) {
            return Long.parseLong(S.valueOf(value));
        }
        if(Integer.class.equals(fieldType)) {
            return Integer.parseInt(S.valueOf(value));
        }
        return value;
    }

    /**
     * 获取字段的真实类型（集合取泛型参数）
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Class<?> getFieldActualType(Class<?> clazz, String fieldName) {
        Field field = extractField(clazz, fieldName);
        if (field == null) {
            log.warn("class {} 中无字段 {}", clazz.getName(), fieldName);
            return null;
        }
        return getFieldActualType(field);
    }

    /**
     * 获取字段的真实类型（集合取泛型参数）
     * @param field
     * @return
     */
    public static Class<?> getFieldActualType(Field field) {
        Type genericType = field.getGenericType();
        if(genericType instanceof Class) {
            return (Class) genericType;
        }
        // 得到泛型里的class类型对象
        else if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            return (Class<?>)pt.getActualTypeArguments()[0];
        }
        else{
            log.warn("非预期的GenericType : {}", genericType.getTypeName());
            return null;
        }
    }

    /**
     * 初始化fields
     * @param beanClazz
     * @return
     */
    private static List<Field> extractClassFields(Class<?> beanClazz, Class<? extends Annotation> annotation){
        List<Field> fieldList = new ArrayList<>();
        Set<String> fieldNameSet = new HashSet<>();
        loopFindFields(beanClazz, annotation, fieldList, fieldNameSet);
        return fieldList;
    }

    /**
     * 循环向上查找fields
     * @param beanClazz
     * @param annotation
     * @param fieldList
     * @param fieldNameSet
     */
    private static void loopFindFields(Class<?> beanClazz, Class<? extends Annotation> annotation, List<Field> fieldList, Set<String> fieldNameSet){
        if(beanClazz == null) {
            return;
        }
        Field[] fields = beanClazz.getDeclaredFields();
        if (V.notEmpty(fields)) {
            for (Field field : fields) {
                // 被重写属性，以子类的为准
                if (!fieldNameSet.add(field.getName())) {
                    continue;
                }
                if (annotation == null || field.getAnnotation(annotation) != null) {
                    fieldList.add(field);
                }
            }
        }
        loopFindFields(beanClazz.getSuperclass(), annotation, fieldList, fieldNameSet);
    }

}
