package com.plumdo.flow.rest.instance;

import java.util.Date;


public class ProcessInstanceResponse {
	protected String id;
	protected String businessKey;
	protected boolean suspended;
	protected String processDefinitionId;
	protected String processDefinitionName;
	protected String processDefinitionKey;
	protected Integer processDefinitionVersion;
	protected String currentActivityId;
	protected String currentActivityName;
	protected String tenantId;
	protected Date startTime;
	protected String startUserId;
	protected String startUserName;
	protected String superProcessInstanceId;
	protected String attrStr1;
	protected String attrStr2;
	protected String attrStr3;
	protected String attrStr4;
	protected String attrStr5;
	protected String attrStr6;
	protected String attrStr7;
	protected Date attrDate1;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

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

	public Integer getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}

	public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}

	public String getCurrentActivityId() {
		return currentActivityId;
	}

	public void setCurrentActivityId(String currentActivityId) {
		this.currentActivityId = currentActivityId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getSuperProcessInstanceId() {
		return superProcessInstanceId;
	}

	public void setSuperProcessInstanceId(String superProcessInstanceId) {
		this.superProcessInstanceId = superProcessInstanceId;
	}

	public String getAttrStr1() {
		return attrStr1;
	}

	public void setAttrStr1(String attrStr1) {
		this.attrStr1 = attrStr1;
	}

	public String getAttrStr2() {
		return attrStr2;
	}

	public void setAttrStr2(String attrStr2) {
		this.attrStr2 = attrStr2;
	}

	public String getAttrStr3() {
		return attrStr3;
	}

	public void setAttrStr3(String attrStr3) {
		this.attrStr3 = attrStr3;
	}

	public String getAttrStr4() {
		return attrStr4;
	}

	public void setAttrStr4(String attrStr4) {
		this.attrStr4 = attrStr4;
	}

	public String getAttrStr5() {
		return attrStr5;
	}

	public void setAttrStr5(String attrStr5) {
		this.attrStr5 = attrStr5;
	}

	public String getAttrStr6() {
		return attrStr6;
	}

	public void setAttrStr6(String attrStr6) {
		this.attrStr6 = attrStr6;
	}

	public String getAttrStr7() {
		return attrStr7;
	}

	public void setAttrStr7(String attrStr7) {
		this.attrStr7 = attrStr7;
	}

	public Date getAttrDate1() {
		return attrDate1;
	}

	public void setAttrDate1(Date attrDate1) {
		this.attrDate1 = attrDate1;
	}

	public String getCurrentActivityName() {
		return currentActivityName;
	}

	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}

}
