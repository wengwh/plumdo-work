package com.plumdo.common.model;

/**
 * 验证信息线程类
 *
 * @author wengwenhui
 * @date 2018年4月12日
 */
public abstract class Authentication {

	static ThreadLocal<String> userIdThreadLocal = new ThreadLocal<String>();

	public static void setUserId(String userId) {
		userIdThreadLocal.set(userId);
	}

	public static String getUserId() {
		return userIdThreadLocal.get();
	}

	public static void clear() {
		userIdThreadLocal.remove();
	}

}