package com.plumdo.common.exception;

/**
 * 禁止异常
 *
 * @author wengwenhui
 * @date 2018年4月2日
 */
public class ForbiddenException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ForbiddenException(String ret,String msg) {
		super(ret,msg);
	}

	public ForbiddenException(String ret,String msg, Throwable cause) {
		super(ret,msg, cause);
	}
	
}
