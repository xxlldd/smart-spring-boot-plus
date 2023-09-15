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

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;


/**
 * @author 11960
 * @ClassName PropertiesUtils
 * @description: 配置文件工具类
 * @date 2023年07月18日
 * @version: 1.0
 */
@Slf4j
public class PropertiesUtils {

    private static Environment environment;

    /**
     * 绑定Environment
     * @param env
     */
    public static void bindEnvironment(Environment env){
        environment = env;
    }

    /***
     *  读取配置项的值
     * @param key
     * @return
     */
    public static String get(String key){
        if(environment == null){
            try{
                environment = ContextHolder.getApplicationContext().getEnvironment();
            }
            catch (Exception e){
                log.warn("无法获取Environment，参数配置可能不生效");
            }
        }
        // 获取配置值
        if(environment == null){
            log.warn("无法获取上下文Environment，请在Spring初始化之后调用!");
            return null;
        }
        String value = environment.getProperty(key);
        return value;
    }

    /***
     *  读取int型的配置项
     * @param key
     * @return
     */
    public static Integer getInteger(String key){
        // 获取配置值
        String value = get(key);
        if(ObjectUtil.isNotEmpty(value)){
            return Integer.parseInt(value);
        }
        return null;
    }

    /***
     *  读取Long型的配置项
     * @param key
     * @return
     */
    public static Long getLong(String key){
        // 获取配置值
        String value = get(key);
        if(ObjectUtil.isNotEmpty(value)){
            return Long.parseLong(value);
        }
        return null;
    }

    /***
     * 读取boolean值的配置项
     */
    public static boolean getBoolean(String key) {
        // 获取配置值
        String value = get(key);
        if(ObjectUtil.isNotEmpty(value)){
            return BooleanUtil.isTrue(Boolean.valueOf(value));
        }
        return false;
    }
}
