package com.github.dongglin.smart.spring.boot.plus.common.exception;

import org.springframework.util.StringUtils;

/**
 */
public class EntityExistException extends BaseException {

    public EntityExistException(Class clazz, String field, String val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, val));
    }

    private static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity)
                + " with " + field + " "+ val + " existed";
    }
}
