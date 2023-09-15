package com.github.dongglin.smart.spring.boot.plus.common.vo;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.dongglin.smart.spring.boot.plus.common.constant.BaseConfig;
import com.github.dongglin.smart.spring.boot.plus.common.constant.Cons;
import com.github.dongglin.smart.spring.boot.plus.common.utils.S;
import com.github.dongglin.smart.spring.boot.plus.common.utils.V;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 11960
 * @ClassName Pagination
 * @description: 分页
 * @date 2023年07月18日
 * @version: 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class Pagination {
    private static final Logger log = LoggerFactory.getLogger(Pagination.class);

    private static final long serialVersionUID = -4083929594112114522L;

    /***
     * 当前页
     */
    private int pageIndex = 1;
    /***
     * 默认每页数量10
     */
    private int pageSize = BaseConfig.getPageSize();
    /***
     * count总数
     */
    private long totalCount = 0;
    /**
     * 默认排序 - ID降序
     */
    public static final String ORDER_BY_ID_DESC = Cons.FieldName.id.name() + ":" + Cons.ORDER_DESC;
    /**
     * 默认排序 - 创建时间降序
     */
    public static final String ORDER_BY_CREATE_TIME_DESC = Cons.FieldName.createTime.name() + ":" + Cons.ORDER_DESC;

    /**
     * 排序
     */
    private String orderBy;

    public Pagination() {
    }

    /***
     * 指定当前页数
     */
    public Pagination(int pageIndex) {
        setPageIndex(pageIndex);
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 1000) {
            log.warn("分页pageSize过大，将被调整为默认限值，请检查调用是否合理！pageSize=" + pageSize);
            pageSize = 1000;
        }
        this.pageSize = pageSize;
    }

    /***
     * 获取总的页数
     * @return
     */
    public int getTotalPage() {
        if (totalCount <= 0) {
            return 0;
        }
        return (int) Math.ceil((float) totalCount / pageSize);
    }

    /**
     * 清空默认排序
     */
    public void clearOrder() {
        // 是否为默认排序
        orderBy = null;
    }

    /**
     * 转换为IPage
     *
     * @param <T>
     * @return
     */
    public <T> Page<T> toPage(Class<?> entityClass) {
        List<OrderItem> orderItemList = null;
        // 解析排序
        if (ObjectUtil.isNotEmpty(this.orderBy)) {
            orderItemList = new ArrayList<>();
            // orderBy=name:DESC,age:ASC,birthdate
            String[] orderByFields = S.split(this.orderBy);
            for (String field : orderByFields) {
                V.securityCheck(field);
                if (field.contains(":")) {
                    String[] fieldAndOrder = S.split(field, ":");
                    String fieldName = fieldAndOrder[0];
                    String columnName = S.toSnakeCase(fieldName);
                    if (Cons.ORDER_DESC.equalsIgnoreCase(fieldAndOrder[1])) {
                        orderItemList.add(OrderItem.desc(columnName));
                    } else {
                        orderItemList.add(OrderItem.asc(columnName));
                    }
                } else {
                    orderItemList.add(OrderItem.asc(S.toSnakeCase(field)));
                }
            }
        }
        Page<T> page = new Page<T>()
                .setCurrent(getPageIndex())
                .setSize(getPageSize())
                // 如果前端传递过来了缓存的总数，则本次不再count统计
                .setTotal(getTotalCount() > 0 ? -1 : getTotalCount());
        if (orderItemList != null) {
            page.addOrder(orderItemList);
        }
        return page;
    }

    /**
     * 是否为分页参数
     * @param paramName
     * @return
     */
    public static boolean isPaginationParam(String paramName){
        return Cons.PaginationParam.isPaginationParam(paramName);
    }
}
