package com.plumdo.flow.rest.task;

import java.util.Date;


public class HistoricTaskResponse {

	protected String id;
	protected String processDefinitionId;
	protected String processDefinitionName;
	protected String processDefinitionKey;
	protected Integer processDefinitionVersion;
	protected String processInstanceId;
	protected String name;
	protected String description;
	protected String owner;
	protected String assignee;
	protected Date startTime;
	protected Date endTime;
	protected Long durationInMillis;
	protected Long workTimeInMillis;
	protected Date claimTime;
	protected String taskDefinitionKey;
	protected String formKey;
	protected Integer priority;
	protected Date dueDate;
	protected String parentTaskId;
	protected String tenantId;
	protected String category;
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

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
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

	public Long getWorkTimeInMillis() {
		return workTimeInMillis;
	}

	public void setWorkTimeInMillis(Long workTimeInMillis) {
		this.workTimeInMillis = workTimeInMillis;
	}

	public Date getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
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
