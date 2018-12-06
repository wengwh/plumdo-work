package com.plumdo.common.exception;

/**
 * 禁止异常
 *
 * @author wengwenhui
 * @date 2018年4月2日
 */
class ForbiddenException extends BaseException {

    private static final long serialVersionUID = 1L;

    ForbiddenException(String ret, String msg) {
        super(ret, msg);
    }

}
