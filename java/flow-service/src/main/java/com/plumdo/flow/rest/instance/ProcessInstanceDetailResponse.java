package com.plumdo.flow.rest.instance;


public class ProcessInstanceDetailResponse extends HistoricProcessInstanceResponse{
	private boolean suspended;
	private String deleteReason;
	public boolean isSuspended() {
		return suspended;
	}
	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	public String getDeleteReason() {
		return deleteReason;
	}
	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	

}
