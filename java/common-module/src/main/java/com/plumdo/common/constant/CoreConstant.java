package com.plumdo.common.constant;

/**
 * 系统参数配置常量表
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
public class CoreConstant {
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String ERROR_CODE = "-1";

	public static final long LOGIN_USER_EXPIRE_IN = 48 * 60 * 60 * 1000;
	public static final String JWT_SECRET = "XX1#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";
	public static final String JWT_AUTH_EXCLUDE_URL = "/auths/login";

	public static final String HTTP_UERID_NOT_FOUND = "00003";
	public static final String HEADER_TOKEN_NOT_FOUND = "00004";
	public static final String HEADER_TOKEN_ILLEGAL = "00005";
	public static final String TOKEN_UERID_NOT_FOUND = "00006";
	public static final String TOKEN_EXPIRE_OUT = "00007";
	public static final String TOKEN_AUTH_CHECK_ERROR = "00008";
}
