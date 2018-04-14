package com.plumdo.flow.rest.model;

public class ModelEditorJsonRequest {
	private String name;
	private String key;
	private String description;
	private String jsonXml;
	private boolean newVersion;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJsonXml() {
		return jsonXml;
	}

	public void setJsonXml(String jsonXml) {
		this.jsonXml = jsonXml;
	}

	public boolean isNewVersion() {
		return newVersion;
	}

	public void setNewVersion(boolean newVersion) {
		this.newVersion = newVersion;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
