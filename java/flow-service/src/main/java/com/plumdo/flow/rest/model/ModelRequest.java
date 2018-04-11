package com.plumdo.flow.rest.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ModelRequest {
	protected String name;
	protected String key;
	protected String category;
	protected String description;
	protected String tenantId;
	protected Boolean clearDeployId = false;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTenantId(String tenantId) {
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
	}

	public String getMetaInfo() {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode metaInfo = objectMapper.createObjectNode();
		metaInfo.put("name", name);
		metaInfo.put("description", description);
		return metaInfo.toString();
	}

}
