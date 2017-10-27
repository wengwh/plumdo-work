package com.plumdo.form.rest.definition;

import com.plumdo.form.rest.BaseRequest;

import io.swagger.annotations.ApiModelProperty;


public class FormDefinitionRequest extends BaseRequest{
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "标识")
	private String key;
	@ApiModelProperty(value = "分类")
	private String category;
	@ApiModelProperty(value = "版本号")
	private Integer version;
	@ApiModelProperty(value = "描述")
	private String description;
	
	
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}