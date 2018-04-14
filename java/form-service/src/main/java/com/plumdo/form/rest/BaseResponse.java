package com.plumdo.form.rest;

import java.sql.Timestamp;

import com.plumdo.form.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;

public class BaseResponse {
	
	@ApiModelProperty(value = "主键ID")
	private Long id;
	@ApiModelProperty(value = "租户ID")
	private String tenantId;
	@ApiModelProperty(value = "创建时间")
	private Timestamp createTime;
	@ApiModelProperty(value = "修改时间")
	private Timestamp lastUpdateTime;

	protected void setBaseEntity(BaseEntity baseEntity){
		this.id = baseEntity.getId();
		this.tenantId = baseEntity.getTenantId();
		this.createTime = baseEntity.getCreateTime();
		this.lastUpdateTime = baseEntity.getLastUpdateTime();
	}
	
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

}
