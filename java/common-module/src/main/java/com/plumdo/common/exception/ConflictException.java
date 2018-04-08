package com.plumdo.common.exception;

/**
 * 冲突异常
 *
 * @author wengwenhui
 * @date 2018年4月2日
 */
class ConflictException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ConflictException(String ret, String msg) {
		super(ret, msg);
	}

	public ConflictException(String ret, String msg, Throwable cause) {
		super(ret, msg, cause);
	}

}
