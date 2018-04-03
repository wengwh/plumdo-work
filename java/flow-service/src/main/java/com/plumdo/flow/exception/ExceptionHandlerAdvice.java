package com.plumdo.flow.exception;

import org.flowable.engine.FlowableTaskAlreadyClaimedException;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * 异常全局捕获
 * @author wengwh
 *
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	// 415
	@ExceptionHandler(FlowableContentNotSupportedException.class)
	@ResponseBody
	public ErrorInfo handleNotSupported(FlowableContentNotSupportedException e) {
		LOGGER.error("Content is not supported", e);
		return new ErrorInfo("Content is not supported", e);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	// 409
	@ExceptionHandler(FlowableConflictException.class)
	@ResponseBody
	public ErrorInfo handleConflict(FlowableConflictException e) {
		LOGGER.error("Conflict", e);
		return new ErrorInfo("Conflict", e);
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	// 409
	@ExceptionHandler(FlowableTaskAlreadyClaimedException.class)
	@ResponseBody
	public ErrorInfo handleTaskAlreadyClaimed(FlowableTaskAlreadyClaimedException e) {
		LOGGER.error("Task was already claimed", e);
		return new ErrorInfo("Task was already claimed", e);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	// 404
	@ExceptionHandler(FlowableObjectNotFoundException.class)
	@ResponseBody
	public ErrorInfo handleNotFound(FlowableObjectNotFoundException e) {
		LOGGER.error("Not found", e);
		return new ErrorInfo("Not found", e);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	// 403
	@ExceptionHandler(FlowableForbiddenException.class)
	@ResponseBody
	public ErrorInfo handleForbidden(FlowableForbiddenException e) {
		LOGGER.error("Forbidden", e);
		return new ErrorInfo("Forbidden", e);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	// 400
	@ExceptionHandler(FlowableIllegalArgumentException.class)
	@ResponseBody
	public ErrorInfo handleIllegal(FlowableIllegalArgumentException e) {
		LOGGER.error("Bad request", e);
		return new ErrorInfo("Bad request", e);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	// 400
	@ExceptionHandler(HttpMessageConversionException.class)
	@ResponseBody
	public ErrorInfo handleBadMessageConversion(HttpMessageConversionException e) {
		LOGGER.error("Bad request", e);
		return new ErrorInfo("Bad request", e);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	// 500
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ErrorInfo handleOtherException(Exception e) {
		LOGGER.error("Unhandled exception", e);
		return new ErrorInfo("Internal server error", e);
	}

}
