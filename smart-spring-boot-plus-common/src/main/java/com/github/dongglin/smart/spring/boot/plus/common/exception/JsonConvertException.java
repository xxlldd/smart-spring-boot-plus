package com.github.dongglin.smart.spring.boot.plus.common.exception;

/**
 * 一个异常：代表 JSON 转换失败
 */
public class JsonConvertException extends BaseException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 6806129545290134144L;

	/**
	 * 一个异常：代表 JSON 转换失败
	 * @param cause 异常对象
	 */
	public JsonConvertException(Throwable cause) {
		super(cause);
	}

}
