package com.github.dongglin.smart.spring.boot.plus.core.framework.util;

import java.util.UUID;

/**
 * @author DongGL
 * @date 2022/6/26
 **/
public class UUIDUtil {

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
