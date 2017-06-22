package com.plumdo.flow.rest.definition;

import java.util.List;
import java.util.Map;

public class ProcessDefinitionNodeResponse {

	protected String processDefinitionId;
	protected String processDefinitionName;
	protected String processDefinitionKey;
	protected Integer processDefinitionVersion;
	protected String tenantId;
	protected List<Map<String, String>> nodeInfo;
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public String getProcessDefinitionName() {
		return processDefinitionName;
	}
	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}
	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public List<Map<String, String>> getNodeInfo() {
		return nodeInfo;
	}
	public void setNodeInfo(List<Map<String, String>> nodeInfo) {
		this.nodeInfo = nodeInfo;
	}
	public Integer getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}
	public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}

}
