package com.plumdo.flow.rest.task;

import java.util.List;

import com.plumdo.flow.rest.variable.RestVariable;


public class TaskCompleteRequest extends TaskActionRequest{

	private List<RestVariable> variables;
	private List<TaskActor> nextActors;
	private List<MultiKey> multiKeys;
	private List<TaskDueDate> nextDueDates;

	public List<RestVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<RestVariable> variables) {
		this.variables = variables;
	}

	public List<TaskActor> getNextActors() {
		return nextActors;
	}

	public void setNextActors(List<TaskActor> nextActors) {
		this.nextActors = nextActors;
	}

	public List<MultiKey> getMultiKeys() {
		return multiKeys;
	}

	public void setMultiKeys(List<MultiKey> multiKeys) {
		this.multiKeys = multiKeys;
	}

	public List<TaskDueDate> getNextDueDates() {
		return nextDueDates;
	}

	public void setNextDueDates(List<TaskDueDate> nextDueDates) {
		this.nextDueDates = nextDueDates;
	}
}
