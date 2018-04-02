package com.plumdo.common.exception;

/**
 * 禁止异常
 *
 * @author wengwenhui
 * @date 2018年4月2日
 */
public class IllegalArgumentException extends BaseException {

	private static final long serialVersionUID = 1L;

	public IllegalArgumentException(String ret,String msg) {
		super(ret,msg);
	}

	public IllegalArgumentException(String ret,String msg, Throwable cause) {
		super(ret,msg, cause);
	}
	
}
