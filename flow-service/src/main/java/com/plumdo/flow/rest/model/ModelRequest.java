package com.plumdo.flow.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ModelRequest {

	protected String name;
	protected String key;
	protected String category;
	protected Integer version;
	protected String metaInfo;
	protected String tenantId;
	protected Boolean clearDeployId = false;

	protected boolean nameChanged;
	protected boolean keyChanged;
	protected boolean categoryChanged;
	protected boolean versionChanged;
	protected boolean metaInfoChanged;
	protected boolean tenantChanged;
	protected boolean clearDeployChanged;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.nameChanged = true;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
		this.keyChanged = true;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		this.categoryChanged = true;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
		this.versionChanged = true;
	}

	public String getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
		this.metaInfoChanged = true;
	}

	public void setTenantId(String tenantId) {
		tenantChanged = true;
		this.tenantId = tenantId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public Boolean getClearDeployId() {
		return clearDeployId;
	}

	public void setClearDeployId(Boolean clearDeployId) {
		this.clearDeployId = clearDeployId;
		clearDeployChanged = true;
	}
	
	@JsonIgnore
	public boolean isCategoryChanged() {
		return categoryChanged;
	}

	@JsonIgnore
	public boolean isKeyChanged() {
		return keyChanged;
	}

	@JsonIgnore
	public boolean isMetaInfoChanged() {
		return metaInfoChanged;
	}

	@JsonIgnore
	public boolean isNameChanged() {
		return nameChanged;
	}

	@JsonIgnore
	public boolean isVersionChanged() {
		return versionChanged;
	}

	@JsonIgnore
	public boolean isTenantIdChanged() {
		return tenantChanged;
	}

	@JsonIgnore
	public boolean isClearDeployChanged() {
		return clearDeployChanged;
	}
}
