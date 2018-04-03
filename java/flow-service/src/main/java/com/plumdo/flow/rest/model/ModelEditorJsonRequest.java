package com.plumdo.flow.rest.model;


public class ModelEditorJsonRequest {
	private String name;
	private String description;
	private String json_xml;
	private boolean addVersion;
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
	public String getJson_xml() {
		return json_xml;
	}
	public void setJson_xml(String json_xml) {
		this.json_xml = json_xml;
	}
	public boolean isAddVersion() {
		return addVersion;
	}
	public void setAddVersion(boolean addVersion) {
		this.addVersion = addVersion;
	}
	
}
