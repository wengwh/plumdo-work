package com.plumdo.common.exception;

/**
 * 对象没有找到异常
 *
 * @author wengwenhui
 * @date 2018年4月2日
 */
public class ObjectNotFoundException extends BaseException {

	private static final long serialVersionUID = 1L;


	public ObjectNotFoundException(String ret,String msg) {
		super(ret,msg);
	}

	public ObjectNotFoundException(String ret,String msg, Throwable cause) {
		super(ret,msg, cause);
	}
	
}
