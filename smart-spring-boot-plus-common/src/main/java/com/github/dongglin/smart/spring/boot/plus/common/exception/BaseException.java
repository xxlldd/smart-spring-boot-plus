package com.github.dongglin.smart.spring.boot.plus.common.exception;

import com.github.dongglin.smart.spring.boot.plus.common.enums.IResponseEnum;
import com.github.dongglin.smart.spring.boot.plus.common.enums.StatusCode;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 框架内部逻辑发生错误抛出的异常
 * <p> 框架其它异常均继承自此类，开发者可通过捕获此异常来捕获框架内部抛出的所有异常 </p>
 */
@Getter
public class BaseException extends RuntimeException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 6806129545290130132L;

    /**
     * 返回码
     */
    protected IResponseEnum responseEnum;
    /**
     * 异常消息参数
     */
    protected Object[] args;

    public BaseException(IResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.responseEnum = responseEnum;
    }

    public BaseException(int code, String msg) {
        super(msg);
        this.responseEnum = new IResponseEnum() {
            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return msg;
            }
        };
    }

    public BaseException(IResponseEnum responseEnum, Object[] args, String message) {
        super(message);
        this.responseEnum = responseEnum;
        this.args = args;
    }

    public BaseException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.responseEnum = responseEnum;
        this.args = args;
    }

	/**
	 * 构建一个异常
	 *
	 * @param message 异常描述信息
	 */
	public BaseException(String message) {
		super(message);
        this.responseEnum = new IResponseEnum() {
            @Override
            public int getCode() {
                return StatusCode.CODE_999999.getCode();
            }

            @Override
            public String getMessage() {
                return message;
            }
        };
	}

	/**
	 * 构建一个异常
	 *
	 * @param cause 异常对象
	 */
	public BaseException(Throwable cause) {
		super(cause);
        this.responseEnum = StatusCode.CODE_999999;
	}

	/**
	 * 构建一个异常
	 *
	 * @param message 异常信息
	 * @param cause 异常对象
	 */
	public BaseException(String message, Throwable cause) {
		super(message, cause);
        this.responseEnum = new IResponseEnum() {
            @Override
            public int getCode() {
                return StatusCode.CODE_999999.getCode();
            }

            @Override
            public String getMessage() {
                return message;
            }
        };
	}

	/**
	 * 获取异常细分状态码
	 * @return 异常细分状态码
	 */
	public int getCode() {
		return responseEnum.getCode();
	}


	/**
	 * 如果flag==true，则抛出message异常
	 * @param flag 标记
	 * @param message 异常信息
	 * @param code 异常细分状态码
	 */
	public static void throwBy(boolean flag, String message, int code) {
		if(flag) {
			throw new BaseException(code,message);
		}
	}

	/**
	 * 如果value==null或者isEmpty，则抛出message异常
	 * @param value 值
	 * @param message 异常信息
	 * @param code 异常细分状态码
	 */
	public static void throwByNull(Object value, String message, int code) {
		if(ObjectUtils.isEmpty(value)) {
			throw new BaseException(code,message);
		}
	}

}
