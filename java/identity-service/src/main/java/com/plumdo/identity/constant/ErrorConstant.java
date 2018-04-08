package com.plumdo.identity.constant;


/**
 * 错误码常量
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
public class ErrorConstant {
	public static final String OBJECT_NOT_FOUND = "1001";
	
	public static final String USER_NOT_FOUND = "2001";
	public static final String USER_ACCOUNT_REPEAT = "2002";
	public static final String USER_ALREADY_STOP = "2003";
	public static final String USER_PWD_NOT_MATCH = "2004";
	public static final String USER_PASSWORD_CONFIRM_ERROR = "2006";
	public static final String USER_OLD_PASSWORD_WRONG = "2007";
	

	public static final String MENU_NOT_FOUND = "3001";
	public static final String MENU_HAVE_CHILDREN = "3002";
	public static final String MENU_ALREADY_ROLE_USE = "3003";

	public static final String ROLE_NOT_FOUND = "4001";
	public static final String ROLE_ALREADY_USER_USE = "4002";
	

	public static final String GROUP_NOT_FOUND = "5001";
	public static final String GROUP_HAVE_CHILDREN = "5002";
	public static final String Group_ALREADY_USER_USE = "5003";
	

}
