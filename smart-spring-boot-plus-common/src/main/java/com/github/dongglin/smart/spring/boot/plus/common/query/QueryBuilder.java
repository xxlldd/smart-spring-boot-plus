package com.github.dongglin.smart.spring.boot.plus.common.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.dongglin.smart.spring.boot.plus.common.annotation.BindQuery;
import com.github.dongglin.smart.spring.boot.plus.common.constant.Cons;
import com.github.dongglin.smart.spring.boot.plus.common.enums.Comparison;
import com.github.dongglin.smart.spring.boot.plus.common.enums.Strategy;
import com.github.dongglin.smart.spring.boot.plus.common.interfaces.ProtectFieldHandler;
import com.github.dongglin.smart.spring.boot.plus.common.parser.ParserCache;
import com.github.dongglin.smart.spring.boot.plus.common.utils.BeanUtils;
import com.github.dongglin.smart.spring.boot.plus.common.utils.ContextHolder;
import com.github.dongglin.smart.spring.boot.plus.common.utils.S;
import com.github.dongglin.smart.spring.boot.plus.common.utils.V;
import com.github.dongglin.smart.spring.boot.plus.common.vo.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.type.NullType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 11960
 * @ClassName QueryBuilder
 * @description: QueryWrapper构建器
 * @date 2023年07月18日
 * @version: 1.0
 */
@SuppressWarnings({"unchecked", "rawtypes", "JavaDoc"})
public class QueryBuilder {
    private static Logger log = LoggerFactory.getLogger(QueryBuilder.class);

    /**
     * Entity或者DTO对象转换为QueryWrapper
     *
     * @param dto
     * @param <DTO>
     * @return
     */
    public static <DTO> QueryWrapper toQueryWrapper(DTO dto) {
        return dtoToWrapper(dto, null, null);
    }

    /**
     * Entity或者DTO对象转换为QueryWrapper
     *
     * @param dto
     * @param pagination 分页
     * @param <DTO>
     * @return
     */
    public static <DTO> QueryWrapper toQueryWrapper(DTO dto, Pagination pagination) {
        return dtoToWrapper(dto, null, pagination);
    }

    /**
     * Entity或者DTO对象转换为QueryWrapper
     *
     * @param dto
     * @param fields 指定参与转换的属性值
     * @param <DTO>
     * @return
     */
    public static <DTO> QueryWrapper toQueryWrapper(DTO dto, Collection<String> fields) {
        return dtoToWrapper(dto, fields, null);
    }

    /**
     * Entity或者DTO对象转换为QueryWrapper
     *
     * @param dto
     * @param fields     指定参与转换的属性值
     * @param pagination 分页
     * @param <DTO>
     * @return
     */
    public static <DTO> QueryWrapper toQueryWrapper(DTO dto, Collection<String> fields, Pagination pagination) {
        return dtoToWrapper(dto, fields, pagination);
    }

