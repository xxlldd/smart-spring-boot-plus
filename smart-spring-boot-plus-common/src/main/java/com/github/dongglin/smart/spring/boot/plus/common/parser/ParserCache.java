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
package com.github.dongglin.smart.spring.boot.plus.common.parser;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.dongglin.smart.spring.boot.plus.common.annotation.BindQuery;
import com.github.dongglin.smart.spring.boot.plus.common.annotation.ProtectField;
import com.github.dongglin.smart.spring.boot.plus.common.cache.BindingCacheManager;
import com.github.dongglin.smart.spring.boot.plus.common.query.AnnoJoiner;
import com.github.dongglin.smart.spring.boot.plus.common.utils.BeanUtils;
import com.github.dongglin.smart.spring.boot.plus.common.utils.S;
import com.github.dongglin.smart.spring.boot.plus.common.utils.V;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * @author 11960
 * @ClassName ParserCache
 * @description:  对象中的绑定注解 缓存管理类
 * @date 2023年07月18日
 * @version: 1.0
 */
@SuppressWarnings({"rawtypes", "JavaDoc"})
@Slf4j
public class ParserCache {

    /**
     * dto类-BindQuery注解的缓存
     */
    private static final Map<String, List<AnnoJoiner>> dtoClassBindQueryCacheMap = new ConcurrentHashMap<>();
    /**
     * 保护字段缓存
     */
    private static final Map<String, List<String>> PROTECT_FIELD_MAP = new ConcurrentHashMap<>();

    /**
     * 当前DTO是否有Join绑定
     * @param dto dto对象
     * @param fieldNameSet 有值属性集合
     * @param <DTO>
     * @return
     */
    public static <DTO> boolean hasJoinTable(DTO dto, Collection<String> fieldNameSet){
        List<AnnoJoiner> annoList = getBindQueryAnnos(dto.getClass());
        if(V.notEmpty(annoList)){
            for(AnnoJoiner anno : annoList){
                if(V.notEmpty(anno.getJoin()) && V.contains(fieldNameSet, anno.getFieldName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取dto类中定义的BindQuery注解
     * @param dtoClass
     * @return
     */
    public static List<AnnoJoiner> getBindQueryAnnos(Class<?> dtoClass){
        String dtoClassName = dtoClass.getName();
        if(dtoClassBindQueryCacheMap.containsKey(dtoClassName)){
            return dtoClassBindQueryCacheMap.get(dtoClassName);
        }
        // 初始化
        List<AnnoJoiner> annos = new ArrayList<>();
        AtomicInteger index = new AtomicInteger(1);
        Map<String, String> joinOn2Alias = new HashMap<>(8);
        // 构建AnnoJoiner
        BiConsumer<Field, BindQuery> buildAnnoJoiner = (field, query) -> {
            AnnoJoiner annoJoiner = new AnnoJoiner(field, query);
            // 关联对象，设置别名
            if (V.notEmpty(annoJoiner.getJoin())) {
                String key = annoJoiner.getJoin() + ":" + annoJoiner.getCondition();
                String alias = joinOn2Alias.get(key);
                if (alias == null) {
                    alias = "r" + index.getAndIncrement();
                    annoJoiner.setAlias(alias);
                    joinOn2Alias.put(key, alias);
                } else {
                    annoJoiner.setAlias(alias);
                }
                annoJoiner.parse();
            }
            annos.add(annoJoiner);
        };
        for (Field field : BeanUtils.extractFields(dtoClass, BindQuery.class)) {
            BindQuery query = field.getAnnotation(BindQuery.class);
            // 不可能为null
            if (query.ignore()) {
                continue;
            }
            buildAnnoJoiner.accept(field, query);
        }
        for (Field field : BeanUtils.extractFields(dtoClass, BindQuery.List.class)) {
            BindQuery.List queryList = field.getAnnotation(BindQuery.List.class);
            for (BindQuery bindQuery : queryList.value()) {
                buildAnnoJoiner.accept(field, bindQuery);
            }
        }
        dtoClassBindQueryCacheMap.put(dtoClassName, annos);
        return annos;
    }

    /**
     * 获取注解joiner
     * @param dtoClass
     * @param fieldNames
     * @return
     */
    public static List<AnnoJoiner> getAnnoJoiners(Class<?> dtoClass, Collection<String> fieldNames) {
        List<AnnoJoiner> annoList = getBindQueryAnnos(dtoClass);
        // 不过滤  返回全部
        if(fieldNames == null){
            return annoList;
        }
        // 过滤
        if(V.notEmpty(annoList)){
            List<AnnoJoiner> matchedAnnoList = new ArrayList<>();
            for(AnnoJoiner anno : annoList){
                if(fieldNames.contains(anno.getFieldName())){
                    matchedAnnoList.add(anno);
                }
            }
            return matchedAnnoList;
        }
        return Collections.emptyList();
    }


    /**
     * 获取entity对应的表名
     * @param entityClass
     * @return
     */
    public static String getEntityTableName(Class<?> entityClass){
        EntityInfoCache entityInfoCache = BindingCacheManager.getEntityInfoByClass(entityClass);
        if(entityInfoCache != null){
            return entityInfoCache.getTableName();
        }
        else{
            TableName tableNameAnno = AnnotationUtils.findAnnotation(entityClass, TableName.class);
            if(tableNameAnno != null && V.notEmpty(tableNameAnno.value())){
                return tableNameAnno.value();
            }
            else{
                return S.toSnakeCase(entityClass.getSimpleName());
            }
        }
    }

    /**
     * 获取该类保护字段属性名列表
     *
     * @param clazz 类型
     * @return 属性名列表
     */
    @NonNull
    public static List<String> getProtectFieldList(@NonNull Class<?> clazz) {
        return PROTECT_FIELD_MAP.computeIfAbsent(clazz.getName(), k -> {
            List<String> protectFieldList = new ArrayList<>(4);
            for (Field field : BeanUtils.extractFields(clazz, ProtectField.class)) {
                if (!field.getType().isAssignableFrom(String.class)) {
                    log.error("`@ProtectField` 仅支持 String 类型字段。");
                    continue;
                }
                protectFieldList.add(field.getName());
            }
            return protectFieldList.isEmpty() ? Collections.emptyList() : protectFieldList;
        });
    }
}
