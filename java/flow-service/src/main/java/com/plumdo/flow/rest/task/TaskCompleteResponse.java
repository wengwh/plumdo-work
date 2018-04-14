package com.plumdo.flow.rest.task;

import java.util.List;

public class TaskCompleteResponse extends TaskResponse{

	protected List<TaskIdentityResponse> candidate;

	public List<TaskIdentityResponse> getCandidate() {
		return candidate;
	}

	public void setCandidate(List<TaskIdentityResponse> candidate) {
		this.candidate = candidate;
	}

}
