package com.plumdo.common.model;

/**
 * 验证信息线程类
 *
 * @author wengwenhui
 * @date 2018年4月12日
 */
public class Authentication {
    private static ThreadLocal<String> userIdThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    public static void setUserId(String userId) {
        userIdThreadLocal.set(userId);
    }

    public static String getUserId() {
        return userIdThreadLocal.get();
    }

    public static void setToken(String token) {
        tokenThreadLocal.set(token);
    }

    public static String getToken() {
        return tokenThreadLocal.get();
    }

    public static void clear() {
        userIdThreadLocal.remove();
        tokenThreadLocal.remove();
    }

}