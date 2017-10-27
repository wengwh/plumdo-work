package com.plumdo.flow.rest.task;

import java.util.ArrayList;
import java.util.List;


public class TaskNextActorResponse {

	public static final String TYPE_GROUP = "group";
	public static final String TYPE_USER = "user";
	
	private String processDefinitionId;
	private String taskDefinitionKey;
	private String taskDefinitionName;
	private List<ActorInfo> actorInfos = new ArrayList<ActorInfo>();
	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}
	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}
	
	public List<ActorInfo> getActorInfos() {
		return actorInfos;
	}
	public void setActorInfos(List<ActorInfo> actorInfos) {
		this.actorInfos = actorInfos;
	}
	
	public void addActorInfo(String id,String type,String role){
		ActorInfo actorInfo = new ActorInfo();
		actorInfo.setId(id);
		actorInfo.setType(type);
		actorInfo.setRole(role);
		actorInfos.add(actorInfo);
	}
	public String getTaskDefinitionName() {
		return taskDefinitionName;
	}
	public void setTaskDefinitionName(String taskDefinitionName) {
		this.taskDefinitionName = taskDefinitionName;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	
}

class ActorInfo{
	private String id;
	private String type;
	private String role;
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
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
