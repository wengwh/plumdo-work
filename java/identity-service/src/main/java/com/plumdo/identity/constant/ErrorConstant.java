package com.plumdo.identity.constant;


/**
 * 错误码常量
 *
 * @author wengwenhui
 * @date 2018年3月27日
 */
public interface ErrorConstant {
    String USER_NOT_FOUND = "20001";
    String USER_ACCOUNT_REPEAT = "20002";
    String USER_ALREADY_STOP = "20003";
    String USER_PWD_NOT_MATCH = "20004";
    String USER_PASSWORD_CONFIRM_ERROR = "20006";
    String USER_OLD_PASSWORD_WRONG = "20007";

    String MENU_NOT_FOUND = "21001";
    String MENU_HAVE_CHILDREN = "21002";
    String MENU_ALREADY_ROLE_USE = "21003";

    String ROLE_NOT_FOUND = "22001";
    String ROLE_ALREADY_USER_USE = "22002";

    String GROUP_NOT_FOUND = "23001";
    String GROUP_HAVE_CHILDREN = "23002";
    String Group_ALREADY_USER_USE = "23003";

}
