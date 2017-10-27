package com.plumdo.form.rest.data;

import com.plumdo.form.rest.BaseRequest;

import io.swagger.annotations.ApiModelProperty;


public class FormDataRequest extends BaseRequest {
	@ApiModelProperty(value = "业务Key")
	private String businessKey;
	@ApiModelProperty(value = "表单内容键")
	private String key;
	@ApiModelProperty(value = "表单内容值")
	private String value;

	public String getBusinessKey() {
		return this.businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}