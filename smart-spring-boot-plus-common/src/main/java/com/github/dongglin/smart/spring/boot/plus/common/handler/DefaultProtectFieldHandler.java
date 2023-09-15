
package com.github.dongglin.smart.spring.boot.plus.common.handler;

import com.github.dongglin.smart.spring.boot.plus.common.interfaces.ProtectFieldHandler;
import com.github.dongglin.smart.spring.boot.plus.common.utils.S;
import org.springframework.stereotype.Component;

/**
 * 保护字段默认处理器
 *
 * @author wind
 * @version v2.5.0
 * @date 2022/03/25
 */
@Component
public class DefaultProtectFieldHandler implements ProtectFieldHandler {

    @Override
    public String encrypt(Class<?> clazz, String fieldName, String content) {
        return null;
    }

    @Override
    public String decrypt(Class<?> clazz, String fieldName, String content) {
        return null;
    }

    @Override
    public String mask(Class<?> clazz, String fieldName, String content) {
        if (S.isBlank(content)) {
            return S.EMPTY;
        }
        int length = content.length();
        switch (length) {
            case 11:
                // 11位手机号，保留前3位和后4位
                return S.replace(content, 3, length - 4, '*');
            case 18:
                // 18位身份证号，保留前6位和后4位
                return S.replace(content, 6, length - 4, '*');
            default:
                // 其他长度，保留前0位和后4位，长度小于5位不脱敏
                return S.replace(content, 0, length - 4, '*');
        }
    }
}
