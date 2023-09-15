package com.github.dongglin.smart.spring.boot.plus.common.exception;


import org.apache.commons.lang3.ObjectUtils;

/**
 * 一个异常：代表 API 参数签名校验失败
 */
public class SignException extends BaseException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 6806129545290130144L;

	/**
	 * 一个异常：代表 API 参数签名校验失败
	 * @param message 异常描述
	 */
	public SignException(String message) {
		super(message);
	}

	/**
	 * 如果flag==true，则抛出message异常
	 * @param flag 标记
	 * @param message 异常信息
	 */
	public static void throwBy(boolean flag, String message) {
		if(flag) {
			throw new SignException(message);
		}
	}

	/**
	 * 如果 value isEmpty，则抛出 message 异常
	 * @param value 值
	 * @param message 异常信息
	 */
	public static void throwByNull(Object value, String message) {
		if(ObjectUtils.isEmpty(value)) {
			throw new SignException(message);
		}
	}

}
