package com.github.dongglin.smart.spring.boot.plus.common.interfaces;

/**
 * @author 11960
 * @ClassName BindQuery
 * @description: 保护字段处理器
 * @date 2023年07月18日
 * @version: 1.0
 */
public interface ProtectFieldHandler {

    /**
     * 加密
     *
     * @param content 内容
     * @return 密文
     */
    String encrypt(Class<?> clazz, String fieldName, String content);

    /**
     * 解密
     *
     * @param content 内容
     * @return 明文
     */
    String decrypt(Class<?> clazz, String fieldName, String content);

    /**
     * 脱敏处理
     *
     * @param content 字符串
     * @return 脱敏之后的字符串
     */
    String mask(Class<?> clazz, String fieldName, String content);

}
