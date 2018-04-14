package com.plumdo.flow.rest.instance.resource;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;

import com.plumdo.flow.rest.RestResponseFactory;


public class BaseProcessInstanceResource {

	@Autowired
	protected RestResponseFactory restResponseFactory;

	@Autowired
	protected HistoryService historyService;

	@Autowired
	protected RuntimeService runtimeService;


	protected ProcessInstance getProcessInstanceFromRequest(String processInstanceId) {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if (processInstance == null) {
			throw new FlowableObjectNotFoundException("Could not find a run process instance with id '"+  processInstanceId + "'.",ProcessInstance.class);
		}
		return processInstance;
	}

	protected HistoricProcessInstance getHistoricProcessInstanceFromRequest(String processInstanceId) {
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if (historicProcessInstance == null) {
			throw new FlowableObjectNotFoundException("Could not find a process instance with id '" + processInstanceId + "'.",HistoricProcessInstance.class);
		}
		return historicProcessInstance;
	}
}