    /**
     * 转换具体实现
     *
     * @param dto
     * @return
     */
    private static <DTO> QueryWrapper<?> dtoToWrapper(DTO dto, Collection<String> fields, Pagination pagination) {
        QueryWrapper<?> wrapper;
        // 转换
        LinkedHashMap<String, FieldAndValue> fieldValuesMap = extractNotNullValues(dto, fields, pagination);
        if (V.isEmpty(fieldValuesMap)) {
            return new QueryWrapper<>();
        }
        wrapper = new ExtQueryWrapper<>();
        // 构建 ColumnName
        List<AnnoJoiner> annoJoinerList = ParserCache.getBindQueryAnnos(dto.getClass());
        BiFunction<BindQuery, Field, String> buildColumnName = (bindQuery, field) -> {
            if (bindQuery != null) {
                String key = field.getName() + bindQuery;
                for (AnnoJoiner annoJoiner : annoJoinerList) {
                    if (key.equals(annoJoiner.getKey())) {
                        if (V.notEmpty(annoJoiner.getJoin())) {
                            // 获取注解Table
                            return annoJoiner.getAlias() + "." + annoJoiner.getColumnName();
                        }
                    }
                }
            }
            return BeanUtils.getColumnName(field);
        };
        // 忽略空字符串"",空集合等
        BiPredicate<Object, BindQuery> ignoreEmpty = (value, bindQuery) -> bindQuery != null &&
                (Strategy.IGNORE_EMPTY.equals(bindQuery.strategy()) && value instanceof String && S.isEmpty((String) value) // 忽略空字符串""
                        || Comparison.IN.equals(bindQuery.comparison()) && V.isEmpty(value)); // 忽略空集合
        // 获取Class类型
        Function<BindQuery, Class<?>> getClass = bindQuery -> bindQuery == null || bindQuery.entity() == NullType.class ? dto.getClass() : bindQuery.entity();
        // 获取属性名类型
        BiFunction<BindQuery, String, String> getFieldName = (bindQuery, defFieldName) -> bindQuery == null || S.isEmpty(bindQuery.column()) ? defFieldName : bindQuery.column();
        // 保护字段处理器
        ProtectFieldHandler protectFieldHandler = ContextHolder.getBean(ProtectFieldHandler.class);
        // 构建QueryWrapper
        for (Map.Entry<String, FieldAndValue> entry : fieldValuesMap.entrySet()) {
            FieldAndValue fieldAndValue = entry.getValue();
            Field field = fieldAndValue.getField();
            //忽略注解 @TableField(exist = false) 的字段
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null && !tableField.exist()) {
                continue;
            }
            //忽略字段
            BindQuery query = field.getAnnotation(BindQuery.class);
            if (query != null && query.ignore()) {
                continue;
            }
            BindQuery.List queryList = field.getAnnotation(BindQuery.List.class);
            Object value = fieldAndValue.getValue();
            // 构建Query
            if (queryList != null) {
                List<BindQuery> bindQueryList = Arrays.stream(queryList.value()).filter(e -> !ignoreEmpty.test(value, e)).collect(Collectors.toList());
                wrapper.and(V.notEmpty(bindQueryList), queryWrapper -> {
                    for (BindQuery bindQuery : bindQueryList) {
                        String columnName = buildColumnName.apply(bindQuery, field);
                        buildQuery(queryWrapper.or(), bindQuery, columnName, value);
                    }
                });
            } else {
                if (query == null && V.isEmpty(value)) {
                    continue;
                }
                if (ignoreEmpty.test(value, query)) {
                    continue;
                }
                String columnName = buildColumnName.apply(query, field);
                if (protectFieldHandler != null) {
                    Class<?> clazz = getClass.apply(query);
                    String fieldName = getFieldName.apply(query, entry.getKey());
                    if (ParserCache.getProtectFieldList(clazz).contains(fieldName)) {
                        buildQuery(wrapper, query, columnName, protectFieldHandler.encrypt(clazz, fieldName, value.toString()));
                        continue;
                    }
                }
                buildQuery(wrapper, query, columnName, value);
            }
        }
        return wrapper;
    }

    /**
     * 提取非空字段及值
     *
     * @param dto
     * @param fields
     * @param <DTO>
     * @return
     */
    private static <DTO> LinkedHashMap<String, FieldAndValue> extractNotNullValues(DTO dto, Collection<String> fields, Pagination pagination) {
        Class<?> dtoClass = dto.getClass();
        // 转换
        List<Field> declaredFields = BeanUtils.extractAllFields(dtoClass);
        List<String> extractOrderFieldNames = extractOrderFieldNames(pagination);
        // 结果map：<字段名,字段对象和值>
        LinkedHashMap<String, FieldAndValue> resultMap = new LinkedHashMap<>(declaredFields.size());
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            // 非指定属性，非逻辑删除字段，跳过；
            if (V.notContains(fields, fieldName)) {
                //Date 属性放过
                if (!V.equals(field.getType(), Date.class) && !V.equals(field.getType(), LocalDate.class) && !V.equals(field.getType(), LocalDateTime.class)) {
                    continue;
                }
            }
            //忽略static，以及final，transient
            int modifiers = field.getModifiers();
            boolean isStatic = Modifier.isStatic(modifiers);
            boolean isFinal = Modifier.isFinal(modifiers);
            boolean isTransient = Modifier.isTransient(modifiers);
            if (isStatic || isFinal || isTransient) {
                continue;
            }
            //打开私有访问 获取值
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(dto);
                if (V.isEmpty(value)) {
                    String prefix = V.equals(boolean.class, field.getType()) ? "is" : "get";
                    Method method = dtoClass.getMethod(prefix + S.capFirst(fieldName));
                    value = method.invoke(dto);
                }
            } catch (IllegalAccessException e) {
                log.error("通过反射获取属性值出错：{}", e.getMessage());
            } catch (NoSuchMethodException e) {
                log.debug("通过反射获取属性方法不存在：{}", e.getMessage());
            } catch (InvocationTargetException e) {
                log.warn("通过反射执行属性方法出错：{}", e.getMessage());
            }
            // 忽略逻辑删除字段，含有逻辑删除字段，并且值为false，则忽略
            if (field.isAnnotationPresent(TableLogic.class) && V.equals(false, value)) {
                continue;
            }
            BindQuery bindQuery = field.getAnnotation(BindQuery.class);
            Strategy strategy = bindQuery != null ? bindQuery.strategy() : Strategy.IGNORE_EMPTY;
            boolean collectThisField = false;
            // INCLUDE_NULL策略，包含null也收集
            if (strategy.equals(Strategy.INCLUDE_NULL)) {
                collectThisField = true;
            } else if (strategy.equals(Strategy.IGNORE_EMPTY) && V.notEmpty(value)) {
                collectThisField = true;
            } else if (strategy.equals(Strategy.INCLUDE_EMPTY) && value != null) {
                collectThisField = true;
            } else if (extractOrderFieldNames.contains(fieldName)) {
                collectThisField = true;
            }
            if (collectThisField) {
                resultMap.put(fieldName, new FieldAndValue(field, value));
            }
        }
        return resultMap;
    }


    /**
     * 是否为排序字段
     *
     * @param pagination
     * @return
     */
    private static List<String> extractOrderFieldNames(Pagination pagination) {
        if (pagination == null || V.isEmpty(pagination.getOrderBy())) {
            return Collections.emptyList();
        }
        // 解析排序
        // orderBy=name:DESC,age:ASC,birthdate
        String[] orderByFields = S.split(pagination.getOrderBy());
        List<String> orderFields = new ArrayList<>(orderByFields.length);
        for (String field : orderByFields) {
            if (field.contains(":")) {
                field = S.substringBefore(field, ":");
            }
            orderFields.add(field);
        }
        return orderFields;
    }

    /**
     * 建立条件
     *
     * @param wrapper    条件包装器
     * @param bindQuery 注解
     * @param columnName 列名
     * @param value      值
     */
    private static void buildQuery(QueryWrapper<?> wrapper, BindQuery bindQuery, String columnName, Object value) {
        Comparison comparison = bindQuery != null ? bindQuery.comparison() : Comparison.EQ;
        if(value == null) {
            if(bindQuery != null && bindQuery.strategy().equals(Strategy.INCLUDE_NULL) && comparison.equals(Comparison.EQ)) {
                wrapper.isNull(columnName);
            }
            return;
        }
        switch (comparison) {
            case EQ:
                wrapper.eq(columnName, value);
                break;
            case IN:
                if (value.getClass().isArray()) {
                    Object[] valueArray = (Object[]) value;
                    if (valueArray.length == 1) {
                        wrapper.eq(columnName, valueArray[0]);
                    } else if (valueArray.length >= 2) {
                        wrapper.in(columnName, valueArray);
                    }
                } else if (value instanceof Collection) {
                    wrapper.in(!((Collection) value).isEmpty(), columnName, (Collection<?>) value);
                } else {
                    log.warn("字段类型错误：IN仅支持List及数组.");
                }
                break;
            case NOT_IN:
                if (value.getClass().isArray()) {
                    Object[] valueArray = (Object[]) value;
                    if (valueArray.length == 1) {
                        wrapper.ne(columnName, valueArray[0]);
                    } else if (valueArray.length >= 2) {
                        wrapper.notIn(columnName, valueArray);
                    }
                } else if (value instanceof Collection) {
                    wrapper.notIn(!((Collection) value).isEmpty(), columnName, (Collection<?>) value);
                } else {
                    log.warn("字段类型错误：NOT_IN仅支持List及数组.");
                }
                break;
            case CONTAINS:
            case LIKE:
                wrapper.like(columnName, value);
                break;
            case STARTSWITH:
                wrapper.likeRight(columnName, value);
                break;
            case ENDSWITH:
                wrapper.likeLeft(columnName, value);
                break;
            case GT:
                wrapper.gt(columnName, value);
                break;
            case BETWEEN_BEGIN:
            case GE:
                wrapper.ge(columnName, value);
                break;
            case LT:
                wrapper.lt(columnName, value);
                break;
            case BETWEEN_END:
            case LE:
                wrapper.le(columnName, value);
                break;
            case BETWEEN:
                if (value.getClass().isArray()) {
                    Object[] valueArray = (Object[]) value;
                    if (valueArray.length == 1) {
                        wrapper.ge(columnName, valueArray[0]);
                    } else if (valueArray.length >= 2) {
                        wrapper.between(columnName, valueArray[0], valueArray[1]);
                    }
                } else if (value instanceof List) {
                    List<?> valueList = (List<?>) value;
                    if (valueList.size() == 1) {
                        wrapper.ge(columnName, valueList.get(0));
                    } else if (valueList.size() >= 2) {
                        wrapper.between(columnName, valueList.get(0), valueList.get(1));
                    }
                }
                // 支持逗号分隔的字符串
                else if (value instanceof String && ((String) value).contains(Cons.SEPARATOR_COMMA)) {
                    Object[] valueArray = ((String) value).split(Cons.SEPARATOR_COMMA);
                    wrapper.between(columnName, valueArray[0], valueArray[1]);
                } else {
                    wrapper.ge(columnName, value);
                }
                break;
            // 不等于
            case NOT_EQ:
                wrapper.ne(columnName, value);
                break;
            case IS_NULL:
                wrapper.isNull(columnName);
                break;
            case IS_NOT_NULL:
                wrapper.isNotNull(columnName);
                break;
            default:
                break;
        }
    }


}
