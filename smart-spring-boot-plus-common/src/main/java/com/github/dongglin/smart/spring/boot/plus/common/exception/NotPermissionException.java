package com.github.dongglin.smart.spring.boot.plus.common.exception;


/**
 * 一个异常：代表会话未能通过权限认证校验
 */
public class NotPermissionException extends BaseException {

	/**
	 * 序列化版本号
	 */
	private static final long serialVersionUID = 6806129545290130141L;

	/** 权限码 */
	private final String permission;

	/**
	 * @return 获得具体缺少的权限码
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * 账号类型
	 */
	private final String loginType;

	/**
	 * 获得账号类型
	 *
	 * @return 账号类型
	 */
	public String getLoginType() {
		return loginType;
	}

	public NotPermissionException(String permission) {
		this(permission, "");
	}

	public NotPermissionException(String permission, String loginType) {
		super("无此权限：" + permission);
		this.permission = permission;
		this.loginType = loginType;
	}

}
