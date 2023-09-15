package com.github.dongglin.smart.spring.boot.plus.common.exception;

/**
 * 一个异常：代表停止匹配，直接退出，向前端输出结果
 */
public class BackResultException extends BaseException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 6806129545290130143L;

	/**
	 * 要输出的结果
	 */
	public Object result;

	/**
	 * 构造
	 * @param result 要输出的结果
	 */
	public BackResultException(String result) {
		super(result);
		this.result = result;
	}

}
