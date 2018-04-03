package com.plumdo.form.rest;

import io.swagger.annotations.ApiModelProperty;

public class BaseRequest {

	@ApiModelProperty(value = "租户ID")
	private String tenantId;


	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
