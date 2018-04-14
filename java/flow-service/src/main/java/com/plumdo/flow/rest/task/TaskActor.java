package com.plumdo.flow.rest.task;


public class TaskActor {

	public static final String TYPE_GROUP = "group";
	public static final String TYPE_USER = "user";

	private String taskDefinitionKey;
	private String multiKey;
	private String id;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

}
