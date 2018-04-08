package com.plumdo.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.plumdo.common.exception.ExceptionFactory;
import com.plumdo.common.model.ErrorInfo;

/**
 * 异常全局捕获
 *
 * @author wengwenhui
 * @date 2018年4月8日
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
	private final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

	@Autowired
	private ExceptionFactory exceptionFactory;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public ErrorInfo handleIllegal(IllegalArgumentException e) {
		logger.error("参数非法异常", e);
		return new ErrorInfo(e.getRet(), e.getMessage());
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ExceptionHandler(ForbiddenException.class)
	@ResponseBody
	public ErrorInfo handleForbidden(ForbiddenException e) {
		logger.error("禁止异常", e);
		return new ErrorInfo(e.getRet(), e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ObjectNotFoundException.class)
	@ResponseBody
	public ErrorInfo handleNotFound(ObjectNotFoundException e) {
		logger.error("对象没找到异常", e);
		return new ErrorInfo(e.getRet(), e.getMessage());
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ConflictException.class)
	@ResponseBody
	public ErrorInfo handleConflict(ConflictException e) {
		logger.error("冲突异常", e);
		return new ErrorInfo(e.getRet(), e.getMessage());
	}

	@ExceptionHandler(BaseException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public ErrorInfo handleResponse(BaseException e) {
		logger.error("全局捕获自定义异常", e);
		return new ErrorInfo(e.getRet(), e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorInfo handleOtherException(Exception e) {
		logger.error("全局捕获未知异常", e);
		return exceptionFactory.createInternalError();
	}

}