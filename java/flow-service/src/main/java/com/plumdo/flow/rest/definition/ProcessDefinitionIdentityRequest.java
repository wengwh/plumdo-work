package com.plumdo.flow.rest.definition;


public class ProcessDefinitionIdentityRequest {
	protected String type;
	protected String identityId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
}
