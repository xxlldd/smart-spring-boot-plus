package com.github.dongglin.smart.spring.boot.plus.core.framework.page;

import com.github.dongglin.smart.spring.boot.plus.core.framework.query.DataRangeQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 排序查询参数
 *
 * @author DongGL
 * @since 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "排序查询参数")
public abstract class BaseOrderQuery extends DataRangeQuery {
    private static final long serialVersionUID = -2274379904047683711L;

    @Schema(description = "排序")
    private OrderByItem orderBy;

}
