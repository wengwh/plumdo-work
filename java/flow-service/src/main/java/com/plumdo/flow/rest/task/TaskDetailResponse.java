package com.plumdo.flow.rest.task;

import org.flowable.task.api.DelegationState;

public class TaskDetailResponse extends HistoricTaskResponse {
	private String delegationState;
	private boolean suspended;

	public String getDelegationState() {
		return delegationState;
	}

	public void setDelegationState(String delegationState) {
		this.delegationState = delegationState;
	}
	public void setDelegationState(DelegationState delegationState) {
		this.delegationState = getDelegationStateString(delegationState);
	}
	
	private String getDelegationStateString(DelegationState state) {
		String result = null;
	    if(state != null) {
	      result = state.toString().toLowerCase();
	    }
	    return result;
	}
	
	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

}
