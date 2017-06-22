package com.plumdo.flow.rest.instance;

import java.util.Date;

public class HistoricProcessInstanceResponse {
	protected String id;
	protected String businessKey;
	protected String processDefinitionId;
	protected String processDefinitionName;
	protected String processDefinitionKey;
	protected Integer processDefinitionVersion;
	protected Date startTime;
	protected Date endTime;
	protected Long durationInMillis;
	protected String startUserId;
	protected String startUserName;
	protected String startActivityId;
	protected String superProcessInstanceId;
	protected String tenantId;
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

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getDurationInMillis() {
		return durationInMillis;
	}

	public void setDurationInMillis(Long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}

	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getStartActivityId() {
		return startActivityId;
	}

	public void setStartActivityId(String startActivityId) {
		this.startActivityId = startActivityId;
	}

	public String getSuperProcessInstanceId() {
		return superProcessInstanceId;
	}

	public void setSuperProcessInstanceId(String superProcessInstanceId) {
		this.superProcessInstanceId = superProcessInstanceId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantId() {
		return tenantId;
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

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
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
	
}
