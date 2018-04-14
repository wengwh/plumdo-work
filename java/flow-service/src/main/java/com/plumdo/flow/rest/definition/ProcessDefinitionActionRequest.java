package com.plumdo.flow.rest.definition;

import java.util.Date;

public class ProcessDefinitionActionRequest {

	public static final String ACTION_SUSPEND = "suspend";
	public static final String ACTION_ACTIVATE = "activate";

	private boolean includeProcessInstances = false;
	private Date date;

	public void setIncludeProcessInstances(boolean includeProcessInstances) {
		this.includeProcessInstances = includeProcessInstances;
	}

	public boolean isIncludeProcessInstances() {
		return includeProcessInstances;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}
	
}
