package com.plumdo.flow.rest.instance.resource;

import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.exception.FlowableConflictException;
import com.plumdo.flow.rest.instance.ProcessInstanceActionRequest;


@RestController
public class ProcessInstanceSuspendResource extends BaseProcessInstanceResource {
	
	@RequestMapping(value = "/process-instance/{processInstanceId}/suspend", method = RequestMethod.PUT, name="流程实例挂起")
	@ResponseStatus(value = HttpStatus.OK)
	public void suspendProcessInstance(@PathVariable String processInstanceId,@RequestBody(required=false) ProcessInstanceActionRequest actionRequest) {
		
		ProcessInstance processInstance = getProcessInstanceFromRequest(processInstanceId);
		    
		if (processInstance.isSuspended()) {
			throw new FlowableConflictException("Process instance with id '" + processInstance.getId() + " ' is already suspend");
		}

		runtimeService.suspendProcessInstanceById(processInstance.getId());
	}
}
