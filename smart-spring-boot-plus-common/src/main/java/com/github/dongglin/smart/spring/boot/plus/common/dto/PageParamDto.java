package com.github.dongglin.smart.spring.boot.plus.common.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author 11960
 * @ClassName PageParamDto
 * @description: 分页参数类
 * @date 2023年07月01日
 * @version: 1.0
 */
@Data
@Builder
public class PageParamDto<E> {
    private int page = 1;
    private int size = 20;
    private String asc;
    private String desc;
    private E entityDto;
}
