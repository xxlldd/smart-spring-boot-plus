package com.github.dongglin.smart.spring.boot.plus.core.framework.page;

import com.github.pagehelper.PageSerializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页结果
 *
 * @author DongGL
 * @date 2018-11-08
 */

@Slf4j
@Data
@Schema(description = "分页结果")
public class Paging<T> implements Serializable {
    private static final long serialVersionUID = 7302531776693980009L;

    @Schema(description = "总行数")
    private long total = 0;

    @Schema(description = "数据列表")
    private List<T> list = Collections.emptyList();

    public Paging() {

    }

    public Paging(List<T> list) {
        PageSerializable pageSerializable = new PageSerializable(list);
        this.total = pageSerializable.getTotal();
        this.list = pageSerializable.getList();
    }

}
