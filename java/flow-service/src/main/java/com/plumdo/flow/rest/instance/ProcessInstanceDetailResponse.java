package com.plumdo.flow.rest.instance;

public class ProcessInstanceDetailResponse extends HistoricProcessInstanceResponse {
	private boolean suspended;
	private String deleteReason;
	private String startUserName;

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

	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}

}
