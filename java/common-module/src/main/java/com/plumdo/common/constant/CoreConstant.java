package com.plumdo.common.constant;

/**
 * 系统参数配置常量表
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
public interface CoreConstant {
    String DEFAULT_CHARSET = "UTF-8";
    String ERROR_CODE = "-1";

    long LOGIN_USER_EXPIRE_IN = 48 * 60 * 60 * 1000;
    String JWT_SECRET = "XX1#$%()(#*!()!KL<><MQLMNQNQJQK sdfkjsdrow32234545fdf>?N<:{LWPW";
    String JWT_AUTH_EXCLUDE_URL = "/auths/login";

    String HTTP_UERID_NOT_FOUND = "00003";
    String HEADER_TOKEN_NOT_FOUND = "00004";
    String HEADER_TOKEN_ILLEGAL = "00005";
    String TOKEN_USER_ID_NOT_FOUND = "00006";
    String TOKEN_EXPIRE_OUT = "00007";
    String TOKEN_AUTH_CHECK_ERROR = "00008";
}
