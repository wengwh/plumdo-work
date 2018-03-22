package com.plumdo.common.exception;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.plumdo.common.constant.CoreConstant;


/**
 * 异常工厂类
 * 
 * @author wengwenhui
 * 
 */
public class ExceptionFactory {

	private MessageSource messageSource;

	public ExceptionFactory(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/** 默认local **/
	private static final Locale defaultLocal = Locale.CHINA;

	public String getResource(String code, Object... arg) {
		return messageSource.getMessage(code, arg, defaultLocal);
	}

	public ErrorInfo createInternalError() {
		return new ErrorInfo(CoreConstant.ERROR_CODE, getResource(CoreConstant.ERROR_CODE));
	}

	public void throwInternalError() {
		throw new ResponseException(CoreConstant.ERROR_CODE, getResource(CoreConstant.ERROR_CODE));
	}

	public void throwDefinedException(String code, Object... args) {
		throw new ResponseException(code, getResource(code, args));
	}

}
