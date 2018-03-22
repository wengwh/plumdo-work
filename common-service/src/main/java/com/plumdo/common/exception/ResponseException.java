package com.plumdo.common.exception;

/**
 * 自定义异常，包含返回类
 * 
 * @author wengwenhui
 * 
 */
public class ResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String ret;

	public ResponseException(String ret,String msg) {
		super(msg);
		this.ret = ret;
	}

	public ResponseException(String ret,String msg, Throwable cause) {
		super(msg, cause);
		this.ret = ret;
	}

	public String getRet() {
		return ret;
	}
}
