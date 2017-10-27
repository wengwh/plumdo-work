package com.plumdo.form.rest.model;

import com.plumdo.form.rest.BaseRequest;

import io.swagger.annotations.ApiModelProperty;

public class FormModelRequest extends BaseRequest {

	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "标识")
	private String key;
	@ApiModelProperty(value = "分类")
	private String category;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}