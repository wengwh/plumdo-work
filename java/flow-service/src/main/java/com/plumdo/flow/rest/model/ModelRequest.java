package com.plumdo.flow.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ModelRequest {

	protected String name;
	protected String key;
	protected String category;
	protected Integer version;
	protected String description;
	protected String tenantId;
	protected Boolean clearDeployId = false;

	protected boolean nameChanged;
	protected boolean keyChanged;
	protected boolean categoryChanged;
	protected boolean versionChanged;
	protected boolean descriptionChanged;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		this.descriptionChanged = true;
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

	public String getMetaInfo() {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode metaInfo = objectMapper.createObjectNode();
		metaInfo.put("name", name);
		metaInfo.put("version", version);
		metaInfo.put("description", description);
		return metaInfo.toString();
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
		return descriptionChanged || nameChanged || versionChanged;
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
