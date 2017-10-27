package com.plumdo.form.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@ApiModel(value="错误实体类")
public class ErrorInfo {

	@ApiModelProperty(value = "错误信息",example="exception message")
	private String message;

	@ApiModelProperty(value = "错误描述",example="exception description")
	private String exception;

	public ErrorInfo(String message, Exception ex) {
		this.message = message;
		if (ex != null) {
			this.exception = ex.getLocalizedMessage();
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	@JsonInclude(Include.NON_NULL)
	public String getException() {
		return exception;
	}
}
