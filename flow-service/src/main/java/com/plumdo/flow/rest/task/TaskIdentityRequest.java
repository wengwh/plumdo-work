package com.plumdo.flow.rest.task;


public class TaskIdentityRequest {

	public static final String AUTHORIZE_GROUP = "group";
	public static final String AUTHORIZE_USER = "user";

	protected String identityId;
	protected String type;
	
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
