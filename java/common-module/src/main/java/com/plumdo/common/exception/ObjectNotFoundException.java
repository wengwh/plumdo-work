package com.plumdo.common.exception;

/**
 * 对象没有找到异常
 *
 * @author wengwenhui
 * @date 2018年4月2日
 */
class ObjectNotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;

    ObjectNotFoundException(String ret, String msg) {
        super(ret, msg);
    }

}
