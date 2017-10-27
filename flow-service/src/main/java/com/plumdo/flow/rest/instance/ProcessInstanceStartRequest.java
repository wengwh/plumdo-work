package com.plumdo.flow.rest.instance;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.plumdo.flow.rest.task.TaskActor;
import com.plumdo.flow.rest.variable.RestVariable;


public class ProcessInstanceStartRequest extends ProcessInstanceActionRequest{

	private String processDefinitionId;
	private String processDefinitionKey;
	private String businessKey;
	private List<RestVariable> variables;
	private List<TaskActor> nextActors;
	private boolean autoCommitTask = false;
	private String tenantId;

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public List<RestVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<RestVariable> variables) {
		this.variables = variables;
	}

	public boolean isCustomTenantSet() {
		return tenantId != null && !StringUtils.isEmpty(tenantId);
	}

	public List<TaskActor> getNextActors() {
		return nextActors;
	}

	public void setNextActors(List<TaskActor> nextActors) {
		this.nextActors = nextActors;
	}

	public boolean isAutoCommitTask() {
		return autoCommitTask;
	}

	public void setAutoCommitTask(boolean autoCommitTask) {
		this.autoCommitTask = autoCommitTask;
	}

}
