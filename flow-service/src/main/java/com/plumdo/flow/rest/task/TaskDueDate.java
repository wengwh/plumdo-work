package com.plumdo.flow.rest.task;

import java.util.Date;


public class TaskDueDate {
	private String taskDefinitionKey;
	private String multiKey;
	private Date date;

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getMultiKey() {
		return multiKey;
	}

	public void setMultiKey(String multiKey) {
		this.multiKey = multiKey;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
