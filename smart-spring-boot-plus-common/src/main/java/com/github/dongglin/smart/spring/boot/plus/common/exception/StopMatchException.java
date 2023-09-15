package com.github.dongglin.smart.spring.boot.plus.common.exception;

/**
 * 一个异常：代表停止路由匹配，进入 Controller （框架内部专属异常，一般情况下开发者无需关注）
 */
public class StopMatchException extends BaseException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 6806129545290130143L;

	/**
	 * 构造
	 */
	public StopMatchException() {
		super("stop match");
	}

}
