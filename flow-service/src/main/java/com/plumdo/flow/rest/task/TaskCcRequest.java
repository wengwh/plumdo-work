package com.plumdo.flow.rest.task;

import java.util.List;

public class TaskCcRequest extends TaskActionRequest{

	private List<String> users;
	private List<String> groups;
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	public List<String> getGroups() {
		return groups;
	}
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	
}
