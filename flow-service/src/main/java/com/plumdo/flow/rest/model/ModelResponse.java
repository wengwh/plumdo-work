package com.plumdo.flow.rest.model;

import java.util.Date;

public class ModelResponse extends ModelRequest {

	protected String id;
	protected Date createTime;
	protected Date lastUpdateTime;
	protected Boolean deployed;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Boolean getDeployed() {
		return deployed;
	}

	public void setDeployed(Boolean deployed) {
		this.deployed = deployed;
	}


}
