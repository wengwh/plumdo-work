package com.plumdo.flow.rest.model;


public class ModelSaveJsonRequest {

	private String name;
	private String description;
	private String json_xml;
	private String is_deploy;
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
	public void setJson_xml(String jsonXml) {
		json_xml = jsonXml;
	}
	public String getIs_deploy() {
		return is_deploy;
	}
	public void setIs_deploy(String isDeploy) {
		is_deploy = isDeploy;
	}
	
}
