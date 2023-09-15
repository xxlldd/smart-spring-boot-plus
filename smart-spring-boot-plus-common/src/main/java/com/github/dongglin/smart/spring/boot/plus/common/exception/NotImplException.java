package com.github.dongglin.smart.spring.boot.plus.common.exception;

/**
 * 一个异常：代表组件或方法未被提供有效的实现
 *
 * @author click33
 * @since 1.33.0
 */
public class NotImplException extends BaseException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 6806129545290130144L;

	/**
	 * 一个异常：代表组件或方法未被提供有效的实现
	 * @param message 异常描述
	 */
	public NotImplException(String message) {
		super(message);
	}

}

